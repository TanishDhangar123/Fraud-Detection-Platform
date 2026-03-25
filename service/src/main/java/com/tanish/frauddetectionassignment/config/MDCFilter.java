package com.tanish.frauddetectionassignment.config;


import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class MDCFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        try {

            String txId = UUID.randomUUID().toString();

            MDC.put("transactionId", txId);

            log.info("Starting request {} with transactionId = {}"
                    , ((HttpServletRequest) servletRequest).getRequestURI(), txId);

            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.clear();
        }
    }
}
