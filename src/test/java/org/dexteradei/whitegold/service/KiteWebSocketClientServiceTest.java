package org.dexteradei.whitegold.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KiteWebSocketClientServiceTest {

    @Autowired
    private KiteWebSocketClientService client;

    @Test
    public void clientIsNotNull() {
        Assertions.assertNotNull(client);
    }

    @Test
    public void fetchesApiKey() {
        Assertions.assertEquals(client.getAPI_KEY(), "ip1kn7r1g5qjrbju");
    }
}
