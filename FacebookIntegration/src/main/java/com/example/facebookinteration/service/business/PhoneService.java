package com.example.facebookinteration.service.business;

import com.example.facebookinteration.dto.response.AddPhoneResponseDTO;
import com.example.facebookinteration.dto.response.GetPhoneResponseDTO;
import com.example.facebookinteration.entity.Phone;
import com.company.common.apiresponse.CustomException;
import com.example.facebookinteration.repository.PhoneRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhoneService {

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AddPhoneResponseDTO addPhone(String senderId, String phoneNumber) {
        // Kiểm tra nếu senderId đã có số điện thoại này
        Phone existingPhone = phoneRepository.findBySenderIdAndPhoneNumber(senderId, phoneNumber);
        if (existingPhone != null) {
            // Ném lỗi nếu số điện thoại đã tồn tại với senderId này
            throw new CustomException(400, "Số điện thoại đã được dùng cho tài khoản này rồi");
        }

        // Tạo mới đối tượng Phone từ thông tin nhận được
        Phone phone = new Phone();
        phone.setPhoneNumber(phoneNumber); // Sử dụng tham số phoneNumber
        phone.setSenderId(senderId); // Sử dụng tham số senderId

        // Lưu vào cơ sở dữ liệu
        phone = phoneRepository.save(phone);

        // Chuyển đổi từ Entity sang DTO phản hồi
        AddPhoneResponseDTO response = modelMapper.map(phone, AddPhoneResponseDTO.class);

        return response;
    }

    public List<GetPhoneResponseDTO> getPhoneNumbersBySenderId(String senderId) {
        // Lấy tất cả các số điện thoại của senderId từ cơ sở dữ liệu
        List<Phone> phones = phoneRepository.findBySenderId(senderId);

        // Chuyển đổi danh sách Phone thành danh sách GetPhoneResponseDTO
        return phones.stream()
                .map(phone -> modelMapper.map(phone, GetPhoneResponseDTO.class))
                .collect(Collectors.toList());
    }
}
