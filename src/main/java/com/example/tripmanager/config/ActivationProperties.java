package com.example.tripmanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for activation links and token-based URLs.
 * <p>
 * Properties can be overridden through application configuration in the format:
 * {@code app.activation.<property>=<value>} or via environment variables.
 *
 * <p>Example YAML configuration:
 * <pre>
 * app:
 *   activation:
 *     scheme: https
 *     domain: api.example.com
 *     path: /v1/auth/activate
 *     port: 8443
 * </pre>
 *
 * <p>Default values are production-ready. For development environments, consider:
 * <pre>
 * port: 8080
 * scheme: http
 * domain: localhost
 * </pre>
 *
 * @see org.springframework.boot.context.properties.ConfigurationProperties
 */
@Component
@ConfigurationProperties(prefix = "app.activation")
public class ActivationProperties {

    /**
     * URL protocol for activation links (http/https)
     * <p>Default: {@value #DEFAULT_SCHEME}
     */
    private String scheme = DEFAULT_SCHEME;
    private static final String DEFAULT_SCHEME = "https";

    /**
     * Base domain for generated activation links
     * <p>Required property - must be configured in application properties
     * <p>Can be provided through {@code APP_DOMAIN} environment variable
     */
    private String domain;

    /**
     * Port for activation URLs
     * <p>Special values:
     * <ul>
     *   <li>{@code -1} - omits port in URL (uses scheme's default port)</li>
     *   <li>{@code 0} - invalid value (will throw configuration exception)</li>
     * </ul>
     */
    private int port = -1;

    /**
     * Activation endpoint path
     * <p>Should be relative to the application context root
     * <p>Default: {@value #DEFAULT_PATH}
     */
    private String path = DEFAULT_PATH;
    private static final String DEFAULT_PATH = "/activate";

    public ActivationProperties() {
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
