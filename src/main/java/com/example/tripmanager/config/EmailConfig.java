package com.example.tripmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

@Configuration
public class EmailConfig {
    public static final String templatePrefixPath = "classpath:/emails/templates/";
    public static final String templateSuffix = ".html";
    public static final String templateMode = "HTML";
    public static final String characterEncoding = "UTF-8";

    private final ApplicationContext applicationContext;

    @Autowired
    public EmailConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        SpringResourceTemplateResolver emailTemplateResolver = emailTemplateResolver();
        templateEngine.setTemplateResolver(emailTemplateResolver);
        return templateEngine;
    }



    private SpringResourceTemplateResolver emailTemplateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix(templatePrefixPath);
        templateResolver.setSuffix(templateSuffix);
        templateResolver.setTemplateMode(templateMode);
        templateResolver.setCharacterEncoding(characterEncoding);
        templateResolver.setCacheable(true);
        return templateResolver;
    }
}
