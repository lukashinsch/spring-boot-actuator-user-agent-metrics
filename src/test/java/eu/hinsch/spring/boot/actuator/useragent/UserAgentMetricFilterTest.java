package eu.hinsch.spring.boot.actuator.useragent;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import java.util.List;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.BeanFactory;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by lukas.hinsch on 02.06.2015.
 */
public class UserAgentMetricFilterTest {

    private static final String CHROME = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko)" +
            "Chrome/41.0.2228.0 Safari/537.36";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private BeanFactory beanFactory;

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private UserAgentMetricFilterConfiguration configuration;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Counter counter;

    @Spy
    private UserAgentParser userAgentParser = new UserAgentParser();

    @InjectMocks
    private UserAgentMetricFilter filter;

    @Captor
    private ArgumentCaptor<List<Tag>> tagListCaptor;

    @Test
    public void shouldLogExecutionWitoutTagsWithDefaultConfig() throws Exception {
        // given
        mockUserAgentHeader(CHROME);
        when(meterRegistry.counter(anyString(), anyList())).thenReturn(counter);

        // when
        filter.doFilter(request, response, filterChain);

        // then
        verify(meterRegistry).counter(eq("user-agent"), tagListCaptor.capture());
        verify(counter).increment();
        assertThat(tagListCaptor.getValue(), hasSize(0));
    }

    @Test
    public void shouldLogUserAgentWithConfiguredKeys() throws Exception {
        // given
        mockUserAgentHeader(CHROME);
        mockTagConfiguration(Map.of("name", "#this.name", "osName", "#this.operatingSystem.name"));
        when(meterRegistry.counter(anyString(), anyList())).thenReturn(counter);

        // when
        filter.doFilter(request, response, filterChain);

        // then
        verify(meterRegistry).counter(eq("user-agent"), tagListCaptor.capture());
        verify(counter).increment();
        List<Tag> tags = tagListCaptor.getValue();
        assertThat(tags, hasItem(Tag.of("name", "Chrome")));
        assertThat(tags, hasItem(Tag.of("osName", "Windows 7")));
    }

    @Test
    public void shouldLogRequestData() throws Exception {
        // given
        mockUserAgentHeader(CHROME);
        mockTagConfiguration(Map.of("customHeader", "@currentRequest.getHeader('MyHeader')"));
        when(request.getHeader("MyHeader")).thenReturn("MyHeaderValue");
        when(meterRegistry.counter(anyString(), anyList())).thenReturn(counter);

        // when
        filter.doFilter(request, response, filterChain);

        // then
        verify(meterRegistry).counter(eq("user-agent"), tagListCaptor.capture());
        verify(counter).increment();
        List<Tag> tags = tagListCaptor.getValue();
        assertThat(tags, hasItem(Tag.of("customHeader", "MyHeaderValue")));
    }

    @Test
    public void shouldUseOtherBean() throws Exception {
        // given
        mockUserAgentHeader(CHROME);
        when(beanFactory.getBean("myBean")).thenReturn("value");
        mockTagConfiguration(Map.of("customBean", "@myBean"));
        when(meterRegistry.counter(anyString(), anyList())).thenReturn(counter);

        // when
        filter.doFilter(request, response, filterChain);

        // then
        verify(meterRegistry).counter(eq("user-agent"), tagListCaptor.capture());
        verify(counter).increment();
        List<Tag> tags = tagListCaptor.getValue();
        assertThat(tags, hasItem(Tag.of("customBean", "value")));
    }

    @Test
    public void shouldCallFilterChainWithoutUserAgent() throws Exception {
        // when
        filter.doFilter(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        verify(counter, never()).increment();
        verify(meterRegistry, never()).counter(anyString(), anyList());
    }

    private void mockTagConfiguration(Map<String, String> config) {
        when(configuration.getTags()).thenReturn(config);
    }

    private void mockUserAgentHeader(String userAgent) {
        when(request.getHeader("User-Agent")).thenReturn(userAgent);
    }
}
