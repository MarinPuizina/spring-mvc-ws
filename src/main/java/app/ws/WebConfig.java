package app.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public void addCorsMappings(CorsRegistry registry) {


        registry
                .addMapping("/**") // We can map for specific requests this way "/users/email-verification"
                .allowedMethods("*") // We can provide specific methods "GET","POST"...
                .allowedOrigins("*"); // We can configure specific origin

    }

}
