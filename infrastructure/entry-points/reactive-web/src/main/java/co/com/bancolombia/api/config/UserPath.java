package co.com.bancolombia.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "routes.paths")
public class UserPath {
    private String register;
    private String login;
    private String logout;
    private String session;
}
