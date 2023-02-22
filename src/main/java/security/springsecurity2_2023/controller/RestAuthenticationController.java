package security.springsecurity2_2023.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.springsecurity2_2023.auth.AuthenticationRequest;
import security.springsecurity2_2023.auth.AuthenticationResponse;
import security.springsecurity2_2023.auth.AuthenticationService;
import security.springsecurity2_2023.auth.RegisterRequest;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class RestAuthenticationController {

    // кодирует информацию при регистрации, по раскодировке можно
    // получить данные на JWT сайте (дату)

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
        @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(authenticationService.authenticate(request));

    }


}
