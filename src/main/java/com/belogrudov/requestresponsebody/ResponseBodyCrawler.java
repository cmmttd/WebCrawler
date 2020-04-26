package com.belogrudov.requestresponsebody;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ResponseBodyCrawler {
    private Map<String, Map<String, Integer>> words;
}
