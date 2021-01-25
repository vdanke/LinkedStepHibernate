package org.step.linked.step.filters;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TraceLoggingFilter implements Filter {

//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void destroy() {
//
//    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String method = request.getMethod();
        String path = request.getRequestURI();
        String log = String.format("Request to the server, PATH: %s, METHOD: %s%n", path, method);
        Lock lock = new ReentrantLock();
        CompletableFuture.runAsync(() -> {
            lock.lock();
            String pathToLogFile = "C:/Users/viele/IdeaProjects/LinkedStep/src/main/resources/logs/logs.txt";
            Resource resource = new FileSystemResource(
                    pathToLogFile
            );
            try (
                    Writer writer = new FileWriter(resource.getFile());
                    BufferedWriter bufferedWriter = new BufferedWriter(writer)
            ) {
                bufferedWriter.append(log);
                bufferedWriter.flush();
            } catch (Exception e) {
                System.out.printf("Exception during log: %s%n", e.getLocalizedMessage());
            }
            lock.unlock();
        });
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
