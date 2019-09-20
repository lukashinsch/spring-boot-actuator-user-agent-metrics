package eu.hinsch.spring.boot.actuator.useragent;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
     * List of url patterns to apply the servlet filter to.
     */
    private List<String> urlPatterns = new ArrayList<>();

    /**
     * Map of micrometer tag name to expression that will be evaluated.
     * Context root will be {@link net.sf.uadetector.ReadableUserAgent}
     */
    private Map<String, String> tags = new HashMap<>();
}
