package org.dexteradei.whitegold.provider.kite;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

import org.dexteradei.whitegold.util.TerminalPrompt;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;;

@Component
public class KiteConnectClient {

    private final String USER_ID;
    private final String PASSWORD;
    private final String API_KEY;

    public KiteConnectClient(
        @Value("${secrets.kite-connect.user-id}") String USER_ID,
        @Value("${secrets.kite-connect.password}") String PASSWORD,
        @Value("${secrets.kite-connect.api-key}") String API_KEY
    ) {
        this.USER_ID = USER_ID;
        this.PASSWORD = PASSWORD;
        this.API_KEY = API_KEY;
    }
    
    public Mono<Void> connect() {

        Mono<Void> connectEvent = Mono.fromCallable(() -> {
            ChromeOptions options = new ChromeOptions();
            // options.addArguments("--headless");
            options.addArguments("--ignore-certificate-errors");
            options.setAcceptInsecureCerts(true);
            
            WebDriver driver = new ChromeDriver(options);
            driver.manage().deleteAllCookies();
            // Make call to Kite Authentication page
            driver.get(String.format("https://kite.zerodha.com/connect/login?api_key=%s", this.API_KEY));

            // Wait for loading
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement userIdElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("userid")));
            WebElement passwordElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
            WebElement submitButtonElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.actions > button.button-orange.wide")));

            // Get the URL to compare for change
            String currentUrl = driver.getCurrentUrl();

            // Fill the form and submit
            userIdElement.sendKeys(this.USER_ID);
            passwordElement.sendKeys(this.PASSWORD);
            submitButtonElement.click();

            // Ask for Authenticator Code
            String authCode = TerminalPrompt.prompt("Enter Kite App authenticator code");
            WebDriverWait waitAuthCode = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement authCodeElement = waitAuthCode.until(ExpectedConditions.presenceOfElementLocated(By.id("userid")));
            // WebElement authCodeSubmit = waitAuthCode.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.actions > button.button-orange.wide")));

            authCodeElement.sendKeys(authCode);
            // authCodeSubmit.click();
            // The submit button is clicked as soon the authCode is input. It goes stale due to immediate redirection.

            // Await change of URL
            WebDriverWait waitNew = new WebDriverWait(driver, Duration.ofSeconds(10));
            waitNew.until(d -> !d.getCurrentUrl().equals(currentUrl));

            driver.quit();
            return (Void)null;
        }).subscribeOn(Schedulers.boundedElastic());

        return connectEvent;
    }
}

