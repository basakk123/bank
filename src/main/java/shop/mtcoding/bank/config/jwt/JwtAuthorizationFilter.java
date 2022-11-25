package shop.mtcoding.bank.config.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
            UserRepository userRepository) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.debug("디버그 : doFilterInternal 호출됨");
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            log.debug("디버그 : 토큰이 잘못됨, header가 없거나, Bearer 없음");
            chain.doFilter(request, response);
            return;
        }
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");
        log.debug("디버그 : token : " + token);

        try {
            String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                    .getClaim("username").asString();
            log.debug("디버그 : 서명검증완료 username : " + username);
            Optional<User> userOP = userRepository.findByUsername(username);

            if (userOP.isPresent()) {
                LoginUser loginUser = new LoginUser(userOP.get());

                Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser,
                        loginUser.getPassword(), loginUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // UsernamePasswordAuthenticationToken authenticationToken = new
                // UsernamePasswordAuthenticationToken(
                // userPS.getUsername(),
                // userPS.getPassword());

                // authenticationManager.authenticate(authenticationToken);

                chain.doFilter(request, response);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}