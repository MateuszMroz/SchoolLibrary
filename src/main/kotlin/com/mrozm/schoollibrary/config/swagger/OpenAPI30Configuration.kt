package com.mrozm.schoollibrary.config.swagger

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration

@Configuration
@SecurityScheme(
    name = "Bearer Authentication",
    type = HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
class OpenAPI30Configuration