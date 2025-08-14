package org.dexteradei.whitegold.domain.model.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class PriceMetrics {
    private String symbol;
    private Instant timestamp;
    private double lastPrice;
    private double changePercent;
    private double dayHigh;
    private double dayLow;
}
