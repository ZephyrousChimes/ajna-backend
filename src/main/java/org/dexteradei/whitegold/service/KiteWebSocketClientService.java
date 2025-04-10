package org.dexteradei.whitegold.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.neovisionaries.ws.client.WebSocketException;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.Tick;
import com.zerodhatech.models.User;
import com.zerodhatech.ticker.KiteTicker;
import com.zerodhatech.ticker.OnConnect;
import com.zerodhatech.ticker.OnDisconnect;
import com.zerodhatech.ticker.OnError;
import com.zerodhatech.ticker.OnOrderUpdate;
import com.zerodhatech.ticker.OnTicks;

import jakarta.annotation.PostConstruct;
import lombok.Data;

@Data
@Service
public class KiteWebSocketClientService {
    
    private final String API_KEY;
    private final String USER_ID;
    private final String API_SECRET;

    String subscriptionJson = """
    {
        "a": "subscribe",
        "v": [408065, 884737]
    }
    """;

    public KiteWebSocketClientService(
        @Value("${secrets.kiteconnect.api-key}") String API_KEY,
        @Value("${secrets.kiteconnect.user-id}") String USER_ID,
        @Value("${secrets.kiteconnect.api-secret}") String API_SECRET
    ) {
        this.API_KEY = API_KEY;
        this.USER_ID = USER_ID;
        this.API_SECRET = API_SECRET;
    }

    
    @PostConstruct
    public void init() {
        KiteConnect kiteConnect = new KiteConnect(API_KEY);
        kiteConnect.setUserId(USER_ID);
        String url = kiteConnect.getLoginURL();

        kiteConnect.setSessionExpiryHook(new SessionExpiryHook() {
            @Override
            public void sessionExpired() {
                System.out.println("session expired");
            }
        });

        /* The request token can to be obtained after completion of login process. Check out https://kite.trade/docs/connect/v3/user/#login-flow for more information.
            A request token is valid for only a couple of minutes and can be used only once. An access token is valid for one whole day. Don't call this method for every app run.
            Once an access token is received it should be stored in preferences or database for further usage.
        */
        User user;
        try {
            // user = kiteConnect.generateSession("7rY17Vl46ulh6FDBDcnAUirk8SrTCVMZ", API_SECRET);

            // System.out.println("Access Token: " + user.accessToken);
            // System.out.println("Public Token: " + user.publicToken);
            
            // kiteConnect.setAccessToken(user.accessToken);
            kiteConnect.setAccessToken("3PoiZYBAKnE09m7OGifc6erJoF8aVt24");
            // kiteConnect.setPublicToken(user.publicToken);
            kiteConnect.setPublicToken("QdsdsO0dlyO6bLCqwVMfIKu9ot38EXbI");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        

        System.out.println("🟩🟠🔷🟩🟠🔷\n🔷🟠🟩🔷🟠🟩");
        System.out.println(API_KEY);

        try {
            tickerUsage(kiteConnect, new ArrayList<>(List.of()));
        } catch (IOException | WebSocketException | KiteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // WebSocketClient client = new ReactorNettyWebSocketClient();
        // URI url = null;
        // try {
        //     url = new URI("wss://ws.kite.trade?api_key=ip1kn7r1g5qjrbju&access_token=alipm946ru144mo7i9m2mk4r74ubxit3");
        // } catch (URISyntaxException e) {
        //     System.err.print(e);
        //     // Change to Logback advices later
        // } 

        // client.execute(url, session -> 
        //     session.send(Mono.just(session.textMessage(subscriptionJson)))
        //         .thenMany(session.receive()
        //             .map(WebSocketMessage::getPayloadAsText)
        //             .doOnNext(System.out::println)
        //         ).then()
        // ).subscribe(); 
    }

    public void tickerUsage(KiteConnect kiteConnect, ArrayList<Long> tokens) throws IOException, WebSocketException, KiteException {
        /** To get live price use websocket connection.
         * It is recommended to use only one websocket connection at any point of time and make sure you stop connection, once user goes out of app.
         * custom url points to new endpoint which can be used till complete Kite Connect 3 migration is done. */
        final KiteTicker tickerProvider = new KiteTicker(kiteConnect.getAccessToken(), kiteConnect.getApiKey());

        tickerProvider.setOnConnectedListener(new OnConnect() {
            @Override
            public void onConnected() {
                /** Subscribe ticks for token.
                 * By default, all tokens are subscribed for modeQuote.
                 * */
                tickerProvider.subscribe(tokens);
                tickerProvider.setMode(tokens, KiteTicker.modeFull);
            }
        });

        tickerProvider.setOnDisconnectedListener(new OnDisconnect() {
            @Override
            public void onDisconnected() {
                // your code goes here
                System.out.println("😵 DISCONNECTED");
            }
        });

        /** Set listener to get order updates.*/
        tickerProvider.setOnOrderUpdateListener(new OnOrderUpdate() {
            @Override
            public void onOrderUpdate(Order order) {
                System.out.println("order update "+order.orderId);
            }
        });

        /** Set error listener to listen to errors.*/
        tickerProvider.setOnErrorListener(new OnError() {
            @Override
            public void onError(Exception exception) {
                //handle here.
                tickerProvider.disconnect();
            }

            @Override
            public void onError(KiteException kiteException) {
                //handle here.
                tickerProvider.disconnect();
            }

            @Override
            public void onError(String error) {
                tickerProvider.disconnect();
            }
        });

        tickerProvider.setOnTickerArrivalListener(new OnTicks() {
            @Override
            public void onTicks(ArrayList<Tick> ticks) {
                NumberFormat formatter = new DecimalFormat();
                System.out.println("ticks size "+ticks.size());
                if(ticks.size() > 0) {
                    System.out.println(ticks.get(0).getChange() > 0 ? "🟢" : "🟠");
                    System.out.println("last price "+ticks.get(0).getLastTradedPrice());
                    System.out.println("open interest "+formatter.format(ticks.get(0).getOi()));
                    System.out.println("day high OI "+formatter.format(ticks.get(0).getOpenInterestDayHigh()));
                    System.out.println("day low OI "+formatter.format(ticks.get(0).getOpenInterestDayLow()));
                    System.out.println("change "+formatter.format(ticks.get(0).getChange()));
                    System.out.println("tick timestamp "+ticks.get(0).getTickTimestamp());
                    System.out.println("tick timestamp date "+ticks.get(0).getTickTimestamp());
                    System.out.println("last traded time "+ticks.get(0).getLastTradedTime());
                    System.out.println(ticks.get(0).getMarketDepth().get("buy").size());
                    System.out.println("-----------------------------------");
                }
            }
        });
        // Make sure this is called before calling connect.
        tickerProvider.setTryReconnection(true);
        //maximum retries and should be greater than 0
        tickerProvider.setMaximumRetries(10);
        //set maximum retry interval in seconds
        tickerProvider.setMaximumRetryInterval(30);

        /** connects to com.zerodhatech.com.zerodhatech.ticker server for getting live quotes*/
        tickerProvider.connect();

        /** You can check, if websocket connection is open or not using the following method.*/
        boolean isConnected = tickerProvider.isConnectionOpen();
        System.out.println(isConnected);

        /** set mode is used to set mode in which you need tick for list of tokens.
         * Ticker allows three modes, modeFull, modeQuote, modeLTP.
         * For getting only last traded price, use modeLTP
         * For getting last traded price, last traded quantity, average price, volume traded today, total sell quantity and total buy quantity, open, high, low, close, change, use modeQuote
         * For getting all data with depth, use modeFull*/
        tickerProvider.setMode(tokens, KiteTicker.modeLTP);

        // Unsubscribe for a token.
        tickerProvider.unsubscribe(tokens);

        // // After using com.zerodhatech.com.zerodhatech.ticker, close websocket connection.
        tickerProvider.disconnect();
    }

}
