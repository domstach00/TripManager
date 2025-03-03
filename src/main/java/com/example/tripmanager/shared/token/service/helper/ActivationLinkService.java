package com.example.tripmanager.shared.token.service.helper;

import com.example.tripmanager.config.ActivationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

@Service
public class ActivationLinkService {
    private final ActivationProperties activationProperties;

    @Autowired
    public ActivationLinkService(ActivationProperties activationProperties) {
        this.activationProperties = activationProperties;
    }

    public String createActivationLink(String queryParamToken, String activationToken) {
        UriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
        UriBuilder builder = uriBuilderFactory.builder()
                .scheme(activationProperties.getScheme())
                .host(activationProperties.getDomain());

        if (activationProperties.getPort() > 0) {
            builder.port(activationProperties.getPort());
        }
        return builder.path(activationProperties.getPath())
                .queryParam(queryParamToken, activationToken)
                .build()
                .toString();
    }
}
