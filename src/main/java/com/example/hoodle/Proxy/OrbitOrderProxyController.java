package com.example.hoodle.Proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class OrbitOrderProxyController {

    @Autowired
    private RestTemplate restTemplate;

    // 1. SANITY CHECK ENDPOINT - Proves the controller is reachable
    @GetMapping("/proxy/ping")
    public String pingTest() {
        System.out.println(">>> PROXY PING REACHED <<<");
        return "Proxy is officially awake and reachable!";
    }

    // 2. PURE GET PROXY - Stripped down to avoid Tomcat body errors
    @GetMapping("/proxy/orbitorder/**")
    public ResponseEntity<String> proxyGetRequests(HttpServletRequest request,
                                                   @CookieValue(value = "jwt", required = false) String jwtToken) {

        System.out.println(">>> PROXY INITIATED FOR GET <<<");

        String requestURI = request.getRequestURI();
        String apiPath = requestURI.substring(requestURI.indexOf("/proxy/orbitorder") + "/proxy/orbitorder".length());

        String targetUrl = "https://orbitorder.onrender.com/orbitorder" + apiPath;
        if (request.getQueryString() != null) {
            targetUrl += "?" + request.getQueryString();
        }

        System.out.println("Forwarding To: " + targetUrl);

        HttpHeaders headers = new HttpHeaders();
        if (jwtToken != null) {
            headers.set("Authorization", "Bearer " + jwtToken);
        }
        headers.set("Content-Type", "application/json");

        try {
            // Forwarding as a pure GET request with NO body
            ResponseEntity<String> response = restTemplate.exchange(targetUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            System.out.println(">>> RECEIVED SUCCESS FROM ORBIT ORDER: " + response.getStatusCode() + " <<<");
            // 2. CRITICAL FIX: Return ONLY the status code and body (DO NOT pass raw response headers!)
            return ResponseEntity.status(response.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(response.getBody());
        } catch (HttpStatusCodeException e) {
            System.out.println(">>> ORBIT ORDER RETURNED ERROR: " + e.getStatusCode() + " <<<");
            return ResponseEntity.status(e.getStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString());
        } catch (Throwable t) {
            // 'Throwable' catches Exceptions AND fatal system Errors like OutOfMemory!
            System.out.println(">>> FATAL PROXY CRASH: " + t.toString() + " <<<");
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Hoodle crashed while forwarding: " + t.getMessage());
        }
    }
}
