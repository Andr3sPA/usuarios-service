package co.com.bancolombia.api.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class LoanAppPath {

    private final String loanApplication;

    public LoanAppPath(@Value("${routes.paths.loanApplication}") String loanApplication) {
        this.loanApplication = loanApplication;
    }
}
