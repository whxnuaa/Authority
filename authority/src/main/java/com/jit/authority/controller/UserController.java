package com.jit.authority.controller;

import com.jit.authority.common.ImageUtils;
import com.jit.authority.responseResult.result.ResponseResult;
import com.jit.authority.responseResult.exceptions.DataNotFoundException;
import com.jit.authority.mapper.UserMapper;
import com.jit.authority.domain.User;
import com.jit.authority.serviceinterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


@ResponseResult
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/bb",method = RequestMethod.GET)
    public User getUsers( @RequestParam(value = "username") String username){
        User user = userMapper.findByUsername(username);
        if(user == null){
            throw new DataNotFoundException();
        }
        return user;
    }

    @RequestMapping(value = "/image",method = RequestMethod.POST)
    public ResponseEntity<Map<String,String>> updateUserImage(HttpServletRequest request, MultipartFile file) throws Exception{

        String pic_path = "D:\\tmp\\";
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String fileName = ImageUtils.ImgReceive(file,pic_path);
        if (fileName != null){
            String image = userService.getUserImage(username);
            if (image != null){
                String imageArray[] = image.split("/");
                String oldFileName = imageArray[imageArray.length-1];
                System.out.println("oldFileName " + oldFileName);
                File oldFile = new File(pic_path + oldFileName);
                System.out.println("oldFile " + oldFile);
                if (oldFile.exists()){
                    oldFile.delete();
                }
            }
            userService.updateUserImage(username,fileName);
            Map<String,String> imageUrl = new HashMap<>();
            imageUrl.put("image",fileName);
            return new ResponseEntity<>(imageUrl, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
