package com.example.facebookinteration.convert;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;

import com.example.facebookinteration.constant.Constant;
import com.example.facebookinteration.constant.exception.CustomException;
import com.example.facebookinteration.dto.facebookdto.FacebookCommentResponse;
import com.example.facebookinteration.entity.CommentEntity;
import com.example.facebookinteration.entity.Phone;
import com.example.facebookinteration.repository.PhoneRepository;
import com.example.facebookinteration.utils.DataUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.restfb.types.webhook.FeedCommentValue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class FacebookCommentDtoToEntityConverter
        implements Converter<FacebookCommentResponse.CommentData, CommentEntity> {

    private static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
    private final PhoneRepository phoneRepository;

    @Override
    public CommentEntity convert(FacebookCommentResponse.CommentData source) {
        CommentEntity entity = new CommentEntity();
        entity.setId(source.getId());
        entity.setMessage(source.getMessage());
        entity.setCreatedTime(DataUtils.parseToLocalDateTime(source.getCreated_time(), DATETIME_PATTERN));
        try {
            if (source.getFrom() != null) {
                entity.setFromUser(DataUtils.objectToJson(source.getFrom()));
            }
            if (source.getAttachment() != null) {
                entity.setAttachment(source.getAttachment().getMedia().getImage().getSrc());
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to convert 'from' or 'attachment' to JSON for commentId={}, from={}, attachment={}",
                    source.getId(), source.getFrom(), source.getAttachment(), e);
            throw new CustomException(400, "Cannot convert comment for id=" + source.getId());
        }

        return entity;
    }

    public CommentEntity convert(FacebookCommentResponse.CommentData source, String postId) {
        CommentEntity entity = new CommentEntity();
        entity = convert(source);
        entity.setPostId(postId);
        return entity;
    }

    public CommentEntity toEntity(FeedCommentValue commentValue) {
        if (commentValue == null) {
            return null;
        }

        CommentEntity entity = new CommentEntity();

        entity.setId(commentValue.getCommentId());
        entity.setPostId(commentValue.getPostId());
        String message = commentValue.getMessage();
        entity.setMessage(message);

        // Chuyển đổi các đối tượng phức tạp thành chuỗi JSON để lưu trữ
        try {
            entity.setFromUser(DataUtils.objectToJson(commentValue.getFrom()));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        entity.setCreatedTime(parseFacebookTimestamp(commentValue.getCreatedTime()));
        String itemType = commentValue.getItem();
        if ("photo".equals(itemType) || "video".equals(itemType)) {
            entity.setAttachment(itemType);
        }
        boolean hasPhone = false; // Mặc định là false
        if (message != null && !message.isEmpty()) {
            // 1. Trích xuất tất cả SĐT từ message
            List<String> extractedPhoneNumbers = Constant.extractPhoneNumbers(message);

            if (!extractedPhoneNumbers.isEmpty()) {
                hasPhone = true;

                // 2. Query database MỘT LẦN DUY NHẤT để lấy về tất cả các SĐT đã tồn tại
                List<Phone> existingPhones = phoneRepository.findAllByPhoneNumberIn(extractedPhoneNumbers);

                // 3. Chuyển danh sách các SĐT đã tồn tại thành một Map để tra cứu nhanh
                // Key: số điện thoại, Value: đối tượng Phone tương ứng
                Map<String, Phone> existingPhonesMap = existingPhones.stream()
                        .collect(Collectors.toMap(Phone::getPhoneNumber, phone -> phone));

                List<Phone> phonesToSave = new ArrayList<>();
                String senderId = commentValue.getFrom().getId();

                // 4. Lặp qua danh sách SĐT vừa trích xuất để quyết định UPDATE hay INSERT
                for (String phoneNumber : extractedPhoneNumbers) {
                    // Lấy đối tượng Phone từ Map nếu nó đã tồn tại
                    Phone phoneToSave = existingPhonesMap.get(phoneNumber);

                    if (phoneToSave != null) {
                        // SĐT ĐÃ TỒN TẠI -> Cập nhật thông tin cần thiết
                        log.info("Phone number {} already exists. Updating senderId.", phoneNumber);
                        phoneToSave.setSenderId(senderId); // Ví dụ: cập nhật lại người gửi cuối cùng
                    } else {
                        // SĐT CHƯA TỒN TẠI -> Tạo một đối tượng mới
                        log.info("Phone number {} is new. Creating new record.", phoneNumber);
                        phoneToSave = Phone.builder()
                                .phoneNumber(phoneNumber)
                                .senderId(senderId)
                                .build();
                    }
                    phonesToSave.add(phoneToSave);
                }

                // 5. Gọi saveAll MỘT LẦN DUY NHẤT với danh sách đã được xử lý
                // JPA sẽ tự biết đối tượng nào cần INSERT (id=null) và đối tượng nào cần UPDATE
                // (id!=null)
                phoneRepository.saveAll(phonesToSave);
            }
        }
        entity.setHasPhone(hasPhone);
        return entity;

    }

    private LocalDateTime parseFacebookTimestamp(java.util.Date date) {
        if (date == null) {
            return LocalDateTime.now();
        }
        // RestFB đã tự parse timestamp thành java.util.Date
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
    }

}
