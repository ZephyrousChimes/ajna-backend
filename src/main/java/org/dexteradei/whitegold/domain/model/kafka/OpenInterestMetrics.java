package org.dexteradei.whitegold.domain.model.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class OpenInterestMetrics {
    private String symbol;
    private Instant timestamp;
    private double currentOi;
    private double dayHighOi;
    private double dayLowOi;
    private double oiChangePercent;
}
