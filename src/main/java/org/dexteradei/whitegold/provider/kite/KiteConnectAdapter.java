package org.dexteradei.whitegold.provider.kite;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

import org.dexteradei.whitegold.service.DepthMetricsProducerService;
import org.dexteradei.whitegold.service.OpenInterestMetricsProducerService;
import org.dexteradei.whitegold.service.PriceMetricsProducerService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PriceMetricsProducerService priceProducer;
    @Autowired
    private OpenInterestMetricsProducerService oiProducer;
    @Autowired
    private DepthMetricsProducerService depthProducer;

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
            }
        });

        tickerProvider.setOnOrderUpdateListener(new OnOrderUpdate() {
            @Override
            public void onOrderUpdate(Order order) {
                System.out.println("order update "+order.orderId);
            }
        });

        tickerProvider.setOnErrorListener(new OnError() {
            @Override
            public void onError(Exception exception) {
                
            }

            @Override
            public void onError(KiteException kiteException) {
                
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
            }
        });

        tickerProvider.setOnTickerArrivalListener(new OnTicks() {
            @Override
            public void onTicks(ArrayList<Tick> ticks) {
                if (ticks.isEmpty()) return;
                Tick t = ticks.get(0);

                String symbol = String.valueOf(t.getInstrumentToken());
                Instant ts = t.getTickTimestamp().toInstant();

                priceProducer.produce(symbol, ts, t.getLastTradedPrice(), t.getChange(),
                                    t.getHighPrice(), t.getLowPrice());

                oiProducer.produce(symbol, ts, t.getOi(), t.getOpenInterestDayHigh(),
                                t.getOpenInterestDayLow(),
                                100.0 * (t.getOi() - t.getOpenInterestDayLow()) / (double) t.getOpenInterestDayLow());

                double bestBid = t.getMarketDepth().get("buy").get(0).getPrice();
                long bestBidQty = t.getMarketDepth().get("buy").get(0).getQuantity();
                double bestAsk = t.getMarketDepth().get("sell").get(0).getPrice();
                long bestAskQty = t.getMarketDepth().get("sell").get(0).getQuantity();

                depthProducer.produce(symbol, ts, bestBid, bestBidQty, bestAsk, bestAskQty);
            }
        });

        tickerProvider.setTryReconnection(true);
        tickerProvider.setMaximumRetries(10);
        tickerProvider.setMaximumRetryInterval(30);
        tickerProvider.connect();

        boolean isConnected = tickerProvider.isConnectionOpen();
        System.out.println(isConnected);

        tickerProvider.setMode(tokens, KiteTicker.modeFull);
    }

    public void streamTicks(ArrayList<Long> tokens) throws IOException, WebSocketException, KiteException {
        this.subscribedTokens = tokens;
        this.tickerUsage(this.kiteConnectSessionObject, tokens);
    }

    @PreDestroy
    public void cleanConnection() {
        tickerProvider.unsubscribe(this.subscribedTokens);

        tickerProvider.disconnect();

        System.out.println("Disconnected from Kite API");
    }
}
