package org.dexteradei.whitegold.service;

import org.dexteradei.whitegold.domain.model.kafka.DepthMetrics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class DepthMetricsProducerService {

    private final KafkaTemplate<String, DepthMetrics> kafkaTemplate;
    private static final String TOPIC = "depth-metrics";

    public DepthMetricsProducerService(KafkaTemplate<String, DepthMetrics> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produce(String symbol, Instant timestamp,
                        double bestBidPrice, long bestBidQty,
                        double bestAskPrice, long bestAskQty) {
        DepthMetrics metrics = new DepthMetrics(symbol, timestamp, bestBidPrice, bestAskPrice, bestBidQty, bestAskQty);

        kafkaTemplate.send(TOPIC, symbol, metrics);
    }
}
