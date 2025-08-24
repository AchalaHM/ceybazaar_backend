package com.CeyBazaar.backend.service;

import com.CeyBazaar.backend.dto.LoginDTO;
import com.CeyBazaar.backend.dto.LoginResponseDto;
import com.CeyBazaar.backend.dto.Response;
import com.CeyBazaar.backend.dto.UserDTO;
import com.CeyBazaar.backend.entity.User;
import com.CeyBazaar.backend.repository.UserRepository;
import com.CeyBazaar.backend.util.Constants;
import com.CeyBazaar.backend.util.EncryptionUtil;
import com.CeyBazaar.backend.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Response<String> userRegistration(UserDTO userDTO) {
        if(userRepository.existsByUserEmail(userDTO.getUserEmail())){
            logger.error("Email is already exist with another user");
            return new Response<>(Constants.ALREADY_EXIST ,"Email already exists", null);
        }

        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setUserEmail(userDTO.getUserEmail());
        user.setPassword(EncryptionUtil.encrypt(userDTO.getPassword()));
        user.setUserType(userDTO.getUserType());

        userRepository.save(user);

        ObjectMapper objectMapper = new ObjectMapper();
        String userDTOJson = "";
        try {
            userDTOJson = objectMapper.writeValueAsString(userDTO);
            logger.info("DTO successfully formatted as JSON");
        } catch (Exception e) {
            logger.error("Error while converting to JSON : " + e);
            e.printStackTrace();
        }

        logger.info("User successfully registered with email " + userDTO.getUserEmail());
        return new Response<>(Constants.SUCCESS , "User successfully registered" , userDTOJson);
    }

    @Override
    public Response<LoginResponseDto> userLogin(LoginDTO loginDTO) {
        // Find user by email
        User user = userRepository.findByUserEmail(loginDTO.getUserEmail())
                .orElse(null);

        if (user == null) {
            logger.error("User not found with email: " + loginDTO.getUserEmail());
            return new Response<>(Constants.NOT_FOUND, "Invalid email or password", null);
        }

        // Compare encrypted passwords (frontend-provided vs database-stored)
        if (!user.getPassword().equals(EncryptionUtil.encrypt(loginDTO.getPassword()))) {
            logger.error("Invalid email or password for email: " + loginDTO.getUserEmail());
            return new Response<>(Constants.NOT_FOUND, "Invalid email or password", null);
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUserEmail());
        String userName = user.getUserName();
        String userType = user.getUserType().getUserType();

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setUserName(userName);
        loginResponseDto.setUserType(userType);
        loginResponseDto.setToken(token);


        logger.info("User logged in successfully: " + user.getUserEmail());
        return new Response<>(Constants.SUCCESS, "Login successful", loginResponseDto);
    }

}
