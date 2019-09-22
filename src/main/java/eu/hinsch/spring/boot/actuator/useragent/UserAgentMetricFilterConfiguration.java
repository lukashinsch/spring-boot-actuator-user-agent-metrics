package eu.hinsch.spring.boot.actuator.useragent;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by lh on 01/06/15.
 */
@Data
@Component
@ConfigurationProperties("user-agent-metric")
public class UserAgentMetricFilterConfiguration {

    /**
     * Flag to turn on user agent metrics
     */
    private boolean enabled;

    /**
     * Number of user agent strings to cache
     */
    private int cacheSize = 10000;

    /**
     * List of url patterns to apply the servlet filter to.
     */
    private List<String> urlPatterns = new ArrayList<>();

    /**
     * List of fields that will be added as micrometer tags
     */
    private List<String> tags = new ArrayList<>();
}
