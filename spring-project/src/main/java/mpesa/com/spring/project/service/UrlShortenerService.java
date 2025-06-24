package mpesa.com.spring.project.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import mpesa.com.spring.project.dto.MessageRequestDto;
import mpesa.com.spring.project.dto.ReceiverDto;
import mpesa.com.spring.project.model.UrlEntity;
import mpesa.com.spring.project.repository.UrlRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

import java.security.SecureRandom; // For secure random number generation
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlShortenerService {


    private static final String DOMAIN = "http://sho.rt/";


    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";


    private static final int SHORT_CODE_LENGTH = 8;


    private static final String BASE_URL = "";

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private WebClient webClient;





    private final SecureRandom random = new SecureRandom();

    // Values from application.properties or application.yml
    @Value("${notification.url}")
    private String notificationUrl;

    @Value("${notification.sender}")
    private String notificationSender;

    @Value("${notification.content-approved}")
    private String notificationContentApproved;

    // Returns a shortened version of the given long URL
    @Transactional
    public String shorten(String longUrl) {
        return urlRepository.findByLongUrl(longUrl)
                .map(UrlEntity::getShortCode)
                .map(shortCode -> DOMAIN + shortCode)
                .orElseGet(() -> createShortUrl(longUrl));
    }

    // Retrieves the original long URL from a shortened URL
    @Transactional(readOnly = true)
    public String expand(String shortUrl) {
        String shortCode = shortUrl.replace(DOMAIN, "");
        return urlRepository.findByShortCode(shortCode)
                .map(UrlEntity::getLongUrl)
                .orElse(null);
    }

    // Asynchronously sends an SMS or notification message to the user
    @Async("taskExecutor")
    public void sendNotification(String msisdn, String msg) {
        // Build the request body
        MessageRequestDto request = MessageRequestDto.builder()
                .requestRefid(UUID.randomUUID().toString())
                .content(msg)
                .sender(notificationSender)
                .receivers(List.of(ReceiverDto.builder().phoneNumber(msisdn).build()))
                .build();

        // Send POST request to notification service
        webClient.post()
                .uri(notificationUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class) // Expect response body as String
                .doOnNext(response -> log.info("Notification Response: {}", response))
                .doOnError(error -> log.error("Notification Error: {}", error.getMessage()))
                .subscribe();
    }

    // Builds the long URL, shortens it, and sends a notification
    public void processNotification(String msisdn) {
        String longUrl = String.format(BASE_URL, msisdn);
        String shortenedUrl = shorten(longUrl);
        String msg = notificationContentApproved + shortenedUrl;
        sendNotification(msisdn, msg);
    }

    // Creates a new short URL for the provided long URL
    private String createShortUrl(String longUrl) {
        String shortCode = generateShortCode(); // Generate a random short code

        // Keep generating a new one if it already exists in DB
        while (urlRepository.findByShortCode(shortCode).isPresent()) {
            shortCode = generateShortCode();
        }

        // Save the mapping to the database
        UrlEntity entity = new UrlEntity();
        entity.setLongUrl(longUrl);
        entity.setShortCode(shortCode);
        urlRepository.save(entity);

        return DOMAIN + shortCode;
    }

    // Generates a random 8-character short code using BASE62
    private String generateShortCode() {
        StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(BASE62.charAt(random.nextInt(BASE62.length()))); // Pick a random character
        }
        return sb.toString();
    }
}
