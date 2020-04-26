package com.belogrudov.requestresponsebody;

import lombok.Data;

import java.util.Map;

@Data
public class ResponseBodyCrawler {
    private Map<String, Map<String, Integer>> words;

    public ResponseBodyCrawler(Map<String, Map<String, Integer>> words) {
        this.words = words;
    }
}
