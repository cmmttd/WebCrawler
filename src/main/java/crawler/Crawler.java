package crawler;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Crawler {

    private static final ConcurrentHashMap<String, Map<String, Integer>> wordsCount = new ConcurrentHashMap<>() {
        @Override
        public String toString() {
            return this.entrySet()
                    .stream()
                    .map(x -> x.getKey() + " = " + x.getValue())
                    .collect(Collectors.joining("\n"));
        }
    };

    public static Map findHundred(String root, int depth) {
        try {
            HashSet<String> allLinks = Jsoup.connect(root)
                    .timeout(3000)
                    .get()
                    .select("a[href]")
                    .stream()
                    .map(x -> x.attr("href"))
                    .filter(x -> x.startsWith("http"))
                    .distinct()
                    .limit(depth)
                    .collect(Collectors.toCollection(HashSet::new));
            allLinks.add(root);

            //ExecutorService service = Executors.newCachedThreadPool();
            for (String curLink : allLinks) {
                try {
                    Map<String, Integer> curWordsMap = parseLink(curLink);
//                    wordsCount.put(curLink, curWordsMap);
                    System.out.println(curLink + " = " + curWordsMap);
                } catch (IOException e) {
//                    wordsCount.put(curLink, new LinkedHashMap<>());
                    System.out.println(curLink + " = {}");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordsCount;
    }

    public static LinkedHashMap<String, Integer> parseLink(String link) throws IOException {

        HashMap<String, Integer> res = Arrays
                .stream(Jsoup.connect(link)
                        .timeout(5000)
                        .get()
                        .text()
                        .split("\\s+"))
                .filter(x -> x.matches("^\\w+$") && !x.matches("^\\d+$"))
                .collect(Collectors.toMap(x -> x, x -> 1, (x1, x2) -> x1 += 1, HashMap::new));

        return res.entrySet()
                .stream()
                .sorted((x1, x2) -> x2.getValue() - x1.getValue())
                .limit(100)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x1, x2) -> x1, LinkedHashMap::new));

    }

    private Crawler() {
    }
}
