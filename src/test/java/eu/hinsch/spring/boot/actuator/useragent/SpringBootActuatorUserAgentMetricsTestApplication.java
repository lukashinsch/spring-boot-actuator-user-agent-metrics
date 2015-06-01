package eu.hinsch.spring.boot.actuator.useragent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringBootActuatorUserAgentMetricsTestApplication {

    @Bean
    public FilterRegistrationBean filter(UserAgentMetricFilter filter) {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(filter);
        bean.addUrlPatterns("/test");
        return bean;
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "ok";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootActuatorUserAgentMetricsTestApplication.class, args);
    }
}
