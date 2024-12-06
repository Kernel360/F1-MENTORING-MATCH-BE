package com.biengual.userapi.crawling.application;

import static com.biengual.core.response.error.code.CrawlingErrorCode.*;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.biengual.core.annotation.WebCrawler;
import com.biengual.core.domain.document.content.script.Script;
import com.biengual.core.domain.document.content.script.YoutubeScript;
import com.biengual.core.response.error.exception.CommonException;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebCrawler
@RequiredArgsConstructor
public class SeleniumWebCrawler {
    private final TranslateApiClient translateApiClient;

    // LISTENING - YOUTUBE
    // SELENIUM 크롤링 - 동적 크롤링
    public List<Script> getYoutubeScript(String url, double seconds) {
        // 운영체제 감지
        String os = System.getProperty("os.name").toLowerCase();

        // 공통 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--lang=en-US");
        options.addArguments("--start-maximized");
        options.addArguments("--headless");

        WebDriverManager.chromedriver().setup();

        if (os.contains("linux")) {
            log.info("SELENIUM : LINUX");
            // Ubuntu의 경우
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--ignore-ssl-errors=yes");
            options.addArguments("--ignore-certificate-errors");
            // Xvfb를 사용하는 경우
            try {
                log.info("SELENIUM : XVFB CHECK");
                if (System.getenv("DISPLAY") == null) {
                    log.info("SELENIUM : XVFB NEED TO START");
                    System.setProperty("DISPLAY", ":99");
                    // Xvfb를 실행
                    Process xvfbProcess = Runtime.getRuntime().exec("Xvfb :99 -ac &");
                    xvfbProcess.waitFor();
                    log.info("SELENIUM : XVFB START");
                }
                log.info("SELENIUM : XVFB RUN");
            } catch (IOException | InterruptedException e) {
                throw new CommonException(SELENIUM_RUNTIME_ERROR);
            }
        }

        WebDriver driver = new ChromeDriver(options);
        List<Script> transcriptLines;

        try {
            log.info("SELENIUM : SELENIUM DRIVER SET SUCCESS");
            transcriptLines = runSelenium(driver, url, seconds);
            log.info("SELENIUM : SELENIUM END");
        } catch (IOException | IllegalStateException e) {
            throw new CommonException(CRAWLING_TRANSLATE_FAILURE);
        } catch (Exception e) {
            throw new CommonException(SELENIUM_RUNTIME_ERROR);
        } finally {
            driver.quit();
            log.info("SELENIUM : DRIVER QUIT");
        }

        return transcriptLines;
    }

    // 셀레니움 으로 스크립트 크롤링 하기 위한 셋업
    private void setUpSelenium(WebDriver driver) throws InterruptedException {
        log.info("SELENIUM : SETUP SELENIUM START");

        // Initial setting
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
        Thread.sleep(5000);
        log.info("SELENIUM : SETUP SUCCESS");

        // Zoom out
        js.executeScript("document.body.style.zoom='30%'");
        Thread.sleep(5000);
        log.info("SELENIUM : ZOOMOUT SUCESS");

        // Click the "expand" button to expand
        List<WebElement> expandButton
            = driver.findElements(By.xpath("//tp-yt-paper-button[@id='expand']"));
        log.info("SELENIUM : FIND EXPAND BUTTON : {} ", expandButton);
        for (WebElement button : expandButton) {
            log.info("SELENIUM : FIND BUTTON : {} ", button.getText());
            if (button.getText().contains("more")) {
                log.info("SELENIUM : FIND SUCCESS MORE BUTTON : {}", button.getText());
                js.executeScript("arguments[0].click();", button);
                log.info("SELENIUM : EXPAND BUTTON CLICK");
                break;
            }
        }

        Thread.sleep(5000);
        // Locate and click the "Show transcript" button
        log.info("SELENIUM : WAITING TRANSCRIPTION BUTTON FIND");

        WebElement transcriptButton = driver.findElement(
            By.xpath("//yt-button-shape//button[@aria-label='Show transcript']")
        );

        log.info("SELENIUM : FIND TRANSCRIPT BUTTON");
        js.executeScript("arguments[0].click();", transcriptButton);
        log.info("SELENIUM : CLICK TRANSCRIPTION");
        Thread.sleep(5000);
    }

    // 셀레니움 으로 스크립트 크롤링
    public List<Script> runSelenium(WebDriver driver, String url, double seconds) throws Exception {
        List<Script> scripts = new ArrayList<>();

        driver.get(url);
        this.setUpSelenium(driver);
        log.info("SELENIUM : SELENIUM DRIVER SET UP COMPLETE");

        // Use XPath to find all elements containing the transcript text
        List<WebElement> segmentElements = driver.findElements(
            By.xpath("//ytd-transcript-segment-renderer"));
        log.info("SELENIUM : START CRAWLING");
        for (int i = 0; i < segmentElements.size(); ++i) {

            String time = segmentElements.get(i)
                .findElement(By.xpath(".//div[contains(@class, 'timestamp')]"))
                .getText();
            double startTime = getSecondsFromString(time);
            double endtime = seconds;

            if (i + 1 < segmentElements.size()) {
                endtime = getSecondsFromString(
                    segmentElements.get(i + 1)
                        .findElement(By.xpath(".//div[contains(@class, 'timestamp')]"))
                        .getText()
                );
            }

            String text = segmentElements.get(i)
                .findElement(By.xpath(".//yt-formatted-string[contains(@class, 'segment-text')]"))
                .getText();
            if (text != null && !text.isEmpty()) {
                scripts.add(
                    YoutubeScript.of(
                        startTime, endtime - startTime,
                        text, translateApiClient.translate(text, "en", "ko")
                    )
                );

            }
        }
        log.info("SELENIUM : FINISH CRAWLING");
        return scripts;
    }

    // SELENIUM
    private double getSecondsFromString(String time) {
        return Double.parseDouble(time.split(":")[0]) * 60 + Double.parseDouble(time.split(":")[1]);
    }
}
