package eu.hinsch.spring.boot.actuator.useragent;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lh on 01/06/15.
 */
public class WithCurrentRequestBeanFactoryResolver extends BeanFactoryResolver {
    private final HttpServletRequest request;

    public WithCurrentRequestBeanFactoryResolver(BeanFactory beanFactory, HttpServletRequest request) {
        super(beanFactory);
        this.request = request;
    }

    @Override
    public Object resolve(EvaluationContext context, String beanName) throws AccessException {
        if (beanName.equals("currentRequest")) {
            return request;
        }
        return super.resolve(context, beanName);
    }
}
