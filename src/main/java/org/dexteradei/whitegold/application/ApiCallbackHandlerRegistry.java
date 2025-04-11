package org.dexteradei.whitegold.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * {@code ApiCallbackHandlerRegistry} is a central registry that maps
 * {@link ApiCallbackHandler} implementations in provider package for each market data provider.
 * <p>
 * This class implements the Strategy Pattern by allowing the dispatch of callback
 * handling logic based on provider ID.
 * <p>
 * Each {@link ApiCallbackHandler} must annotated with {@link ProviderId} to be registered.
 * The registry scans for all {@link ApiCallbackHandler} implementations and handles them.
 * 
 * <pre>
 * Example usage:
 *  ApiCallbackHanlder handler = registry.getHandler("kite");
 *  handler.handleCallback(request);
 * </pre>
 * 
 * @author KushalSuvan
 */


@Component
public class ApiCallbackHandlerRegistry {
    private final Map<String, ApiCallbackHandler> handlerMap = new HashMap<>();

    public ApiCallbackHandlerRegistry(List<ApiCallbackHandler> handlers) {
        for (var handler: handlers) {
            ProviderId annotation = handler.getClass().getAnnotation(ProviderId.class);
            if (annotation == null) {
                throw new IllegalStateException("Missing @ProviderId on " + handler.getClass().getName());
            }
            handlerMap.put(annotation.value(), handler);
        }
    }

    public ApiCallbackHandler getHandler(String providerId) {
        ApiCallbackHandler handler = handlerMap.get(providerId);
        if (handler == null) {
            throw new IllegalArgumentException("No handler registered for provider " + providerId);
        }
        return handler;
    }
}
