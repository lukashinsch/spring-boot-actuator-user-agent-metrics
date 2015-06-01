package eu.hinsch.spring.boot.actuator.useragent;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lh on 01/06/15.
 */
@Component
@ConfigurationProperties("user-agent-metric")
public class UserAgentMetricFilterConfiguration {
    private List<String> keys = new ArrayList<>();

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
}
