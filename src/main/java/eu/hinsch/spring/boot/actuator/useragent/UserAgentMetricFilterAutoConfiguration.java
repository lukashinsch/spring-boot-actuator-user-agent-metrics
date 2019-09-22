package eu.hinsch.spring.boot.actuator.useragent;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lh on 06/06/15.
 */
@Configuration
@ComponentScan(basePackageClasses = UserAgentMetricFilterAutoConfiguration.class)
public class UserAgentMetricFilterAutoConfiguration {

    @ConditionalOnProperty(value = "user-agent-metric.enabled", havingValue = "true")
    @Bean
    FilterRegistrationBean<UserAgentMetricFilter> userAgentMetricFilterRegistrationBean(
            UserAgentMetricFilter userAgentMetricFilter,
            UserAgentMetricFilterConfiguration configuration) {
        FilterRegistrationBean<UserAgentMetricFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(userAgentMetricFilter);
        if (!configuration.getUrlPatterns().isEmpty()) {
            bean.setUrlPatterns(configuration.getUrlPatterns());
        }
        return bean;
    }

    @ConditionalOnProperty(value = "user-agent-metric.enabled", havingValue = "true")
    @Bean
    UserAgentMetricFilter userAgentMetricFilter(MeterRegistry meterRegistry,
                                                UserAgentMetricFilterConfiguration configuration) {
        return new UserAgentMetricFilter(meterRegistry, configuration);

    }
}
