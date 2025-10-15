package com.example.facebookinteration.controller;

import com.example.facebookinteration.constant.Constant;
import com.example.facebookinteration.constant.enums.PageStatus;
import com.example.facebookinteration.constant.exception.CustomException;
import com.example.facebookinteration.dto.req.PageMesReq;
import com.example.facebookinteration.entity.Conversation;
import com.example.facebookinteration.entity.Message;
import com.example.facebookinteration.entity.Sender;
import com.example.facebookinteration.entity.PageEntity;
import com.example.facebookinteration.entity.Phone;
import com.example.facebookinteration.entity.UserFacebook;
import com.example.facebookinteration.repository.ConversationRepository;
import com.example.facebookinteration.repository.MessageRepository;
import com.example.facebookinteration.repository.PageRepository;
import com.example.facebookinteration.repository.PhoneRepository;
import com.example.facebookinteration.repository.SenderRepository;
import com.example.facebookinteration.repository.UserFacebookRepo;
import com.example.facebookinteration.service.PostService;
import com.example.facebookinteration.service.VideoService;
import com.example.facebookinteration.service.impl.PageService;
import com.example.facebookinteration.service.impl.TokenService;
import com.example.facebookinteration.service.business.ConversationService;
import com.example.facebookinteration.dto.response.GetConversationResponseDTO;
import org.springframework.data.domain.Page;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import org.springframework.context.ApplicationEventPublisher;
import com.example.facebookinteration.event.PageSyncEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfb.types.User;
import com.example.facebookinteration.repository.UserRepository;

@Controller
@RequestMapping()
@Slf4j
public class FaceController {
    @Value("${facebook.app.id}")
    private String clientId;
    @Value("${facebook.app.secret}")
    private String clientSecret;
    @Value("${facebook.redirect.uri}")
    private String redirectUri;
    @Value("${facebook.scope}")
    private String scope;
    @Value("${facebook.url.frontend}")
    private String frontendUrl;
    @Value("${facebook.url.authorization}")
    private String authorizationUrl;
    @Value("${facebook.url.token}")
    private String tokenUrl;
    @Value("${facebook.url.user-info}")
    private String userInfoUrl;
    @Value("${facebook.url.pages}")
    private String pagesUrl;
    @Value("${facebook.url.conversation}")
    private String conversationUrl;
    @Value("${facebook.url.graph-api-base}")
    private String graphApiBaseUrl;

    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private UserFacebookRepo userFacebookRepo;
    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WebClient webClient;
    @Autowired
    private ExecutorService executor;
    @Qualifier("taskExecutor")
    @Autowired
    private Executor taskExecutor;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private SenderRepository senderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private PostService postService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private PageService pageService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private static final String RESPONSE_TYPE = "code";
    private static final Logger logger = LoggerFactory.getLogger(FaceController.class);

