package com.saferent.controller;

import com.saferent.dto.request.RegisterRequest;
import com.saferent.dto.response.ResponseMessage;
import com.saferent.dto.response.SfResponse;
import com.saferent.security.jwt.JwtUtils;
import com.saferent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserJwtController {

    // Bu classda sadece Login ve Register işlemleri yapılacak

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;



    // **** REGİSTER ****
    @PostMapping("/register")
    public ResponseEntity<SfResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {


        userService.saveUser(registerRequest);

        SfResponse response = new SfResponse();
        response.setMessage(ResponseMessage.REGISTER_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }









}
