package org.dexteradei.whitegold.provider.kite;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class KiteConnectApplicationRunner implements ApplicationRunner {
    private final KiteConnectClient client;
    private final KiteConnectCallbackHandler callbackHandler;

    public KiteConnectApplicationRunner(KiteConnectClient client, KiteConnectCallbackHandler callbackHandler) {
        this.client = client;
        this.callbackHandler = callbackHandler;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        client.connect().then(
            callbackHandler.getSession()
                .doOnNext(kiteConnect -> {
                    System.out.println("🏆🏆🏆🏆🏆");
                    System.out.println("ACCESS TOKEN: " + kiteConnect.getAccessToken());
                    System.out.println("PUBLIC TOKEN: " + kiteConnect.getAccessToken());
                })
        ).subscribe();
    }
    
}
