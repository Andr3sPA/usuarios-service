package co.com.bancolombia.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtPayload {
    private String subject;
    private String role;
    private Long userId;
    private Instant issuedAt;
    private Instant expiresAt;
}