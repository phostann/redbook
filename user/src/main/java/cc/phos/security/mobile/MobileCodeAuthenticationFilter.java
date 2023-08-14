package cc.phos.security.mobile;

import cc.phos.vo.user.MobileCodeLoginVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Collections;

public class MobileCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private ObjectMapper objectMapper;

    private final boolean postOnly = true;

    public MobileCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher("/user/login/mobile", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // get request body as UserLoginVO
        MobileCodeLoginVO mobileCodeLoginVO = objectMapper.readValue(request.getInputStream(), MobileCodeLoginVO.class);

        String mobile = mobileCodeLoginVO.getMobile();
        String code = mobileCodeLoginVO.getCode();

        if (mobile == null) {
            mobile = "";
        } else {
            mobile = mobile.trim();
        }
        if (code == null) {
            code = "";
        } else {
            code = code.trim();
        }

        MobileCodeAuthenticationToken authRequest = new MobileCodeAuthenticationToken(mobile, code, Collections.emptyList());
        this.setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request, MobileCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
