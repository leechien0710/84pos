package com.example.businessservice.service;

import com.example.businessservice.dto.response.AddAddressResponseDTO;
import com.example.businessservice.dto.response.GetAddressResponseDTO;
import com.example.businessservice.entity.Address;
import com.example.businessservice.exception.CustomException;
import com.example.businessservice.exception.ErrorMessages;
import com.example.businessservice.repository.AddressRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorMessages.ADDRESS_ALREADY_EXISTS);
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
