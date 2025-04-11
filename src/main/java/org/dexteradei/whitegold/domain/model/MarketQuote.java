package org.dexteradei.whitegold.domain.model;

import java.util.List;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;


@Data
public class MarketQuote {
    
    @JsonProperty("instrument_token")
    private int instrumentToken;
    private String timestamp;
    private String lastTradeTime;

    private double lastPrice;
    private int lastQuantity;
    private int buyQuantity;
    private int sellQuantity;

    private long volume;
    private double averagePrice;

    private int oi;
    private int oiDayHigh;
    private int oiDayLow;

    private double netChange;
    private double lowerCircuitLimit;
    private double upperCircuitLimit;

    private double open;
    private double high;
    private double low;
    private double close;

    private List<OrderBookEntry> bids;
    private List<OrderBookEntry> asks;

    @Data
    public static class OrderBookEntry {
        private double price;
        private int quantity;
        private int orders;
    }

    public OHLC getOHLC() {
        return new OHLC(open, high, low, close);
    }
}
