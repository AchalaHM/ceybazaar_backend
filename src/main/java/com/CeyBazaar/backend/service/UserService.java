package com.CeyBazaar.backend.service;

import com.CeyBazaar.backend.dto.LoginDTO;
import com.CeyBazaar.backend.dto.LoginResponseDto;
import com.CeyBazaar.backend.dto.Response;
import com.CeyBazaar.backend.dto.UserDTO;

public interface UserService {
    Response<String> userRegistration(UserDTO userDTO);
    Response<LoginResponseDto> userLogin(LoginDTO loginDTO);

}
