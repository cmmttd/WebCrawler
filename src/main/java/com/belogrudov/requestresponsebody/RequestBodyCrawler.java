package com.belogrudov.requestresponsebody;

import lombok.Data;

@Data
public class RequestBodyCrawler {
    private String link;
    private int depth;
}
