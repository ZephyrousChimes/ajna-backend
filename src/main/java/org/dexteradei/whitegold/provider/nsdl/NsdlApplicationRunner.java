package org.dexteradei.whitegold.provider.nsdl;

import java.time.Duration;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class NsdlApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Flux.interval(Duration.ofMinutes(15))
        .flatMap ( this::detectionFunction )
        .filter(available -> available)
        .next() // only take the first 'true'
        .flatMap (
            this::triggerSeleniumScraper // launch Selenium scraping job
        )
        .subscribe();
    }

    public Mono<Boolean> detectionFunction(Long interrupt) {
        return Mono.just(false);
    }

    public Mono<Void> triggerSeleniumScraper(boolean b) {
        return Mono.empty();
    }
    
}
