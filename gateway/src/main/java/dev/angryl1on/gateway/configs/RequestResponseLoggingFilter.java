package dev.angryl1on.gateway.configs;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Servlet filter for logging incoming HTTP requests and outgoing HTTP responses.
 *
 * <p>This filter logs basic details of HTTP requests and responses, including:
 * <ul>
 *   <li>Request method (e.g., GET, POST) and URI.</li>
 *   <li>Response status code.</li>
 * </ul>
 * </p>
 *
 * <p>It is designed to be registered as a component in the Spring application context
 * and applies to all incoming and outgoing HTTP traffic.</p>
 *
 * <p>Usage of this filter is intended for debugging and monitoring purposes. Ensure
 * that sensitive information is not logged to comply with security and privacy guidelines.</p>
 *
 * @author AngryL1on
 * @version 1.0
 * @since 1.0
 */
@Component
public class RequestResponseLoggingFilter implements Filter {

    /**
     * Logger for logging HTTP request and response details.
     */
    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    /**
     * Logs the HTTP request method and URI before passing the request to the next filter in the chain.
     * After processing the request, logs the HTTP response status code.
     *
     * @param request  The incoming {@link ServletRequest}, cast to {@link HttpServletRequest}.
     * @param response The outgoing {@link ServletResponse}, cast to {@link HttpServletResponse}.
     * @param chain    The {@link FilterChain} for passing the request/response to the next filter.
     * @throws IOException If an I/O error occurs during request processing.
     * @throws jakarta.servlet.ServletException If a servlet-specific error occurs.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, jakarta.servlet.ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Log incoming HTTP request details
        logger.info("Incoming Request: method={}, URI={}", httpRequest.getMethod(), httpRequest.getRequestURI());

        // Pass the request along the filter chain
        chain.doFilter(request, response);

        // Log outgoing HTTP response details
        logger.info("Outgoing Response: status={}", httpResponse.getStatus());
    }
}
