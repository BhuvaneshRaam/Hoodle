package com.example.hoodle.Proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class OrbitOrderProxyController {

    @Bean
    public RestTemplate restTemplate() { return new RestTemplate(); }

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/proxy/orbitorder/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> proxyRequests(HttpServletRequest request,
                                                @CookieValue(value = "jwt", required = false) String jwtToken,
                                                @RequestBody(required = false) String body) {

        // 1. Swap the URL from Hoodle to OrbitOrder
        String orbitPath = request.getRequestURI().replace("/proxy/orbitorder", "/orbitorder");
        String targetUrl = "https://orbitorder.onrender.com" + orbitPath;
        if (request.getQueryString() != null) targetUrl += "?" + request.getQueryString();

        // 2. Attach the HttpOnly cookie as a Header for OrbitOrder
        HttpHeaders headers = new HttpHeaders();
        if (jwtToken != null) headers.set("Authorization", "Bearer " + jwtToken);
        headers.set("Content-Type", request.getContentType() != null ? request.getContentType() : "application/json");

        // 3. Forward the request
        return restTemplate.exchange(targetUrl, HttpMethod.valueOf(request.getMethod()), new HttpEntity<>(body, headers), String.class);
    }
}