    @GetMapping("/auth-url")
    public ResponseEntity<Map<String, String>> redirectToFacebookAuth() {
        String state = ThreadContext.get(Constant.USER_ID_KEY);

        // Kiểm tra nếu state là null, ném ngoại lệ hoặc xử lý
        if (!StringUtils.hasText(state)) {
            logger.warn("Auth URL failed - missing state");
            throw new CustomException(404, "State parameter is not found");
        }
        String url = UriComponentsBuilder.fromHttpUrl(authorizationUrl)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", RESPONSE_TYPE)
                .queryParam("scope", scope)
                .queryParam("state", state) // Thêm tham số state
                .toUriString();
        // Tạo response body chứa URL
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("redirectUrl", url);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/callback")
    public void callback(@RequestParam("code") String code, @RequestParam("state") String state,
            HttpServletResponse res) throws IOException {
        try {
            // Tạo URL yêu cầu token truy cập từ Facebook
            String url = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                    .queryParam("client_id", clientId)
                    .queryParam("redirect_uri", redirectUri)
                    .queryParam("client_secret", clientSecret)
                    .queryParam("code", code)
                    .toUriString();

            // Sử dụng RestTemplate để gửi yêu cầu GET và nhận phản hồi
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (!StringUtils.hasText(state)) {
                logger.warn("Callback failed - missing state, code={} ", code);
                throw new CustomException(404, "State Not Found");
            }
            Long stateValue;
            try {
                // Chuyển đổi "state" thành kiểu long
                stateValue = Long.parseLong(state);
                System.out.println("Parsed state value: " + stateValue);
            } catch (NumberFormatException e) {
                logger.warn("Callback failed - invalid state format, state={} ", state);
                throw new CustomException(404, "State Not Invalid");
            }
            // Lấy token truy cập từ phản hồi
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null) {
                String accessToken = (String) responseBody.get("access_token");
                if (!StringUtils.hasText(accessToken)) {
                    logger.warn("Callback failed - access_token missing in response body, state={}", state);
                    throw new CustomException(403, "Không có access token trong data body");
                }

                // Lấy thông tin người dùng từ Facebook
                UserFacebook userFacebook = getUserProfile(accessToken, stateValue);
                getUserPages(userFacebook, accessToken);
            } else {
                logger.warn("Callback failed - response body null, state={} ", state);
                throw new CustomException(403, "User Access token not found");
            }
            res.sendRedirect(frontendUrl);
        } catch (HttpClientErrorException e) {
            throw new CustomException(e.getStatusCode().value(), e.getMessage());
        }
    }

    private UserFacebook getUserProfile(String accessToken, Long state) {
        try {
            // Tạo URL yêu cầu thông tin người dùng từ Facebook
            String url = UriComponentsBuilder.fromHttpUrl(userInfoUrl)
                    .queryParam("access_token", accessToken)
                    .queryParam("fields", "id,name,picture") // Thêm trường picture để lấy ảnh đại diện
                    .toUriString();

            // Sử dụng RestTemplate để gửi yêu cầu GET và nhận phản hồi
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            // Xử lý thông tin người dùng
            Map<String, Object> userInfo = response.getBody();
            if (userInfo != null) {
                String userId = (String) userInfo.get("id");
                String userName = (String) userInfo.get("name");

                // Lấy thông tin ảnh đại diện từ trường picture
                Map<String, Object> pictureData = (Map<String, Object>) userInfo.get("picture");
                Map<String, Object> pictureDataData = (Map<String, Object>) pictureData.get("data");
                String profilePictureUrl = (String) pictureDataData.get("url");

                return userFacebookRepo.save(UserFacebook.builder()
                        .userId(userId)
                        .userAppId(state)
                        .name(userName)
                        .userAccessToken(tokenService.getLongLivedToken(clientId, clientSecret, accessToken))
                        .avatar(profilePictureUrl)
                        // .expiresAt(tokenService.debugAccessToken(accessToken))
                        .build());
            } else {
                logger.warn("getUserProfile failed - empty userInfo, state={} ", state);
                throw new CustomException(404, "Failed to retrieve user profile");
            }
        } catch (HttpClientErrorException e) {
            throw new CustomException(e.getStatusCode().value(), e.getMessage());
        } catch (Exception e) {
            throw new CustomException(500, "An error occurred while retrieving user profile: " + e.getMessage());
        }
    }

