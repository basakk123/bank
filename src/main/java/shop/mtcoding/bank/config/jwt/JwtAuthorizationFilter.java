package shop.mtcoding.bank.config.jwt;

import java.io.IOException;

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

import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.util.CustomResponseUtil;

/*
 * 모든 주소일 때 동작함.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 1. 헤더검증 후 헤더가 있다면 토큰 검증 후 임시 세션 생성
        if (isHeaderVerify(request, response)) {
            // 토큰 파싱하기 (Bearer 없애기)
            String token = request.getHeader(JwtProperties.HEADER_STRING)
                    .replace(JwtProperties.TOKEN_PREFIX, "");
            // 토큰 검증
            LoginUser loginUser = JwtProcess.verify(token);

            // 임시 세션 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser,
                    null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("디버그 : 토큰 검증 완료, 필터 탐");
        }

        // 2. 세션이 있는 경우와 없는 경우로 나뉘어서 컨트롤러로 진입함
        log.debug("디버그 : 필터 탐");
        chain.doFilter(request, response);
    }

    private boolean isHeaderVerify(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            return false;
        } else {
            return true;
        }
    }
}