package org.dexteradei.whitegold.domain.model.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class DepthMetrics {
    private String symbol;
    private Instant timestamp;
    private double bestBidPrice;
    private double bestAskPrice;
    private long bestBidQty;
    private long bestAskQty;
}
