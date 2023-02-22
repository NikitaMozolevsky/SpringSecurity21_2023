package security.springsecurity2_2023.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final String[] availablePages = new String[] {
            "https://fonts.googleapis.com/css?family=Open+Sans:400,700",
            "page/registration",
            "act/registration",
            "page/unregistered",
            "act/unregistered",
            "/api/v1/auth/**",
            "/rest/registration",
            "/registration",
            "",
    };

    @Autowired
    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter,
                                 AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean // связывание фильтра со Spring App
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
            throws Exception { //build()

        httpSecurity
                .csrf() //in diff video
                .disable()
                //filter white list
                .authorizeHttpRequests()
                .requestMatchers(availablePages)//should not be filtered
                .permitAll() //patterns
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                //filter use before auth pass filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }

}
