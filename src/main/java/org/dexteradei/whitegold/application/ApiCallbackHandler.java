package org.dexteradei.whitegold.application;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import reactor.core.publisher.Mono;

public interface ApiCallbackHandler {
    public Mono<Void> handleCallback(ServerHttpRequest request, ServerHttpResponse response);    
} 
