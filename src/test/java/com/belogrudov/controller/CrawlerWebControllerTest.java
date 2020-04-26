package com.belogrudov.controller;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CrawlerWebControllerTest extends TestCase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CrawlerWebController crawlerWebController;

    @Test
    public void testGet() throws Exception {
        assertNotNull(crawlerWebController);
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Ready to find")));
    }

    @Test
    public void testParse() throws Exception {
        assertNotNull(crawlerWebController);
        this.mockMvc.perform(post("/parse").contentType("application/json")
                .content("{\"link\": \"https://ru.wikipedia.org/wiki/Java\",\"depth\": 42}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("wikipedia")));
    }
}