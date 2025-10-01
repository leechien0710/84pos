package com.example.facebookinteration.constant;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {
    public static final String BASE_URL = "/api/face";
    public static final String FACE_BASE_URL = "https://graph.facebook.com/v19.0/";
    public static final String USER_ID_KEY = "userId";
    public static final String MESSAGE = "message";
    public static LocalDateTime convertStringToDate(String createdTime ){
        // Định dạng của chuỗi ngày giờ nhận vào với múi giờ kiểu +0000
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

        // Phân tích chuỗi thời gian theo định dạng đã chỉ định
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(createdTime, formatter);

        // Chuyển đổi thành LocalDateTime ở múi giờ của hệ thống
        return offsetDateTime.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }

    private static final String PHONE_NUMBER_REGEX = "(?:\\b|[^0-9])((?:o|0|84|\\+84)\\s?[1-9](?:(?:\\d|o)(?:\\s+|\\.)?){8})(?:\\b|[^0-9])";

    // Method to extract phone numbers from the text
    public static List<String> extractPhoneNumbers(String text) {
        List<String> phoneNumbers = new ArrayList<>();
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            phoneNumbers.add(matcher.group(1).replaceAll("o", "0")); // Replace 'o' with '0'
        }
        return phoneNumbers;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Field {

        public static final String LIVE_VIDEOS = "live_videos";
        public static final String FEED = "feed";
    }
}