    public void getUserPages(UserFacebook userFacebook, String accessToken) {
        try {
            // Tạo URL yêu cầu danh sách các trang của người dùng
            String url = UriComponentsBuilder.fromHttpUrl(pagesUrl)
                    .queryParam("access_token", accessToken)
                    .queryParam("fields", "id,name,picture,access_token") // Thêm trường picture để lấy avatar
                    .toUriString();

            // Sử dụng RestTemplate để gửi yêu cầu GET và nhận phản hồi
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            // Xử lý danh sách các trang
            Map<String, Object> pagesInfo = response.getBody();
            if (pagesInfo != null) {
                Object data = pagesInfo.get("data");
                // Tạo một danh sách để lưu các thực thể PageEntity
                List<PageEntity> pageEntities = new ArrayList<>();
                List<Sender> senders = new ArrayList<>();
                if (data instanceof List<?>) {
                    List<Map<String, Object>> pagesList = (List<Map<String, Object>>) data;
                    for (Map<String, Object> page : pagesList) {
                        String pageId = (String) page.get("id");
                        String pageName = (String) page.get("name");
                        String pageAccessToken = (String) page.get("access_token");
                        // Lấy thông tin avatar từ trường picture
                        Map<String, Object> pictureData = (Map<String, Object>) page.get("picture");
                        Map<String, Object> pictureDetails = (Map<String, Object>) pictureData.get("data");
                        String pageAvatarUrl = (String) pictureDetails.get("url");
                        senders.add(new Sender(pageId, pageName, pageAvatarUrl));
                        PageEntity pageEntity = PageEntity.builder()
                                .pageId(pageId)
                                .userId(userFacebook.getUserId())
                                .status(PageStatus.INACTIVE.getValue())
                                .pageAccessToken(
                                        tokenService.getLongLivedToken(clientId, clientSecret, pageAccessToken))
                                // .expiresAt(tokenService.debugAccessToken(accessToken))
                                .build();
                        pageEntities.add(pageEntity);
                    }
                    senderRepository.saveAll(senders);
                    pageRepository.saveAll(pageEntities);
                } else {
                    logger.warn("getUserPages failed - pagesInfo.data is null, userId={} ", userFacebook.getUserId());
                    throw new CustomException(404, "Data pages not found");
                }
            } else {
                logger.warn("getUserPages failed - pagesInfo null, userId={} ", userFacebook.getUserId());
                throw new CustomException(404, "Failed to retrieve user pages");
            }
        } catch (HttpClientErrorException e) {
            throw new CustomException(e.getStatusCode().value(), e.getMessage() + ": " + e.getCause());
        } catch (DataAccessException e) {
            throw new CustomException(500,
                    "Database error while saving pages: " + e.getMessage() + ": " + e.getCause());
        }
    }

    @PostMapping("pages/sync-all")
    public ResponseEntity<String> syncAllPage(@RequestBody PageMesReq pageMesReq) {
        List<String> pageIds = pageMesReq.getPageIds();
        // 1. Đánh dấu tất cả page là "đang đồng bộ"
        pageService.updatePageStatus(pageIds, PageStatus.SYNCING.getValue());

        // 2. Lấy userId từ ThreadContext trước khi publish event
        String userId = ThreadContext.get(Constant.USER_ID_KEY);
        Long userAppId = Long.parseLong(userId);
        
        eventPublisher.publishEvent(new PageSyncEvent(this, pageIds, userId, userAppId));

        // 3. Trả về message đơn giản cho client
        return ResponseEntity.ok("Success");
    }

    // Cho phép test trực tiếp logic đồng bộ 1 page
    public void syncSinglePage(String pageId) {
        try {
            PageEntity pageEntity = pageRepository.findByPageId(pageId)
                    .orElseThrow(() -> {
                        logger.warn("syncSinglePage failed - PageEntity not found: pageId={}", pageId);
                        return new CustomException(404, "Không tìm thấy PageEntity với pageId: " + pageId);
                    });
            String pageAccessToken = pageEntity.getPageAccessToken();
            postService.syncPost(pageId, pageAccessToken);
            videoService.syncVideo(pageId, pageAccessToken);
            pageService.updatePageStatus(List.of(pageId), PageStatus.ACTIVE.getValue());
        } catch (Exception e) {
            pageService.updatePageStatus(List.of(pageId), PageStatus.FAILED.getValue());
        }
    }

    // @PostMapping("pages/async-all")
    // public Mono<ResponseEntity<String>> syncAllPageAsync(@RequestBody PageMesReq
    // pageMesReq) {
    // List<String> pageIds = pageMesReq.getPageIds();

    // List<Mono<Void>> syncMonos = pageIds.stream().map(pageId -> {
    // return Mono.fromCallable(() -> pageRepository.findByPageId(pageId)
    // .orElseThrow(() -> new CustomException(404, "Không tìm thấy PageEntity với
    // pageId: " + pageId)))
    // .flatMap(pageEntity -> {
    // String accessToken = pageEntity.getPageAccessToken();
    // Mono<Void> videoMono = videoService.syncVideo(pageId, accessToken);
    // return Mono.when(videoMono);
    // });
    // }).collect(Collectors.toList());

