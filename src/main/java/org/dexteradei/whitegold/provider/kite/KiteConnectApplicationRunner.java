package org.dexteradei.whitegold.provider.kite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.neovisionaries.ws.client.WebSocketException;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;

@Component
public class KiteConnectApplicationRunner implements ApplicationRunner {
    private final KiteConnectClient client;
    private final KiteConnectCallbackHandler callbackHandler;
    private final KiteConnectAdapter adapter;

    public KiteConnectApplicationRunner(KiteConnectClient client, KiteConnectCallbackHandler callbackHandler, KiteConnectAdapter adapter) {
        this.client = client;
        this.callbackHandler = callbackHandler;
        this.adapter = adapter;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        client.connect().then(
            callbackHandler.getSession()
                .doOnNext(kiteConnect -> {
                    System.out.println("🏆🏆🏆🏆🏆");
                    System.out.println("ACCESS TOKEN: " + kiteConnect.getAccessToken());
                    System.out.println("PUBLIC TOKEN: " + kiteConnect.getPublicToken());
                    adapter.setKiteConnectSessionObject(kiteConnect);
                    try {
                        adapter.streamTicks(new ArrayList<>(List.of((341249l))));
                    } catch (IOException | WebSocketException | KiteException e) {
                        e.printStackTrace();
                    }
                })
        ).subscribe();
    }
    
}
