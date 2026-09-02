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

    @RequestMapping(value = "/proxy/orbitorder/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> proxyRequests(HttpServletRequest request,
                                                @CookieValue(value = "jwt", required = false) String jwtToken,
                                                @RequestBody(required = false) String body) {

        String requestURI = request.getRequestURI();
        String apiPath = requestURI.substring(requestURI.indexOf("/proxy/orbitorder") + "/proxy/orbitorder".length());

        // IMPORTANT: Verify this matches Orbit Order's exact live URL and context path!
        String targetUrl = "https://orbitorder.onrender.com/orbitorder" + apiPath;
        if (request.getQueryString() != null) {
            targetUrl += "?" + request.getQueryString();
        }

        HttpHeaders headers = new HttpHeaders();
        if (jwtToken != null) headers.set("Authorization", "Bearer " + jwtToken);
        headers.set("Content-Type", request.getContentType() != null ? request.getContentType() : "application/json");

        try {
            // Try to forward the request
            return restTemplate.exchange(targetUrl, HttpMethod.valueOf(request.getMethod()), new HttpEntity<>(body, headers), String.class);
        } catch (HttpStatusCodeException e) {
            // If Orbit Order returns 401, 403, 404, etc., pass it safely back to Angular!
            return ResponseEntity.status(e.getStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString());
        } catch (Exception e) {
            // If Orbit Order is asleep or completely down
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Failed to reach Orbit Order: " + e.getMessage());
        }
    }
}