    // return Mono.when(syncMonos)
    // .thenReturn(ResponseEntity.ok("success"))
    // .doOnError(error -> {
    // // Xử lý lỗi toàn cục nếu cần
    // });
    // }

    public void getPageMessagesAsync(String pageId, String accessToken) {
        String nextPageUrl = null;

        do {
            // Tạo URL yêu cầu tin nhắn từ Facebook hoặc tiếp tục từ trang tiếp theo nếu có
            String messagesUrl;
            if (nextPageUrl == null) {
                messagesUrl = UriComponentsBuilder.fromHttpUrl(conversationUrl)
                        .queryParam("access_token", accessToken)
                        .queryParam("fields", "senders")
                        .buildAndExpand(pageId)
                        .toUriString();
            } else {
                messagesUrl = URLDecoder.decode(nextPageUrl);
            }

            // Gửi yêu cầu GET và nhận phản hồi
            ResponseEntity<Map> response = restTemplate.getForEntity(messagesUrl, Map.class);
            Map<String, Object> messagesInfo = response.getBody();

            if (messagesInfo != null) {
                List<Conversation> conversations = new ArrayList<>();
                List<Sender> customers = new ArrayList<>();
                Object data = messagesInfo.get("data");
                if (data instanceof List) {
                    List<Map<String, Object>> conversationList = (List<Map<String, Object>>) data;
                    // Tạo một List chứa các CompletableFuture để đợi tất cả các task async hoàn
                    // thành
                    List<CompletableFuture<Void>> futures = new ArrayList<>();
                    for (Map<String, Object> conversation : conversationList) {
                        String conversationId = (String) conversation.get("id");
                        Map<String, Object> senders = (Map<String, Object>) conversation.get("senders");
                        if (senders != null) {
                            Object dataSenders = senders.get("data");
                            if (dataSenders instanceof List) {
                                List<Map<String, Object>> sendersList = (List<Map<String, Object>>) dataSenders;
                                String id = (String) sendersList.get(0).get("id");
                                String name = (String) sendersList.get(0).get("name");
                                // String senderInfoUrl = UriComponentsBuilder.fromHttpUrl(BASE_URL+"{id}")
                                // .queryParam("access_token", accessToken)
                                // .buildAndExpand(id)
                                // .toUriString();
                                // // Sử dụng RestTemplate để gửi yêu cầu GET và nhận phản hồi
                                // ResponseEntity<Map> responseSender = restTemplate.getForEntity(senderInfoUrl,
                                // Map.class);
                                // String profile_pic = (String) responseSender.getBody().get("profile_pic");
                                String profile_pic = "https://scontent.fhan20-1.fna.fbcdn.net/v/t1.30497-1/84628273_176159830277856_972693363922829312_n.jpg?stp=dst-jpg_p720x720&_nc_cat=1&ccb=1-7&_nc_sid=7565cd&_nc_ohc=YGV-9-9i8m4Q7kNvgGk5DEy&_nc_zt=24&_nc_ht=scontent.fhan20-1.fna&edm=AP4hL3IEAAAA&_nc_gid=A33jOSTUJlLAfUX-pCn363_&oh=00_AYBq6rbMC8NBfPW1uy2sB1S0lQU5q4NlAGTiGT-BcbqF_A&oe=67427E99\",\n"
                                        +
                                        "  \"id\": \"8251209854924742";
                                customers.add(new Sender(id, name, profile_pic));
                                conversations.add(new Conversation(conversationId, pageId, id));
                            } else {
                                throw new CustomException(404, "Data Sender is not List");
                            }
                        } else {
                            throw new CustomException(404, "No senders found in the response.");
                        }
                        // Tạo CompletableFuture để chạy bất đồng bộ
                        CompletableFuture<Void> future = CompletableFuture
                                .runAsync(() -> getConversationDetailsAsync(conversationId, accessToken));

                        // Thêm CompletableFuture vào danh sách
                        futures.add(future);
                    }
                    // Đợi cho tất cả các CompletableFuture hoàn thành
                    CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
                    allOf.join(); // Đợi tất cả các task hoàn thành trước khi tiếp tục

                    conversationRepository.saveAll(conversations);
                    senderRepository.saveAll(customers);
                    // Đợi tất cả các cuộc hội thoại được xử lý
                    // Kiểm tra xem có trang tiếp theo không
                    Map<String, Object> paging = (Map<String, Object>) messagesInfo.get("paging");
                    nextPageUrl = paging != null ? (String) paging.get("next") : null;
                } else {
                    throw new CustomException(404, "Failed to retrieve conversation details");
                }

            } else {
                throw new CustomException(404, "Failed to retrieve conversation data");
            }
        } while (nextPageUrl != null); // Lặp lại nếu còn trang tiếp theo

    }

