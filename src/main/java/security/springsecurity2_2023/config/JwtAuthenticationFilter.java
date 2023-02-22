package security.springsecurity2_2023.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**JWT - Json Web Token (Encoded)
 * Состоит из 3 частей:
 * Header (Algorithm(it happens differently) and token type (JWT)),
 * Payload (difference data)
 * Signature (ensure indifferencible of JWT)
 * */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal( //should not be null
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);//HttpHeaders
        final String jwt;
        final String userEmail;

        // should always start with "Bearer " the gap is required (not sure)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // pass to the next filter
            return;
        }

        jwt = authHeader.substring(7); //так надо потому-что "Bearer "

        //extract from JWT token with class which can do it (JwtService)
        userEmail = jwtService.extractUsername(jwt);

        //если пользователь не аутентифицирован/не соединен
        //и его email в наличии
        if (userEmail!=null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            //get user form DB
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            //validate user, comparing retrieved and actual data
            if (jwtService.isTokenValid(jwt, userDetails)) {
                //if true - create UsernamePasswordAuthenticationToken Object
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                //еще не получены, или их нет, не разобрался
                                null,
                                userDetails.getAuthorities()
                        );
                //расширение authenticationToken данными из полученного запроса
                authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                //update security context holder with new authenticationToken
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        //нужно всегда пройти по следующим фильтрам
        filterChain.doFilter(request, response);

    }
}
