package org.dexteradei.whitegold.controller;

import org.dexteradei.whitegold.application.ApiCallbackHandler;
import org.dexteradei.whitegold.application.ApiCallbackHandlerRegistry;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/oauth/callback")
public class OAuth2CallbackController {
    
    private final ApiCallbackHandlerRegistry registry;

    public OAuth2CallbackController(ApiCallbackHandlerRegistry registry) {
        this.registry = registry;
    }
    
    @GetMapping("/{provider}")
    public Mono<Void> handleRedirection(
        @PathVariable("provider") String provider,
        ServerHttpRequest request,
        ServerHttpResponse response
    ) {

        ApiCallbackHandler handler = registry.getHandler(provider);
        return handler.handleCallback(request, response);
    } 
}
