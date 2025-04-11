package org.dexteradei.service;

import java.net.URI;
import java.net.URISyntaxException;


import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

import jakarta.annotation.PostConstruct;


@Service
public class KiteWebSocketClient {

    String subscriptionJson = """
    {
        "a": "subscribe",
        "v": [408065, 884737]
    }
    """;

    
    @PostConstruct
    public void init() {
        WebSocketClient client = new ReactorNettyWebSocketClient();

        try {
            URI url = new URI("wss://ws.kite.trade?api_key=ip1kn7r1g5qjrbju&access_token=alipm946ru144mo7i9m2mk4r74ubxit3");
        } catch (URISyntaxException e) {
            System.err.print(e);
            // Change to Logback advices later
        } 

    }

}
