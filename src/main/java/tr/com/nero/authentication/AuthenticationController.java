package tr.com.nero.authentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.nero.authentication.ws.schemas.JwtTokenResponse;
import tr.com.nero.authentication.ws.schemas.LoginRequest;
import tr.com.nero.authentication.ws.schemas.RegisterRequest;
import tr.com.nero.common.BaseResponse;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authorization", description = "the Authorization Api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Register", description = "API Endpoint to register")
    public ResponseEntity<BaseResponse<String>> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "API Endpoint to login")
    public ResponseEntity<BaseResponse<JwtTokenResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request, false));
    }
}
