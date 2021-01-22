package org.step.linked.step.configuration.web;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.step.linked.step.configuration.DatabaseConfiguration;
import org.step.linked.step.filters.TraceLoggingFilter;
import org.step.linked.step.listeners.ContextCreationListener;
import org.step.linked.step.listeners.HttpSessionInitializationListener;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.ResourceBundle;

public class WebMvcInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static final String tempPath;
    private static final Long maxFileSize;
    private static final Long maxRequestSize;
    private static final Integer trashHoldSize;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("multipart");
        tempPath = bundle.getString("temp.path");
        maxFileSize = Long.parseLong(bundle.getString("max.file.size"));
        maxRequestSize = Long.parseLong(bundle.getString("max.request.size"));
        trashHoldSize = Integer.parseInt(bundle.getString("trash.hold.size"));
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{DatabaseConfiguration.class, WebMvcConfiguration.class};
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{new TraceLoggingFilter()};
    }

    @Override
    protected void registerContextLoaderListener(ServletContext servletContext) {
        servletContext.addListener(new ContextCreationListener());
        servletContext.addListener(new HttpSessionInitializationListener());
        super.registerContextLoaderListener(servletContext);
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        MultipartConfigElement configElement = new MultipartConfigElement(
                tempPath, maxFileSize, maxRequestSize, trashHoldSize
        );
        registration.setMultipartConfig(configElement);
    }
}
