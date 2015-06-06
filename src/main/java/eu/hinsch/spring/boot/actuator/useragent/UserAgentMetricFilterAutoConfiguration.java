package eu.hinsch.spring.boot.actuator.useragent;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lh on 06/06/15.
 */
@Configuration
@ComponentScan(basePackageClasses = UserAgentMetricFilterAutoConfiguration.class)
public class UserAgentMetricFilterAutoConfiguration {
}
