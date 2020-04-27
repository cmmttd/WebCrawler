package com.belogrudov.controller;

import com.belogrudov.requestresponsebody.RequestBodyCrawler;
import com.belogrudov.requestresponsebody.ResponseBodyCrawler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class CrawlerWebController {

    @GetMapping("/")
    public String get() {
        return "Ready to find";
    }

    @GetMapping("/{name}")
    public String getName(@PathVariable String name) {
        return "Ready to find " + name;
    }

    //curl --header "Content-Type: application/json" -X POST --data @request.json http://localhost:8080/parse
    @PostMapping("/parse")
    public ResponseEntity<ResponseBodyCrawler> parse(@RequestBody RequestBodyCrawler request) throws IOException, InterruptedException {
        return ResponseEntity.ok(new ResponseBodyCrawler(Crawler.findHundred(request.getLink(), request.getDepth())));
    }

}
