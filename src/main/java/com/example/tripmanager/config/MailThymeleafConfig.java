package com.example.tripmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class MailThymeleafConfig {
    public static final String EMAIL_TEMPLATE_ENGINE_BEAN_NAME = "emailTemplateEngine";

    @Bean
    public SpringResourceTemplateResolver emailTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/emails/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCheckExistence(true);
        resolver.setOrder(1);
        return resolver;
    }

    @Bean(name = MailThymeleafConfig.EMAIL_TEMPLATE_ENGINE_BEAN_NAME)
    public SpringTemplateEngine emailTemplateEngine(ITemplateResolver emailTemplateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(emailTemplateResolver);
        return templateEngine;
    }
}
