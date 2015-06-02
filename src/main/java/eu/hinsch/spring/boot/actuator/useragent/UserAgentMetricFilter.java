package eu.hinsch.spring.boot.actuator.useragent;

import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by lh on 31/05/15.
 */
@Component
public class UserAgentMetricFilter implements Filter {

    private static final String DEFAULT_KEY = "#this.name + '.' + #this.versionNumber.major";

    private final CounterService counterService;
    private final BeanFactory beanFactory;
    private final UserAgentMetricFilterConfiguration configuration;
    private final SpelExpressionParser parser = new SpelExpressionParser();

    @Autowired
    public UserAgentMetricFilter(final CounterService counterService,
                                 final BeanFactory beanFactory,
                                 final UserAgentMetricFilterConfiguration configuration) {
        this.counterService = counterService;
        this.beanFactory = beanFactory;
        this.configuration = configuration;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);

        List<String> keys = !configuration.getKeys().isEmpty() ? configuration.getKeys() : asList(DEFAULT_KEY);

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;

            String userAgentString = httpServletRequest.getHeader("User-Agent");
            ReadableUserAgent userAgent = UADetectorServiceFactory.getResourceModuleParser().parse(userAgentString);
            keys.stream()
                    .map(key -> evaluatePattern(userAgent, key, httpServletRequest))
                    .map(this::formatKey)
                    .forEach(counterService::increment);
        }
    }

    private String evaluatePattern(ReadableUserAgent userAgent, String pattern, HttpServletRequest request) {
        Expression expression = parser.parseExpression(pattern);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new WithCurrentRequestBeanFactoryResolver(beanFactory, request));
        context.setRootObject(userAgent);
        return String.valueOf(expression.getValue(context));
    }

    private String formatKey(String name) {
        return name.replace(" ", "-").toLowerCase();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
