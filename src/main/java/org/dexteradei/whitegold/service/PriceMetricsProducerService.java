package org.dexteradei.whitegold.service;

import org.dexteradei.whitegold.domain.model.kafka.PriceMetrics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PriceMetricsProducerService {

    private final KafkaTemplate<String, PriceMetrics> kafkaTemplate;
    private static final String TOPIC = "price-metrics";

    public PriceMetricsProducerService(KafkaTemplate<String, PriceMetrics> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produce(String symbol, Instant timestamp, double lastPrice, double changePercent,
                        double dayHigh, double dayLow) {
        PriceMetrics metrics = new PriceMetrics(symbol, timestamp, lastPrice, changePercent, dayHigh, dayLow);

        kafkaTemplate.send(TOPIC, symbol, metrics);
    }
}
