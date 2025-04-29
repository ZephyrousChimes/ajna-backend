package org.dexteradei.whitegold.provider.kite;

import java.io.IOException;
// import java.text.DecimalFormat;
// import java.text.NumberFormat;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.neovisionaries.ws.client.WebSocketException;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.KiteTicker;
import com.zerodhatech.ticker.OnConnect;
import com.zerodhatech.ticker.OnDisconnect;
import com.zerodhatech.ticker.OnError;
import com.zerodhatech.ticker.OnOrderUpdate;
import com.zerodhatech.ticker.OnTicks;

import jakarta.annotation.PreDestroy;
import lombok.Data;

@Data
@Service
public class KiteConnectAdapter {
    private KiteConnect kiteConnectSessionObject;
    private KiteTicker tickerProvider;

    private ArrayList<Long> subscribedTokens;

    public KiteConnectAdapter() {
        kiteConnectSessionObject = null;
        tickerProvider = null;
        subscribedTokens = null;
    }

    public void tickerUsage(KiteConnect kiteConnect, ArrayList<Long> tokens) throws IOException, WebSocketException, KiteException {
        tickerProvider = new KiteTicker(kiteConnect.getAccessToken(), kiteConnect.getApiKey());

        tickerProvider.setOnConnectedListener(new OnConnect() {
            @Override
            public void onConnected() {
                tickerProvider.subscribe(tokens);
                tickerProvider.setMode(tokens, KiteTicker.modeFull);
            }
        });

        tickerProvider.setOnDisconnectedListener(new OnDisconnect() {
            @Override
            public void onDisconnected() {
                // your code goes here
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
            }

            @Override
            public void onError(KiteException kiteException) {
                //handle here.
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
            }
        });

        tickerProvider.setOnTickerArrivalListener(new OnTicks() {
            @Override
            public void onTicks(ArrayList<Tick> ticks) {
                // NumberFormat formatter = new DecimalFormat();
                System.out.println("last price "+ticks);
                // if(ticks.size() > 0) {
                //     System.out.println("last price "+ticks.get(0).getLastTradedPrice());
                //     System.out.println("open interest "+formatter.format(ticks.get(0).getOi()));
                //     System.out.println("day high OI "+formatter.format(ticks.get(0).getOpenInterestDayHigh()));
                //     System.out.println("day low OI "+formatter.format(ticks.get(0).getOpenInterestDayLow()));
                //     System.out.println("change "+formatter.format(ticks.get(0).getChange()));
                //     System.out.println("tick timestamp "+ticks.get(0).getTickTimestamp());
                //     System.out.println("tick timestamp date "+ticks.get(0).getTickTimestamp());
                //     System.out.println("last traded time "+ticks.get(0).getLastTradedTime());
                //     System.out.println(ticks.get(0).getMarketDepth().get("buy").size());
                // }
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

        boolean isConnected = tickerProvider.isConnectionOpen();
        System.out.println(isConnected);

        /** set mode is used to set mode in which you need tick for list of tokens.
         * Ticker allows three modes, modeFull, modeQuote, modeLTP.
         * For getting only last traded price, use modeLTP
         * For getting last traded price, last traded quantity, average price, volume traded today, total sell quantity and total buy quantity, open, high, low, close, change, use modeQuote
         * For getting all data with depth, use modeFull*/
        tickerProvider.setMode(tokens, KiteTicker.modeFull);
    }

    public void streamTicks(ArrayList<Long> tokens) throws IOException, WebSocketException, KiteException {
        this.subscribedTokens = tokens;
        this.tickerUsage(this.kiteConnectSessionObject, tokens);
    }

    @PreDestroy
    public void cleanConnection() {
        // Unsubscribe for a token.
        tickerProvider.unsubscribe(this.subscribedTokens);

        // After using com.zerodhatech.com.zerodhatech.ticker, close websocket connection.
        tickerProvider.disconnect();

        System.out.println("Disconnected from Kite API");
    }
}
