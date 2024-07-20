package tr.com.nero.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tr.com.nero.authentication.ws.schemas.JwtTokenResponse;
import tr.com.nero.authentication.ws.schemas.LoginRequest;
import tr.com.nero.authentication.ws.schemas.RegisterRequest;
import tr.com.nero.common.BaseResponse;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<String>> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<JwtTokenResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request, false));
    }
}
