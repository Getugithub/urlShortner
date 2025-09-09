
2025-09-09 11:44:58.767 | ethiopian-airlines-orders | bTUx4iwtRgWJGGV1RH6Xvg | INFO | 0 | 0 | PayEthiopianAirlinesOrder | confirmOrder | Finished constructing Confirm Order request, invoking API next | PROCESSING | 657.98 |  |  | https://flyprod.ethiopianairlines.com/api/V1.0/MPESAPayments/ConfirmOrder |  |  | <{ requestType=C2BConfirmation, transactionType=Pay Utility, transID=TI9633FR9Y, transTime=20250909114458, transAmount=26156, businessShortCode=586828, billRefNumber=AUJZXP, invoiceNumber=, orgAccountBalance=26156, thirdPartyTransID=, msisdn=251777550069, firstName=Dennis, middleName=, lastName=Wanyonyi},[Content-Type:"application/json", Accept:"application/json", Host:"flyprod.ethiopianairlines.com", Authorization:"Basic bXBlc2Fwcm9kQGV0aGlvcGlhbmFpcmxpbmVzLmNvbToqKio="]> | null
2025-09-09 11:45:05.042 | ethiopian-airlines-orders | bTUx4iwtRgWJGGV1RH6Xvg | INFO | 0 | 0 | PayEthiopianAirlinesOrder | confirmOrder | Received response from Confirm Order API | PROCESSING | 6933.04 |  |  | https://flyprod.ethiopianairlines.com/api/V1.0/MPESAPayments/ConfirmOrder | 6 | Successfully Paid | <200,com.mpesa.ethiopianairlinesorders.dto.OrderApiResponse@67b5cde,[Content-Type:"application/json; charset=utf-8", Date:"Tue, 09 Sep 2025 08:45:04 GMT", Cache-Control:"no-store", Pragma:"no-cache", Set-Cookie:"ARRAffinity=04e4ae4662bb6d686af058e5e5dc2f336bdaef569f9a9b7df71508476ed2041f;Path=/;HttpOnly;Secure;Domain=flyprod.ethiopianairlines.com", "ARRAffinitySameSite=04e4ae4662bb6d686af058e5e5dc2f336bdaef569f9a9b7df71508476ed2041f;Path=/;HttpOnly;SameSite=None;Secure;Domain=flyprod.ethiopianairlines.com", Transfer-Encoding:"chunked", Vary:"Accept-Encoding", Strict-Transport-Security:"max-age=31536000; includeSubDomains", Request-Context:"appId=cid-v1:78af2322-0009-4af6-bce6-f54648136220", X-XSS-Protection:"1", Referrer-Policy:"strict-origin-when-cross-origin", Content-Security-Policy:"default-src 'self'; img-src 'self' myblobacc.blob.core.windows.net; font-src 'self'; style-src 'self'; script-src 'self' 'nonce-KIBdfgEKjb34ueiw567bfkshbvfi4KhtIUE3IWF'  'nonce-rewgljnOIBU3iu2btli4tbllwwe'; frame-src 'self';connect-src 'self';", Expect-CT:"max-age=0;", X-Frame-Options:"DENY", X-Content-Type-Options:"nosniff", X-Powered-By:"ASP.NET"]> | null
 2025-09-09 11:45:05.057 | ethiopian-airlines-orders | bTUx4iwtRgWJGGV1RH6Xvg | INFO | 0 | 0 | PayEthiopianAirlinesOrder | consumerpayOrder | Response for Paying for Ethiopian Airlines Order | FINISHED | 6948.48 |  |  |  | 0 | The service request is processed successfully. |  {  RequestRefID=bTUx4iwtRgWJGGV1RH6Xvg, ResponseCode=0, ResponseDesc=The service request is processed successfully., ResponseParameters=[{ Key=BOCompletedTime, Value=20250909114458}, { Key=Charge, Value=5.00}, { Key=Amount, Value=26156.00}, { Key=TransactionID, Value=TI9633FR9Y}, { Key=Currency, Value=ETB}, { Key=MMBalance, Value=88300.63}, { Key=TransactionStatus, Value=Completed}, { Key=ETStatusCode, Value=6}, { Key=ETStatus, Value=1}, { Key=ETMessage, Value=Successfully Paid}, { Key=ETErrorCode, Value=null}]} | null
 



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
