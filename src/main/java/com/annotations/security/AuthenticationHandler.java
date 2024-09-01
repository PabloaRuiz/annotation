package com.annotations.security;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "app")
public interface AuthenticationHandler {

    String password();
}
