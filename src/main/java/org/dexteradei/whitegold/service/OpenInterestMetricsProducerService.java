package org.dexteradei.whitegold.service;

import org.dexteradei.whitegold.domain.model.kafka.OpenInterestMetrics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OpenInterestMetricsProducerService {

    private final KafkaTemplate<String, OpenInterestMetrics> kafkaTemplate;
    private static final String TOPIC = "oi-metrics";

    public OpenInterestMetricsProducerService(KafkaTemplate<String, OpenInterestMetrics> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produce(String symbol, Instant timestamp, double currentOi, double dayHighOi,
                        double dayLowOi, double oiChangePercent) {
        OpenInterestMetrics metrics = new OpenInterestMetrics(symbol, timestamp, currentOi, dayHighOi, dayLowOi, oiChangePercent);

        kafkaTemplate.send(TOPIC, symbol, metrics);
    }
}
