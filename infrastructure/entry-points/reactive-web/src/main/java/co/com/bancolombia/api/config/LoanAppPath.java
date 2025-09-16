package co.com.bancolombia.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "routes.paths")
public class LoanAppPath {

    private String loanApplication;
    private String loanCapacity;

}
