package com.annotations.security;

import com.annotations.exceptions.CredentialsError;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

import java.util.Base64;

import static com.annotations.exceptions.ErrorMessages.INVALID_CREDENTIAL;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;

@RequestScoped
public class FilterSecurity {

    public static final String AUTHENTICATION_PASSWORD = "authentication-password";

    @Inject
    AuthenticationHandler authenticationHandler;

    @ServerRequestFilter
    public void requestFilter(ContainerRequestContext containerRequestContext) {
        var keyPassword = containerRequestContext.getHeaderString(AUTHENTICATION_PASSWORD);
        keyPassword = encodeSecurity(keyPassword);

        if (! authenticationHandler.authenticationPassword().equals(keyPassword)) {
            containerRequestContext.abortWith(
                    Response.status(UNAUTHORIZED)
                            .entity(new CredentialsError(INVALID_CREDENTIAL.getMessage()))
                            .build()
            );
        }
    }

    private String encodeSecurity(String key) {
        return Base64.getEncoder().encodeToString(key.getBytes());
    }
}