    public void getConversationDetailsAsync(String conversationId, String accessToken) {
        String nextPageUrl = null;
        do {
            String conversationUrl;
            ResponseEntity<Map> response;
            try {
                if (nextPageUrl == null) {
                    String fields = "messages{created_time,from,message,attachments{image_data,file_url,video_data},sticker,shares}";
                    conversationUrl = graphApiBaseUrl + conversationId + "?fields={fields}&access_token="
                            + accessToken;

                    response = restTemplate.getForEntity(conversationUrl, Map.class, fields);
                } else {
                    URI uri = UriComponentsBuilder.fromHttpUrl(nextPageUrl)
                            .encode()
                            .build(true)
                            .toUri();

                    response = webClient.get()
                            .uri(uri)
                            .retrieve()
                            .toEntity(new ParameterizedTypeReference<Map>() {
                            })
                            .block();
                }
            } catch (HttpClientErrorException e) {
                logger.error("HTTP error while fetching conversation details (conversationId: {}, nextPageUrl: {}): {}",
                        conversationId, nextPageUrl, e.getMessage(), e);
                throw new CustomException(400,
                        "HTTP error occurred while fetching conversation details: " + e.getMessage());
            } catch (ResourceAccessException e) {
                logger.error(
                        "Resource access error while fetching conversation details (conversationId: {}, nextPageUrl: {}): {}",
                        conversationId, nextPageUrl, e.getMessage(), e);
                throw new CustomException(500,
                        "Resource access error occurred while fetching conversation details: " + e.getMessage());
            } catch (Exception e) {
                logger.error(
                        "Unexpected error while fetching conversation details (conversationId: {}, nextPageUrl: {}): {}",
                        conversationId, nextPageUrl, e.getMessage(), e);
                throw new CustomException(500,
                        "Unexpected error occurred while fetching conversation details: " + e.getMessage());
            }

            // Sử dụng RestTemplate để gửi yêu cầu GET và nhận phản hồi
            Map<String, Object> conversationInfo = response.getBody();
            if (conversationInfo != null) {
                // Lấy danh sách tin nhắn trong cuộc trò chuyện
                Map<String, Object> messages = (Map<String, Object>) conversationInfo.get("messages");
                if (messages != null) {
                    List<Map<String, Object>> data = (List<Map<String, Object>>) messages.get("data");
                    if (data != null && !data.isEmpty()) {
                        List<Message> messageList = new ArrayList<>();
                        // get data
                        for (Map<String, Object> dataItem : data) {
                            Message message = new Message();
                            message.setConversationId(conversationId);
                            String id = (String) dataItem.get("id");
                            message.setMessageId(id);
                            String createdTimeString = (String) dataItem.get("created_time");
                            if (!StringUtils.hasText(createdTimeString)) {
                                throw new CustomException(404, "CreatedTime message not found in the response face.");
                            }
                            message.setCreatedTime(Constant.convertStringToDate(createdTimeString));
                            Map<String, Object> from = (Map<String, Object>) dataItem.get("from");
                            String senderId = (String) from.get("id");
                            message.setSenderId(senderId);
                            setMessageContent(message, dataItem, senderId);
                            messageList.add(message);
                        }
                        messageRepository.saveAll(messageList);
                    }
                    // Kiểm tra xem có trang tiếp theo không
                    Map<String, Object> paging = (Map<String, Object>) messages.get("paging");
                    nextPageUrl = paging != null ? (String) paging.get("next") : null;
                } else {
                    List<Map<String, Object>> data = (List<Map<String, Object>>) conversationInfo.get("data");
                    if (data != null && !data.isEmpty()) {
                        List<Message> messageList = new ArrayList<>();
                        // get data
                        for (Map<String, Object> dataItem : data) {
                            Message message = new Message();
                            message.setConversationId(conversationId);
                            String id = (String) dataItem.get("id");
                            message.setMessageId(id);
                            String createdTimeString = (String) dataItem.get("created_time");
                            if (!StringUtils.hasText(createdTimeString)) {
                                throw new CustomException(404, "CreatedTime message not found in the response face.");
                            }
                            message.setCreatedTime(Constant.convertStringToDate(createdTimeString));
                            Map<String, Object> from = (Map<String, Object>) dataItem.get("from");
                            String senderId = (String) from.get("id");
                            message.setSenderId(senderId);
                            setMessageContent(message, dataItem, senderId);
                            messageList.add(message);
                        }
                        messageRepository.saveAll(messageList);
                        Map<String, Object> paging = (Map<String, Object>) conversationInfo.get("paging");
                        nextPageUrl = paging != null ? (String) paging.get("next") : null;
                    }
                }
            } else {
                System.out.println("Failed to retrieve conversation details");
                nextPageUrl = null;
            }
        } while (nextPageUrl != null); // Lặp lại nếu có trang tiếp theo

    }

