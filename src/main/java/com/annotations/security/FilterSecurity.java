package com.annotations.security;

import com.annotations.exceptions.CredentialsInvalidException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

import java.util.Base64;

import static com.annotations.exceptions.ErrorMessages.INVALID_CREDENTIAL;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;

@Slf4j
@RequestScoped
public class FilterSecurity {

    public static final String AUTHENTICATION_PASSWORD = "password";

    @Inject
    AuthenticationHandler authenticationHandler;

    @ServerRequestFilter
    public void requestFilter(ContainerRequestContext containerRequestContext) {
        var keyPassword = containerRequestContext.getHeaderString(AUTHENTICATION_PASSWORD);
        log.info("The request: {}", containerRequestContext.getRequest());
        keyPassword = encodeSecurity(keyPassword);

        if (! authenticationHandler.password().equals(keyPassword)) {
            log.warn("The request: {} has the incorrect secure key", containerRequestContext.getRequest());
            containerRequestContext.abortWith(
                    Response.status(UNAUTHORIZED)
                            .entity(new CredentialsInvalidException(INVALID_CREDENTIAL.getMessage()))
                            .build()
            );
        }
    }

    private String encodeSecurity(String key) {
        return Base64.getEncoder().encodeToString(key.getBytes());
    }
}
