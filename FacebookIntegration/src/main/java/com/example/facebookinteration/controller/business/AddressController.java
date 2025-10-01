package com.example.facebookinteration.controller.business;

import com.example.facebookinteration.dto.response.AddAddressResponseDTO;
import com.example.facebookinteration.dto.response.GetAddressResponseDTO;
import com.example.facebookinteration.service.business.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    // Thêm địa chỉ cho sender (Trực tiếp nhận địa chỉ từ body và senderId từ path)
    @PostMapping("/address/{senderId}")
    public ResponseEntity<AddAddressResponseDTO> addAddress(
            @PathVariable String senderId, // Lấy senderId từ PathVariable
            @RequestBody @NotNull String address) { // Nhận địa chỉ từ body và validate

        // Gọi service để thêm địa chỉ
        AddAddressResponseDTO response = addressService.addAddress(senderId, address);

        // Trả về HTTP status 201 (Created)
        return ResponseEntity.status(201).body(response);
    }
    
    @GetMapping("/address/{senderId}")
    public ResponseEntity<List<GetAddressResponseDTO>> getAddressesBySenderId(@PathVariable String senderId) {
        // Gọi service để lấy danh sách địa chỉ của senderId
        List<GetAddressResponseDTO> addresses = addressService.getAddressesBySenderId(senderId);

        // Trả về danh sách địa chỉ với HTTP status 200 (OK)
        return ResponseEntity.ok(addresses);
    }
}
