package yong.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yong.constants.ContentsTypeCode;
import yong.model.RequestVO;
import yong.service.SimpleService;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class SimpleController {

    private final SimpleService simpleService;

    @PostMapping("/post/request")
    public ResponseEntity methodPostRequest(@RequestBody @Valid RequestVO requestVO) {
        simpleService.method();
        return ResponseEntity.ok().body(requestVO);
    }

    @GetMapping("/get/request")
    public ResponseEntity methodGetRequest(@Valid RequestVO requestVO) {
        return ResponseEntity.ok().body(requestVO);
    }

    @GetMapping("/get/request/request-param")
    public ResponseEntity methodGetRequestRequestParam(@RequestParam ContentsTypeCode contentsTypeCode) {
        return ResponseEntity.ok().body(contentsTypeCode);
    }

    @GetMapping("/get/request/path-variable/{contentsTypeCode}")
    public ResponseEntity methodGetRequestPathVariable(@PathVariable ContentsTypeCode contentsTypeCode) {
        return ResponseEntity.ok().body(contentsTypeCode);
    }
}
