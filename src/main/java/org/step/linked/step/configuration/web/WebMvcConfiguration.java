package org.step.linked.step.configuration.web;

import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.step.linked.step"})
@PropertySources({
        @PropertySource("classpath:multipart.properties")
})
public class WebMvcConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean("multipartResolver")
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
