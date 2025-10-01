package com.example.facebookinteration.service.business;

import com.example.facebookinteration.dto.response.GetUserFacebookResponseDTO;
import com.example.facebookinteration.entity.UserFacebook;
import com.company.common.apiresponse.CustomException;
import com.example.facebookinteration.repository.UserFacebookRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserFacebookService {

    @Autowired
    private UserFacebookRepo userFacebookRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Phương thức này sẽ trả về danh sách người dùng
    public List<GetUserFacebookResponseDTO> getUsersByUserAppId(Long userAppId) {
        // Lấy danh sách user dựa trên userAppId
        List<UserFacebook> users = userFacebookRepository.findByUserAppId(userAppId);
        // Map danh sách UserFacebook sang danh sách GetUserFacebookResponseDTO
        // Nếu list 'users' rỗng, kết quả cũng sẽ là một list rỗng.
        return users.stream()
                .map(user -> modelMapper.map(user, GetUserFacebookResponseDTO.class))
                .collect(Collectors.toList());
    }
}
