package eu.hinsch.spring.boot.actuator.useragent;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.uadetector.ReadableUserAgent;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
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
    private final BeanFactory beanFactory;
    private final UserAgentMetricFilterConfiguration configuration;
    private final UserAgentParser userAgentParser;
    private final SpelExpressionParser parser = new SpelExpressionParser();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        filterChain.doFilter(request, response);

        String userAgentString = request.getHeader("User-Agent");
        if (StringUtils.hasText(userAgentString)) {
            log.debug("User agent: " + userAgentString);
            ReadableUserAgent userAgent = userAgentParser.parseUserAgentString(userAgentString);
            List<Tag> tags = configuration.getTags()
                    .entrySet()
                    .stream()
                    .map(entry -> Tag.of(entry.getKey(), evaluatePattern(userAgent, entry.getValue(), request)))
                    .collect(toList());
            meterRegistry.counter(METRIC_NAME, tags).increment();
        }
    }

    private String evaluatePattern(ReadableUserAgent userAgent, String pattern, HttpServletRequest request) {
        Expression expression = parser.parseExpression(pattern);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new WithCurrentRequestBeanFactoryResolver(beanFactory, request));
        context.setRootObject(userAgent);
        return String.valueOf(expression.getValue(context));
    }
}
