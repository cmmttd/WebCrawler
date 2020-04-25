package crawler;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Crawler {

    private static final ConcurrentHashMap<String, Map<String, Integer>> wordsCount = new ConcurrentHashMap<>() {
        @Override
        public String toString() {
            return this.entrySet()
                    .stream()
                    .map(x -> x.getKey() + " : " + x.getValue())
                    .collect(Collectors.joining("\n"));
        }
    };

    public static Map<String, Map<String, Integer>> findHundred(String root, int depth) throws InterruptedException, IOException {
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

        ExecutorService service = Executors.newCachedThreadPool();
        allLinks.forEach(x -> service.execute(() -> wordsCount.put(x, parseLink(x))));

        service.shutdown();
        service.awaitTermination(20, TimeUnit.SECONDS);

        return wordsCount;
    }

    public static LinkedHashMap<String, Integer> parseLink(String link) {
        try {
            HashMap<String, Integer> temp = Arrays
                    .stream(Jsoup.connect(link)
                            .timeout(5000)
                            .get()
                            .text()
                            .split("\\s+"))
                    .filter(x -> x.matches("^[\\w]+$")) //utf-8 like words
                    .collect(Collectors.toMap(x -> x, x -> 1, (x1, x2) -> x1 += 1, HashMap::new));

            return temp.entrySet()
                    .stream()
                    .sorted((x1, x2) -> x2.getValue() - x1.getValue())
                    .limit(100)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum, LinkedHashMap::new));
        } catch (IOException e) {
            return new LinkedHashMap<>() {{
                put("Connection failed", 0);
            }};
        }
    }

    private Crawler() {
    }
}
