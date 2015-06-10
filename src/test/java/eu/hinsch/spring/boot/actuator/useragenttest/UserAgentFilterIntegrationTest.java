package eu.hinsch.spring.boot.actuator.useragenttest;

import eu.hinsch.spring.boot.actuator.useragent.UserAgentMetricFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * Created by lh on 04/06/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = UserAgentFilterIntegrationTest.TestConfig.class)
@TestPropertySource(properties = "user-agent-metric.keys = #this.name")
public class UserAgentFilterIntegrationTest {

    private static final String CHROME = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko)" +
            "Chrome/41.0.2228.0 Safari/537.36";

    private static CounterService counterService = Mockito.mock(CounterService.class);

    @Autowired
    private UserAgentMetricFilter userAgentMetricFilter;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @SpringBootApplication
    @RestController
    static class TestConfig {

        @RequestMapping("/testmvc")
        @ResponseBody
        public String test() {
            return "ok";
        }

        @Bean
        public CounterService counterService() {
            return counterService;
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

        // then
        verify(counterService).increment("user-agent.chrome");
    }
}
