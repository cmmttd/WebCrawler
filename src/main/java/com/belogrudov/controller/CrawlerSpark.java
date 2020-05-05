package com.belogrudov.controller;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.jsoup.Jsoup;
import scala.Tuple2;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CrawlerSpark {

    private static final ConcurrentHashMap<String, Map<String, Integer>> wordsCount = new ConcurrentHashMap<String, Map<String, Integer>>() {
        @Override
        public String toString() {
            return this.entrySet()
                    .stream()
                    .map(x -> x.getKey() + " : " + x.getValue())
                    .collect(Collectors.joining("\n"));
        }
    };

    public static Map<String, Map<String, Integer>> findHundred(String root, int depth) throws InterruptedException, IOException {
        if (root == null || root.isEmpty() || depth < 0) throw new IllegalArgumentException();
        HashSet<String> allLinks = Jsoup.connect(root)
                .timeout(5000)
                .get()
                .select("a[href]")
                .stream()
                .map(x -> x.attr("href"))
                .filter(x -> x.startsWith("http"))
                .distinct()
                .limit(depth)
                .collect(Collectors.toCollection(HashSet::new));
        allLinks.add(root);

        Logger.getLogger("org.apache").setLevel(Level.WARN);
        allLinks.forEach(x -> wordsCount.put(x, parseLink(x)));

        return wordsCount;
    }

    private static LinkedHashMap<String, Integer> parseLink(String link) {
        try {
            SparkConf conf = new SparkConf().setAppName("CrawlerSpark").setMaster("local[*]");

            try (JavaSparkContext sc = new JavaSparkContext(conf)) {
                List<String> list = Arrays.asList(Jsoup.connect(link)
                        .timeout(5000)
                        .get()
                        .text()
                        .split("\\s+"));

                JavaRDD<String> rdd = sc.parallelize(list);

                return rdd
                        .map(String::toLowerCase)
                        .filter(x -> x.matches("^[a-zA-ZА-Яа-я_]{2,}$"))
                        .mapToPair(w -> new Tuple2<>(w, 1))
                        .reduceByKey(Integer::sum)
                        .mapToPair(Tuple2::swap)
                        .sortByKey(false)
                        .take(100)
                        .stream()
                        .collect(Collectors.toMap(x -> x._2, x -> x._1, Integer::sum, LinkedHashMap::new));
            }
        } catch (IOException e) {
            return new LinkedHashMap<String, Integer>() {{
                put("Connection failed", 0);
            }};
        }
    }

    private CrawlerSpark() {
    }
}
