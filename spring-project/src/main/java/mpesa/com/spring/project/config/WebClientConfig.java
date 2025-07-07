package mpesa.com.spring.project.config;


import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 5s connect timeout
                .responseTimeout(Duration.ofSeconds(10)) // 10s response timeout
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS)));

        return builder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}




// package mpesa.com.spring.project.filter;

// import mpesa.com.spring.project.service.UrlShortenerService;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;
// import javax.servlet.*;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;

// @Component
// public class UrlRedirectFilter implements Filter {

//     private final UrlShortenerService urlShortenerService;
//     private final String domainPattern;
//     private final int shortCodeLength;

//     public UrlRedirectFilter(UrlShortenerService urlShortenerService,
//                            @Value("${url.shortener.domain.pattern}") String domainPattern,
//                            @Value("${url.shortener.code.length}") int shortCodeLength) {
//         this.urlShortenerService = urlShortenerService;
//         this.domainPattern = domainPattern;
//         this.shortCodeLength = shortCodeLength;
//     }

//     @Override
//     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
//             throws IOException, ServletException {
        
//         HttpServletRequest httpRequest = (HttpServletRequest) request;
//         String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        
//         if (isShortUrlPath(path)) {
//             String shortCode = path.substring(1);
//             String longUrl = urlShortenerService.expand(shortCode);
            
//             if (longUrl != null) {
//                 ((HttpServletResponse) response).sendRedirect(longUrl);
//                 return;
//             } else {
//                 ((HttpServletResponse) response).sendError(HttpServletResponse.SC_NOT_FOUND);
//                 return;
//             }
//         }
        
//         chain.doFilter(request, response);
//     }

//     private boolean isShortUrlPath(String path) {
//         return path.matches(domainPattern);
//     }

//     @Override
//     public void init(FilterConfig filterConfig) throws ServletException {
//         log.info("Initializing UrlRedirectFilter with:");
//         log.info("Domain pattern: {}", domainPattern);
//         log.info("Short code length: {}", shortCodeLength);
//     }

//     @Override
//     public void destroy() {
//         // Cleanup if needed
//     }
// }

// # URL Shortener Configuration
// url.shortener.domain.pattern=/[a-zA-Z0-9]{8}
// url.shortener.code.length=8
// url.shortener.base-domain=http://sho.rt
