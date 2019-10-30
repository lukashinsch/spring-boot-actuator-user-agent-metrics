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
     * Flag to toggle parser initialization on startup.
     * This will add some seconds to the app start time at the benefit of a faster first request response.
     */
    private boolean initOnStartup = true;

    /**
     * Number of user agent strings to cache
     */
    private int cacheSize = 10000;

    /**
     * List of url patterns to apply the servlet filter to.
     * Format: ant style matcher.
     */
    private List<String> urlPatterns = new ArrayList<>();

    /**
     * List or url patterns to exclude from metrics collection.
     * Format: regex.
     */
    private List<String> excludePatterns = new ArrayList<>();

    /**
     * List of fields that will be added as micrometer tags
     */
    private List<String> tags = new ArrayList<>();
}
