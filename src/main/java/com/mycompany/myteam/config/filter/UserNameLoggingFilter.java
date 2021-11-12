package com.mycompany.myteam.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.context.*;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import java.io.IOException;

@Slf4j
public class UserNameLoggingFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SecurityContext sc = SecurityContextHolder.getContext();
        if (sc != null) {
            if (sc.getAuthentication() != null) {
                log.debug("Adding {} as username to MDC", sc.getAuthentication().getName());
                MDC.put("username", sc.getAuthentication().getName());
            }
        } else {
            log.warn("Unable to determine username: missing security context");
        }
        chain.doFilter(request, response);
        MDC.remove("username");
    }
}
