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

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/proxy/orbitorder/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> proxyRequests(HttpServletRequest request,
                                                @CookieValue(value = "jwt", required = false) String jwtToken,
                                                @RequestBody(required = false) String body) {

        String requestURI = request.getRequestURI();

        // 2. Safely extract ONLY the API path (e.g., "/api/v1/po/all")
        String apiPath = requestURI.substring(requestURI.indexOf("/proxy/orbitorder") + "/proxy/orbitorder".length());

        // 3. Attach it to Orbit Order's exact live URL and context path
        String targetUrl = "https://orbitorder.onrender.com/orbitorder" + apiPath;

        if (request.getQueryString() != null) {
            targetUrl += "?" + request.getQueryString();
        }
        // 2. Attach the HttpOnly cookie as a Header for OrbitOrder
        HttpHeaders headers = new HttpHeaders();
        if (jwtToken != null) headers.set("Authorization", "Bearer " + jwtToken);
        headers.set("Content-Type", request.getContentType() != null ? request.getContentType() : "application/json");

        // 3. Forward the request
        return restTemplate.exchange(targetUrl, HttpMethod.valueOf(request.getMethod()), new HttpEntity<>(body, headers), String.class);
    }
}
