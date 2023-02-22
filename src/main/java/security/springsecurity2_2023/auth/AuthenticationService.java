package security.springsecurity2_2023.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import security.springsecurity2_2023.config.JwtService;
import security.springsecurity2_2023.user.Role;
import security.springsecurity2_2023.user.Person;
import security.springsecurity2_2023.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    //реализация регистрации
    public AuthenticationResponse register(RegisterRequest request) {
        //настройка пользователя
        var user = Person.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        //сохранение
        userRepository.save(user);

        //представление пользователя в виде токена и возвращение
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    //аутентификация с помощью AuthenticationManager
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        //если не сработает - упадет Exception
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        //создание ответа
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
