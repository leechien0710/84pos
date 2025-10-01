package com.example.facebookinteration.service.business;

import com.example.facebookinteration.dto.response.AddAddressResponseDTO;
import com.example.facebookinteration.dto.response.GetAddressResponseDTO;
import com.example.facebookinteration.entity.Address;
import com.company.common.apiresponse.CustomException;
import com.example.facebookinteration.repository.AddressRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AddAddressResponseDTO addAddress(String senderId, String address) {
        // Chuẩn hóa address từ request để so sánh
        String normalizedAddress = address.trim().toLowerCase();

        // Tìm xem đã có bản ghi nào khớp senderId và address chuẩn hóa chưa
        List<Address> existingAddresses = addressRepository.findBySenderId(senderId);
        boolean isAddressExists = existingAddresses.stream()
                .anyMatch(addr -> addr.getAddress().trim().toLowerCase().equals(normalizedAddress));

        if (isAddressExists) {
            throw new CustomException(400, "Địa chỉ đã được dùng cho tài khoản này rồi");
        }

        // Tạo đối tượng Address từ senderId và address
        Address newAddress = new Address();
        newAddress.setSenderId(senderId);
        newAddress.setAddress(address); // Giữ nguyên định dạng ban đầu khi lưu

        // Lưu vào DB
        Address savedAddress = addressRepository.save(newAddress);

        // Chuyển đổi đối tượng Address thành AddAddressResponseDTO và trả về
        return modelMapper.map(savedAddress, AddAddressResponseDTO.class);
    }

    public List<GetAddressResponseDTO> getAddressesBySenderId(String senderId) {
        // Lấy tất cả các địa chỉ của senderId từ cơ sở dữ liệu
        List<Address> addresses = addressRepository.findBySenderId(senderId);

        // Chuyển đổi danh sách Address thành danh sách GetAddressResponseDTO
        return addresses.stream()
                .map(address -> modelMapper.map(address, GetAddressResponseDTO.class))
                .collect(Collectors.toList());
    }
}
