package org.dexteradei.whitegold.provider.kite;

import java.io.IOException;

import org.dexteradei.whitegold.application.ApiCallbackHandler;
import org.dexteradei.whitegold.application.ProviderId;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@ProviderId("kite")
@Service
public class KiteConnectCallbackHandler implements ApiCallbackHandler{

    private final String API_KEY;
    private final String API_SECRET;

    private final Sinks.One<KiteConnect> kiteConnectSink;
    
    public KiteConnectCallbackHandler(
        @Value("${secrets.kiteconnect.api-key}") String API_KEY,
        @Value("${secrets.kiteconnect.api-secret}") String API_SECRET
    ) {
        this.API_KEY = API_KEY;
        this.API_SECRET = API_SECRET;

        this.kiteConnectSink = Sinks.one();
    }

    @Override
    public Mono<Void> handleCallback(ServerHttpRequest request, ServerHttpResponse response) {
        String requestToken = request.getQueryParams().getFirst("request_token");

        KiteConnect kiteConnect = new KiteConnect(this.API_KEY);
        try {
            User user = kiteConnect.generateSession(requestToken, this.API_SECRET);
            kiteConnect.setAccessToken(user.accessToken);
            kiteConnect.setPublicToken(user.publicToken);

            this.kiteConnectSink.tryEmitValue(kiteConnect);

        } catch (JSONException | IOException | KiteException e) {
            this.kiteConnectSink.tryEmitError(e);
        }
        
        // System.out.println("🎊🎈🌟⭐💫\n🪩🥳🥳🥳🎉");
        // System.out.println("GOT REQUEST TOKEN: " + requestToken);

        return Mono.empty();
    }

    public Mono<KiteConnect> getSession() {
        return this.kiteConnectSink.asMono();
    }
}
