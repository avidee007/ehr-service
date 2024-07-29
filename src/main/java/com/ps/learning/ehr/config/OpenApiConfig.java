package com.ps.learning.ehr.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openApiBean() {

    return new OpenAPI()
        .info(getInfo())
        .addSecurityItem(new SecurityRequirement().addList("Authorization"))
        .components(new Components().addSecuritySchemes
            ("Basic Authentication", securityScheme()));
  }

  private Info getInfo() {
    return new Info()
        .title("Electronic Patient Record (EHR-Service) APIs")
        .version("1.0")
        .description("Electronic Patient Record service API documentation");
  }


  public SecurityScheme securityScheme() {
    return new SecurityScheme()
        .name("basic")
        .in(SecurityScheme.In.HEADER)
        .scheme("basic")
        .description("Enter your username and password for authentication.")
        .type(SecurityScheme.Type.HTTP);

  }
}
