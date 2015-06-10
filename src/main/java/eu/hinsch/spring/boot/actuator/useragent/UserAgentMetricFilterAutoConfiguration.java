package eu.hinsch.spring.boot.actuator.useragent;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lh on 06/06/15.
 */
@Configuration
@ComponentScan(basePackageClasses = UserAgentMetricFilterAutoConfiguration.class)
public class UserAgentMetricFilterAutoConfiguration {

    @ConditionalOnProperty("user-agent-metric.url-patterns")
    @Bean
    public FilterRegistrationBean userAgentMetricFilterRegistrationBean(final UserAgentMetricFilter filter,
                                                                        UserAgentMetricFilterConfiguration configuration) {
        final FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(filter);
        bean.setUrlPatterns(configuration.getUrlPatterns());
        return bean;
    }
}
