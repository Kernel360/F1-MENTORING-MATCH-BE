package com.biengual.userapi.crawling.infrastructure;

import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentType;
import com.biengual.userapi.content.domain.ContentCustomRepository;
import com.biengual.userapi.crawling.application.TranslateService;
import com.biengual.userapi.crawling.domain.CrawlingStore;
import com.biengual.userapi.crawling.presentation.CrawlingResponseDto;
import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.script.domain.entity.CNNScript;
import com.biengual.userapi.script.domain.entity.Script;
import com.biengual.userapi.script.domain.entity.YoutubeScript;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.biengual.userapi.message.error.code.CrawlingErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingStoreImpl implements CrawlingStore {
	private final TranslateService translateService;
	private final ContentCustomRepository contentCustomRepository;

	@Value("${YOUTUBE_API_KEY}")
	private String YOUTUBE_API_KEY;

	@Override
	public ContentCommand.Create getYoutubeDetail(ContentCommand.CrawlingContent command) {
		// Extract the video ID from the URL
		String videoId = extractVideoId(command.url());

		// Check Already Stored In DB
		verifyCrawling(videoId);

		// Create the request URL for the transcript service
		String requestUrl = "https://www.googleapis.com/youtube/v3/videos?id=" + videoId
			+ "&part=snippet, contentDetails" + "&key=" + YOUTUBE_API_KEY;

		// Set up headers with your API key
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cookie", "access_token="
			+ SecurityContextHolder.getContext().getAuthentication().getCredentials());

		// Create an HTTP entity with headers
		// Use RestTemplate to make the request
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response
			= restTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);

		JsonNode snippetNode = null;
		JsonNode contentDetailsNode = null;
		String category = null;

		try {
			snippetNode = getSnippetNode(response.getBody()).path("snippet");
			contentDetailsNode = getSnippetNode(response.getBody()).path("contentDetails");
			category = getCategoryName(snippetNode.path("categoryId").asText());
		} catch (Exception e) {
			throw new CommonException(CRAWLING_JSOUP_FAILURE);
		}

		Duration duration = Duration.parse(contentDetailsNode.path("duration").asText());
		if (duration.compareTo(Duration.ofMinutes(8)) > 0) {
			throw new CommonException(CRAWLING_OUT_OF_BOUNDS);
		}
		return ContentCommand.Create.builder()
			.url(command.url())
			.title(snippetNode.path("title").asText())
			.imgUrl(getThumbnailUrl(snippetNode.path("thumbnails")))
			.category(category)
			.contentType(ContentType.LISTENING)
			.script(getYoutubeScript(command.url(), Double.parseDouble(String.valueOf(duration.getSeconds()))))
			.build();
	}

	@Override
	public ContentCommand.Create getCNNDetail(ContentCommand.CrawlingContent command) {
		CrawlingResponseDto.ContentDetailRes response = fetchArticle(command.url());

		// Check Already Stored In DB
		verifyCrawling(command.url());

		return ContentCommand.Create.builder()
			.url(response.url())
			.title(response.title())
			.imgUrl(response.imgUrl())
			.category(response.category())
			.contentType(ContentType.READING)
			.script(response.script())
			.build();
	}

	// Internal Methods ------------------------------------------------------------------------------------------------

	// LISTENING - YOUTUBE

	// SELENIUM
	public List<Script> getYoutubeScript(String youtubeInfo, double seconds) {
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
			transcriptLines = runSelenium(driver, youtubeInfo, seconds);
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

	// SELENIUM
	public List<Script> runSelenium(WebDriver driver, String youtubeInfo, double seconds) throws Exception {
		List<Script> scripts = new ArrayList<>();

		driver.get(youtubeInfo);
		setUpSelenium(driver);
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
						text, translateService.translate(text, "en", "ko")
					)
				);

			}
		}
		log.info("SELENIUM : FINISH CRAWLING");
		return scripts;
	}

	// SELENIUM
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

	// SELENIUM
	private double getSecondsFromString(String time) {
		return Double.parseDouble(time.split(":")[0]) * 60 + Double.parseDouble(time.split(":")[1]);
	}

	private JsonNode getSnippetNode(String body) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readTree(body).path("items").get(0);
	}

	private String getThumbnailUrl(JsonNode thumbnailNode) {
		Iterator<Map.Entry<String, JsonNode>> thumbnails = thumbnailNode.fields();
		int prevWidth = 0;
		JsonNode thumbnailInfo = thumbnails.next().getValue();

		while (thumbnails.hasNext()) {
			Map.Entry<String, JsonNode> thumbnailMap = thumbnails.next();
			JsonNode comparator = thumbnailMap.getValue();
			if (Integer.parseInt(comparator.get("width").asText()) > prevWidth) {
				thumbnailInfo = comparator;
				prevWidth = Integer.parseInt(thumbnailMap.getValue().get("width").asText());
			}
		}

		return thumbnailInfo.get("url").asText();
	}

	private String extractVideoId(String youtubeUrl) {
		// Logic to extract video ID from URL
		String[] parts = youtubeUrl.split("v=");
		return parts.length > 1 ? parts[1] : "";
	}

	private String getCategoryName(String categoryId) throws Exception {
		String requestUrl =
			"https://www.googleapis.com/youtube/v3/videoCategories?part=snippet&regionCode=US&key=" + YOUTUBE_API_KEY;

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(response.getBody());
		JsonNode itemsNode = rootNode.path("items");

		for (JsonNode item : itemsNode) {
			if (item.path("id").asText().equals(categoryId)) {
				return item.path("snippet").path("title").asText();
			}
		}

		return "Unknown Category";
	}

	// READING - CNN

	public CrawlingResponseDto.ContentDetailRes fetchArticle(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			throw new CommonException(CRAWLING_JSOUP_FAILURE);
		}

		// 제목 추출
		String title = doc.select("h1.headline__text").text();

		// 카테고리 추출
		Element categoryElement = doc.selectFirst("meta[name=meta-section]");
		String category = categoryElement != null ? categoryElement.attr("content") : "Unknown Category";

		// 이미지 URL 추출
		Elements images = doc.select("img.image__dam-img[src]");
		String imgUrl = "%s.jpg".formatted(
			images.get(0).attr("src")
				.split(".jpg")[0]
		);
		int preWidth = getWidthOfImage(images.get(0));

		for (Element image : images) {
			if (preWidth < getWidthOfImage(image)) {
				imgUrl = "%s.jpg".formatted(
					image.attr("src")
						.split(".jpg")[0]
				);
				preWidth = getWidthOfImage(image);
			}
		}

		// 본문 추출
		Elements paragraphs = doc.select("div.article__content p");
		StringBuilder fullText = new StringBuilder();
		for (Element paragraph : paragraphs) {
			fullText.append(paragraph.text()).append(" ");
		}

		// 본문을 문장 단위로 나누기
		List<String> sentences = splitIntoSentences(fullText.toString());
		return CrawlingResponseDto.ContentDetailRes.of(
			url,
			title,
			imgUrl,
			category,
			sentences.stream()
				.map(sentence -> (Script)CNNScript.of(
						sentence, translateService.translate(sentence, "en", "ko")
					)
				).toList()
		);
	}

	private int getWidthOfImage(Element imginfo) {
		return Integer.parseInt(
			imginfo.attr("src")
				.split("w_")[1]
				.split(",c")[0]
		);
	}

	private List<String> splitIntoSentences(String text) {
		List<String> sentences = new ArrayList<>();

		// 정규 표현식을 사용하여 문장 단위로 나누기
		String[] splitSentences = text.split("(?<=[.!?])\\s+");

		for (String sentence : splitSentences) {
			sentences.add(sentence.trim());
		}

		return sentences;
	}

	// COMMON

	private void verifyCrawling(String url) {
		if (contentCustomRepository.existsByUrl(url)) {
			throw new CommonException(CRAWLING_ALREADY_DONE);
		}
	}
}
