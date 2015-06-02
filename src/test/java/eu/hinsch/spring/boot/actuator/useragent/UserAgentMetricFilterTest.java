package eu.hinsch.spring.boot.actuator.useragent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.actuate.metrics.CounterService;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by lukas.hinsch on 02.06.2015.
 */
public class UserAgentMetricFilterTest {

    private static final String CHROME = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko)" +
            "Chrome/41.0.2228.0 Safari/537.36";

    @Mock
    private BeanFactory beanFactory;

    @Mock
    private CounterService counterService;

    @Mock
    private UserAgentMetricFilterConfiguration configuration;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private UserAgentMetricFilter filter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldLogUserAgentWithDefaultConfig() throws Exception {
        // given
        when(request.getHeader("User-Agent")).thenReturn(CHROME);

        // when
        filter.doFilter(request, response, filterChain);

        // then
        verify(counterService).increment("chrome.41");
    }

    @Test
    public void shouldLogUserAgentWithConfiguredKeys() throws Exception {
        // given
        when(request.getHeader("User-Agent")).thenReturn(CHROME);
        when(configuration.getKeys()).thenReturn(asList("#this.name", "#this.operatingSystem.name"));

        // when
        filter.doFilter(request, response, filterChain);

        // then
        verify(counterService).increment("chrome");
        verify(counterService).increment("windows-7");
    }
}
