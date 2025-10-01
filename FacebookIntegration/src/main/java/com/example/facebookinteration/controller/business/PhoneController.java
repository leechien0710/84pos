package com.example.facebookinteration.controller.business;

import com.example.facebookinteration.dto.response.AddPhoneResponseDTO;
import com.example.facebookinteration.dto.response.GetPhoneResponseDTO;
import com.example.facebookinteration.service.business.PhoneService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PhoneController {

    @Autowired
    private PhoneService phoneService;

    // Endpoint để thêm số điện thoại
    @PostMapping("/phone/{senderId}")
    public ResponseEntity<AddPhoneResponseDTO> addPhone(
            @PathVariable String senderId,  // Lấy senderId từ PathVariable
            @Valid @RequestBody  @Pattern(regexp = "^(03|05|07|08|09)[0-9]{8}$", message = "Số điện thoại không hợp lệ. Vui lòng nhập lại.") String phoneNumber) {  // Validate trực tiếp phoneNumber

        // Gọi service để thêm số điện thoại cho sender
        AddPhoneResponseDTO response = phoneService.addPhone(senderId, phoneNumber);

        // Trả về HTTP status 201 (Created)
        return ResponseEntity.status(201).body(response);
    }

    // Endpoint để lấy danh sách số điện thoại theo senderId
    @GetMapping("/phone/{senderId}")
    public ResponseEntity<List<GetPhoneResponseDTO>> getPhoneNumbersBySenderId(@PathVariable @RequestParam String senderId) {
        // Gọi service để lấy danh sách số điện thoại
        List<GetPhoneResponseDTO> response = phoneService.getPhoneNumbersBySenderId(senderId);
        return ResponseEntity.ok(response); // Trả về HTTP status 200 (OK)
    }
}
