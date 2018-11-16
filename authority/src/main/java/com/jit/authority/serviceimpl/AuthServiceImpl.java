package com.jit.authority.serviceimpl;

import com.jit.authority.responseResult.enums.ResultCode;
import com.jit.authority.responseResult.exceptions.BusinessException;
import com.jit.authority.mapper.UserMapper;
import com.jit.authority.domain.User;
import com.jit.authority.security.JwtTokenUtil;
import com.jit.authority.security.JwtUser;
import com.jit.authority.serviceinterface.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService{

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private UserMapper userMapper;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userMapper = userMapper;
    }

    public User register(User userToAdd) {
        final String username = userToAdd.getUsername();
        System.out.println("username1111 "+username);
        if (userMapper.findByUsername(username)!=null){
//            return null;
            throw  new BusinessException(ResultCode.USER_HAS_EXISTED);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = userToAdd.getPassword();
        userToAdd.setUsername(username);
        userToAdd.setPassword(encoder.encode(rawPassword));
        userToAdd.setRegister_time(new Date());
//        final String role = userToAdd.getRole();
//        userToAdd.setRole(role);
        int i = userMapper.insert(userToAdd);
        userMapper.insertUserRole(userToAdd.getId(),1);
//        return userToAdd;
        return userMapper.findByUsername(username);
    }

    @Override
    public String login(String username, String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = userMapper.findByUsername(username);
        if (user == null || !encoder.matches(password,user.getPassword())){
            throw new BusinessException(ResultCode.USER_LOGIN_ERROR);
        }
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username,password);
        System.out.println("ffffffffffffffff1 "+upToken);
        final Authentication authentication = authenticationManager.authenticate(upToken);
//        if (encoder.matches(password, upToken.getCredentials())){
//            throw new BusinessException(ResultCode.USER_LOGIN_ERROR);
//        }
        System.out.println("authentication222  "+authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        System.out.println("userDetails"+userDetails);
        final String token = jwtTokenUtil.generateToken(userDetails);

        System.out.println("token:"+token);

        return token;

    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token,user.getLastPasswordResetDate())){
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }
}
