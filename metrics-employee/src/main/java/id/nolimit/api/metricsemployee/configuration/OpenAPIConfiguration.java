package id.nolimit.api.metricsemployee.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI sajiinOpenApi() {
        Contact contact = new Contact();
        contact.setEmail("example@gmail.com");
        contact.setName("Engineering Team");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");
        Info info = new Info()
                .title("Metrics Employee Services")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage all requirements Metrics Employee Services.")
                .termsOfService("")
                .license(mitLicense);

        return new OpenAPI().info(info);
    }

}
