package com.jit.skiad.controller;


import com.jit.skiad.domain.User;
import com.jit.skiad.dto.UserDTO;
import com.jit.skiad.responseResult.enums.ResultCode;
import com.jit.skiad.responseResult.exceptions.BusinessException;
import com.jit.skiad.responseResult.result.ResponseResult;
import com.jit.skiad.security.JwtAuthenticationRequest;
import com.jit.skiad.security.JwtAuthenticationResponse;
import com.jit.skiad.serviceinterface.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(value = "auth", description = "登录管理")
@ResponseResult
@RestController
public class AuthController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    /**
     * 注册账号
     * @param addedUser
     * @return
     */
    @RequestMapping(value = "/auth/register",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "注册账号",notes = "用户名、手机号和密码注册")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "Authorization", value = "该参数值（value='bearer {token}'）在request header中", paramType ="header", required = true, dataType = "String"),
            @ApiImplicitParam(name = "addedUser", value = "登录帐户和密码", required = true, dataType = "User")
    })
    public User register(@Valid @RequestBody User addedUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new BusinessException(ResultCode.DATA_IS_WRONG);
        }
        return authService.register(addedUser);
    }

    /**
     * 登录系统
     * @param authenticationRequest
     * @return
     */
    @ApiOperation(value = "登录系统",notes = "用户名或手机号和密码登录")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "Authorization", value = "该参数值（value='bearer {token}'）在request header中", paramType ="header", required = true, dataType = "String"),
            @ApiImplicitParam(name = "authenticationRequest", value = "登录帐户和密码", required = true, dataType = "JwtAuthenticationRequest")
    })
    @RequestMapping(value = "/auth",method = RequestMethod.POST)
    public UserDTO createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest){
        UserDTO userDTO = authService.login(authenticationRequest.getUsername(),authenticationRequest.getPassword());
        System.out.println("token in auth: " + userDTO);
        return userDTO;
    }

    /**
     * 刷新token
     * @param request
     * @return
     * @throws AuthenticationException
     */
//    @ApiOperation(value = "刷新token",notes = "刷新token")
//    @ApiImplicitParams({
//            //@ApiImplicitParam(name = "Authorization", value = "该参数值（value='bearer {token}'）在request header中", paramType ="header", required = true, dataType = "String"),
//            //@ApiImplicitParam(name = "request", value = "登录帐户和密码", required = true, dataType = "HttpServletRequest")
//    })
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
}