    private void requestPageSubscription(String pageId, String pageAccessToken) {
        String subscriptionUrl = graphApiBaseUrl + pageId + "/subscribed_apps";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Tạo body yêu cầu
        String requestBody = "subscribed_fields=feed,messages,live_videos&access_token=" + pageAccessToken;

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.exchange(subscriptionUrl, HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            System.out.println("Error while subscribing to page events: " + e.getMessage());
        }
    }

    private void setMessageContent(Message message, Map<String, Object> dataItem, String senderId) {
        if (!StringUtils.isEmpty(dataItem.get(Constant.MESSAGE))) {
            message.setContent(dataItem.get(Constant.MESSAGE).toString());
            message.setType("TEXT");
            List<String> phoneNumbers = Constant.extractPhoneNumbers(dataItem.get(Constant.MESSAGE).toString());
            if (!phoneNumbers.isEmpty()) {
                List<Phone> phones = new ArrayList<>();
                for (String phoneNumber : phoneNumbers) {
                    phones.add(Phone.builder().phoneNumber(phoneNumber).senderId(senderId).build());
                }
                phoneRepository.saveAll(phones);
            }
            return;
        }
        if (!StringUtils.isEmpty(dataItem.get("attachments"))) {
            message.setContent(dataItem.get("attachments").toString());
            message.setType("ATTACHMENT");
            return;
        }
        if (!StringUtils.isEmpty(dataItem.get("sticker"))) {
            message.setContent(dataItem.get("sticker").toString());
            message.setType("STICKER");
            return;
        }
        if (!StringUtils.isEmpty(dataItem.get("shares"))) {
            message.setContent(dataItem.get("shares").toString());
            message.setType("SHARE");
        }
    }

    // Business endpoints - Lấy danh sách cuộc trò chuyện
    @GetMapping("/conversation/{pageId}")
    public ResponseEntity<Page<GetConversationResponseDTO>> getConversations(
            @PathVariable String pageId, // Lấy pageId từ PathVariable
            @RequestParam(defaultValue = "0") int page , @RequestParam(defaultValue = "20") int size// Tham số page mặc định là 0 nếu không truyền vào
           ) {

        // Truyền vào service với pageId, page và size (size mặc định là 20)
        Page<GetConversationResponseDTO> conversations = conversationService.getConversations(pageId, page, size);

        // Trả về kết quả với HTTP status 200 OK
        return ResponseEntity.ok(conversations);
    }

}
