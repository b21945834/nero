package tr.com.nero.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tr.com.nero.authentication.ws.schemas.JwtTokenResponse;
import tr.com.nero.authentication.ws.schemas.LoginRequest;
import tr.com.nero.authentication.ws.schemas.RegisterRequest;
import tr.com.nero.common.BaseResponse;
import tr.com.nero.common.NeroResponseStatus;
import tr.com.nero.config.JwtService;
import tr.com.nero.user.Role;
import tr.com.nero.user.User;
import tr.com.nero.user.UserService;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public BaseResponse<String> register(RegisterRequest request) {
        Optional<User> byEmail = userService.findByEmail(request.getEmail());
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            return new BaseResponse<>("Bu kullanıcı adı ile kullanıcı zaten var.");
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(Role.CLIENT)
                .iDate(LocalDate.now())
                .build();
        try {
            userService.save(user);
        }
        catch (Exception e) {
            return new BaseResponse<>("Kayıt olunurken bilinmeyen hata oluştu." + e.getMessage());
        }
        return new BaseResponse<>("Kayıt başarılı!", NeroResponseStatus.SUCCESS);
    }

    public BaseResponse<JwtTokenResponse> login(LoginRequest request, boolean isAdmin) {
        Optional<User> optUser = userService.findByEmail(request.getEmail());
        if (optUser.isEmpty()) {
            return new BaseResponse<>("Yanlış kullanıcı adı ya da şifre.");
        }
        User user = optUser.get();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return new BaseResponse<>("Yanlış kullanıcı adı ya da şifre.");
        } catch (AuthenticationException e) {
            return new BaseResponse<>("Giriş yaparken hata oluştu: " + e.getMessage());
        }
        if (!user.isEnabled()) {
            return new BaseResponse<>("Lütfen mail adresinize gelen bağlantıdan hesabınızı aktive ediniz.");
        }

        if (isAdmin && !user.getRole().equals(Role.ADMIN))
            return new BaseResponse<>("Admin değilsiniz!");

        JwtTokenResponse token = jwtService.generateToken(user);
        return new BaseResponse<>(token);
    }
}
