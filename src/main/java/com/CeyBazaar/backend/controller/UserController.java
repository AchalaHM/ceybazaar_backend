package com.CeyBazaar.backend.controller;

import com.CeyBazaar.backend.dto.LoginDTO;
import com.CeyBazaar.backend.dto.LoginResponseDto;
import com.CeyBazaar.backend.dto.Response;
import com.CeyBazaar.backend.dto.UserDTO;
import com.CeyBazaar.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/User")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/Registration")
    public ResponseEntity<Response<String>> userRegistration(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.userRegistration(userDTO));
    }

    @PostMapping("/Login")
    public Response<LoginResponseDto> loginUser(@RequestBody LoginDTO loginDTO) {
        return userService.userLogin(loginDTO);
    }

}
