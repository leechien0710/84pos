package com.example.facebookinteration.exception;

import com.example.facebookinteration.constant.exception.CustomException;
import com.company.common.apiresponse.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Exception handler riêng cho FacebookIntegration để convert CustomException
 */
@ControllerAdvice
public class FacebookExceptionHandler {
    
    /**
     * Convert FacebookIntegration CustomException thành ShareLibrary ApiResponse format
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleFacebookCustomException(CustomException ex) {
        // Convert và trả về ApiResponse format trực tiếp
        com.company.common.apiresponse.CustomException shareLibraryEx = ExceptionConverter.convertToShareLibraryException(ex);
        
        ApiResponse<Object> response = ApiResponse.error(shareLibraryEx.getCode(), shareLibraryEx.getMessage());
        return ResponseEntity.status(shareLibraryEx.getCode()).body(response);
    }
    
}
