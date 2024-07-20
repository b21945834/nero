package tr.com.nero.authentication.ws.schemas;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class JwtTokenResponse {
    private String token;
    private Date expireDate;
}