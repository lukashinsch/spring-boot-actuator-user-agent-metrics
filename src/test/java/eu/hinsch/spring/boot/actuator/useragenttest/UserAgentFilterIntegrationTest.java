package eu.hinsch.spring.boot.actuator.useragenttest;

import eu.hinsch.spring.boot.actuator.useragent.UserAgentMetricFilter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * Created by lh on 04/06/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestPropertySource(properties = {
        "user-agent-metric.tags.browserName = #this.name",
        "user-agent-metric.url-patterns[0] = /testmvc",
        "user-agent-metric.enabled = true"
})
public class UserAgentFilterIntegrationTest {

    private static final String CHROME = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko)" +
            "Chrome/41.0.2228.0 Safari/537.36";

    @Autowired
    private UserAgentMetricFilter userAgentMetricFilter;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MeterRegistry meterRegistry;

    private MockMvc mockMvc;

    @SpringBootApplication
    @RestController
    static class TestConfig {

        @RequestMapping("/testmvc")
        @ResponseBody
        public String test() {
            return "ok";
        }
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(userAgentMetricFilter).build();
    }

    @Test
    public void shouldLogRequest() throws Exception {
        // when
        mockMvc.perform(get("/testmvc").header("User-Agent", CHROME)).andExpect(content().string("ok"));
        mockMvc.perform(get("/testmvc").header("User-Agent", CHROME)).andExpect(content().string("ok"));

        // then
        assertThat(meterRegistry.counter("user-agent", List.of(Tag.of("browserName", "Chrome"))).count(), is(2.0));
    }
}
