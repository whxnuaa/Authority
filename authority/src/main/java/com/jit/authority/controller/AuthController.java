package com.jit.authority.controller;

import com.jit.authority.responseResult.result.ResponseResult;
import com.jit.authority.domain.User;
import com.jit.authority.security.JwtAuthenticationRequest;
import com.jit.authority.security.JwtAuthenticationResponse;
import com.jit.authority.serviceinterface.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@ResponseResult
@RestController
public class AuthController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    /**
     * 登录
     * @param authenticationRequest
     * @return
     */
    @RequestMapping(value = "/auth",method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest){

        final String token = authService.login(authenticationRequest.getUsername(),authenticationRequest.getPassword());
        System.out.println("token in auth: " + token);
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }


    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        }

    }

    /**
     * 注册
     * @param addedUser
     * @return
     */
    @RequestMapping(value = "/auth/register",method = RequestMethod.POST)
    @ResponseBody
    public User register(@Validated @RequestBody User addedUser) {
                return authService.register(addedUser);
    }

}
