package com.biengual.userapi.test;

import com.biengual.userapi.crawling.service.TranslateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final TranslateService translateService;

    @GetMapping("/test/translate/{text}")
    public ResponseEntity testTranslate(
        @PathVariable String text
    ) {

//        translateService.translate(text, "en", "ko");

        return ResponseEntity.ok(translateService.translate(text, "en", "ko"));
    }
}
