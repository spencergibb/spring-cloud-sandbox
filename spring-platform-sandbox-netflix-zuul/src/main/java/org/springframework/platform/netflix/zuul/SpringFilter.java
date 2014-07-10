package org.springframework.platform.netflix.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * User: spencergibb
 * Date: 5/1/14
 * Time: 10:59 PM
 */
public abstract class SpringFilter extends ZuulFilter {

    protected <T> T getBean(Class<T> beanClass) {
        return getBean(RequestContext.getCurrentContext(), beanClass);
    }

    protected <T> T getBean(RequestContext rc, Class<T> beanClass) {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(
                rc.getRequest().getServletContext()).getBean(beanClass);
    }
}
