package eu.hinsch.spring.boot.actuator.useragent;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import static java.util.stream.Collectors.toList;

/**
 * Created by lh on 31/05/15.
 */
@Slf4j
@RequiredArgsConstructor
public class UserAgentMetricFilter extends OncePerRequestFilter {

    private static final String METRIC_NAME = "user-agent";

    private final MeterRegistry meterRegistry;
    private final UserAgentMetricFilterConfiguration configuration;
    private UserAgentAnalyzer userAgentAnalyzer;
    private List<Pattern> excludePatterns;

    @PostConstruct
    public void initialize() {
        var builder = UserAgentAnalyzer.newBuilder()
                .hideMatcherLoadStats()
                .withCache(configuration.getCacheSize())
                .withFields(configuration.getTags().toArray(String[]::new));
        if (configuration.isInitOnStartup()) {
            builder.preheat();
        }
        userAgentAnalyzer = builder.build();
        excludePatterns = configuration.getExcludePatterns().stream()
                .map(Pattern::compile)
                .collect(Collectors.toList());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        filterChain.doFilter(request, response);

        String userAgentString = request.getHeader("User-Agent");
        if (StringUtils.hasText(userAgentString)) {
            if (log.isDebugEnabled()) {
                log.debug("User agent: " + userAgentString);
            }

            UserAgent agent = userAgentAnalyzer.parse(userAgentString);
            List<Tag> tags = configuration.getTags()
                    .stream()
                    .map(tag -> Tag.of(tag, agent.getValue(tag)))
                    .collect(toList());
            meterRegistry.counter(METRIC_NAME, tags).increment();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return excludePatterns.stream()
                .anyMatch(pattern -> pattern.matcher(request.getRequestURI()).matches());
    }
}
