package eu.hinsch.spring.boot.actuator.useragent;

import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
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

    private final CounterService counterService;

    @Autowired
    public UserAgentMetricFilter(CounterService counterService) {
        this.counterService = counterService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;

            ReadableUserAgent userAgent = UADetectorServiceFactory.getResourceModuleParser().parse(httpServletRequest.getHeader("User-Agent"));
            generateKeys(userAgent).stream().forEach(counterService::increment);
        }
    }

    // TODO to be replaced by configurable patterns
    private List<String> generateKeys(ReadableUserAgent userAgent) {
        String name = escape(userAgent.getName());

        String fullKey = name + "." + userAgent.getVersionNumber().getMajor();
        String osKey = escape(userAgent.getOperatingSystem().getName());

        return asList(fullKey, name, osKey);
    }

    private String escape(String name) {
        return name.replace(" ", "-").toLowerCase();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
