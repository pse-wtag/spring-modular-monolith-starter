package mu.welldev.rest.configuration;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.resilience.annotation.EnableResilientMethods;

@Configuration
@EnableResilientMethods
@ComponentScan(basePackages = "mu.welldev")
@EntityScan(basePackages = "mu.welldev.persistence.entity")
@EnableJpaRepositories(basePackages = "mu.welldev.persistence.repository")
public class ApplicationConfiguration {
}
