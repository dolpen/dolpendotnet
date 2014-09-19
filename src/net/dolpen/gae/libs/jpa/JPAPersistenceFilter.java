package net.dolpen.gae.libs.jpa;

import javax.servlet.*;
import java.io.IOException;

/**
 * JPAのトランザクションをthreadlocalに割り当てるためのフィルタです
 */
public class JPAPersistenceFilter implements Filter{
    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {
        JPA.bindForCurrentThread();
        filterChain.doFilter(request, response);
        JPA.unbindFromCurrentThread();
    }

    public void destroy() {
    }
}
