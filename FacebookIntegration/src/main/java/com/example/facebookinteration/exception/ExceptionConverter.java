package com.example.facebookinteration.exception;

import com.example.facebookinteration.constant.exception.CustomException;

/**
 * Converter để convert FacebookIntegration CustomException thành ShareLibrary CustomException
 */
public class ExceptionConverter {
    
    /**
     * Convert FacebookIntegration CustomException thành ShareLibrary CustomException
     * 
     * @param facebookEx FacebookIntegration CustomException
     * @return ShareLibrary CustomException
     */
    public static com.company.common.apiresponse.CustomException convertToShareLibraryException(CustomException facebookEx) {
        int statusCode = facebookEx.getErrorResponse().getStatusCode();
        String message = facebookEx.getErrorResponse().getMessage();
        
        return new com.company.common.apiresponse.CustomException(statusCode, message);
    }
}
