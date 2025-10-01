package com.example.businessservice.service;

import com.example.businessservice.dto.response.GetUserFacebookResponseDTO;
import com.example.businessservice.entity.UserFacebook;
import com.example.businessservice.exception.CustomException;
import com.example.businessservice.exception.ErrorMessages;
import com.example.businessservice.repository.UserFacebookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserFacebookService {

    @Autowired
    private UserFacebookRepository userFacebookRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Phương thức này sẽ trả về danh sách người dùng
    public List<GetUserFacebookResponseDTO> getUsersByUserAppId(Long userAppId) {
        // Lấy danh sách user dựa trên userAppId
        List<UserFacebook> users = userFacebookRepository.findByUserAppId(userAppId);
        if (!users.isEmpty()) {
            // Map danh sách UserFacebook sang danh sách GetUserFacebookResponseDTO
            return users.stream()
                    .map(user -> modelMapper.map(user, GetUserFacebookResponseDTO.class))
                    .collect(Collectors.toList());
        }
        throw new CustomException(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
    }

}
