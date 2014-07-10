package filters.pre

import com.netflix.zuul.context.RequestContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.platform.netflix.zuul.SpringFilter

class PreDecorationFilter extends SpringFilter {
    private static Logger LOG = LoggerFactory.getLogger(PreDecorationFilter.class);

    @Override
    int filterOrder() {
        return 5
    }

    @Override
    String filterType() {
        return "pre"
    }

    @Override
    boolean shouldFilter() {
        return true;
    }

    @Override
    Object run() {
        RequestContext ctx = RequestContext.getCurrentContext()

        def requestURI = ctx.getRequest().getRequestURI()

        //FIXME: hard coded to sample frontend
        def serviceId = "samplefrontendservice";
        //if (requestURI.startsWith(path)) {

        if (serviceId != null) {
            // set serviceId for use in filters.route.RibbonRequest
            ctx.set("serviceId", serviceId)
            ctx.setRouteHost(null)
            ctx.addOriginResponseHeader("X-Zuul-ServiceId", serviceId);
        }
    }
}
