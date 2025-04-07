package org.dexteradei.whitegold.adapter.kite;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;

import java.nio.channels.Pipe.SourceChannel;
import java.time.Duration;

import org.dexteradei.whitegold.adapter.kite.KiteConnectDto;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;;

@Component
public class KiteConnectClient implements ApplicationRunner{
    
    public Flux<KiteConnectDto> connect() {
        return null;
    }

    private void authenticateBrowser() {
        return;
    }

    private KiteConnectDto deserialize() {
        return null;
    }

    public static void main(String... args) {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.selenium.dev/selenium/web/web-form.html");
        System.out.println("TITLE: " + driver.getTitle());
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
        System.out.println("WAIT IS OVER");
        WebElement textBox = driver.findElement(By.name("my-text"));
        WebElement submitButton = driver.findElement(By.cssSelector("button"));
        driver.quit();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("BABA");
    }

}

