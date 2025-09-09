{

    "refresh_token": "vlD4GNaVWC7ir5p581MvXv167GkRamh2",

    "token_type": "bearer",

    "access_token": "BaB62ZQbYrVdsYVNq3feHGUvdO6Goxmu",

    "expires_in": 7200

}package mpesa.com.spring.project.controller;


import lombok.RequiredArgsConstructor;
import mpesa.com.spring.project.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NotificationController {


    @Autowired
    private UrlShortenerService urlShortenerService;

    @GetMapping("/test")
    public ResponseEntity<Void> test(@RequestParam String msisdn) {
        urlShortenerService.processNotification(msisdn);
        return ResponseEntity.ok().build();
    }



    @PostMapping("/notify")
    public ResponseEntity<String> receiveNotification(@RequestBody Map<String, Object> payload) {
        System.out.println("Received notification: " + payload);
        return ResponseEntity.ok("Notification received");
    }

    @GetMapping("/expand")
    public ResponseEntity<String> expand(@RequestParam String shortUrl) {
        String longUrl = urlShortenerService.expand(shortUrl);
        return longUrl != null ? ResponseEntity.ok(longUrl) : ResponseEntity.notFound().build();
    }
}
