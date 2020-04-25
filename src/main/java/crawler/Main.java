package crawler;

/**
 * Требуется написать многопоточный HTTP-crawler.
 * Crawler должен принимать на вход URL сайта и глубину сканирования.
 * На выход должен отдаваться словарь с топ-100 самых встречающихся слов в текстовом содержимом сайта с частотой их употребления.
 * (Осуществляя сканирование crawler должен переходить по ссылкам в глубь сайта, но не более чем на указанную глубину. Ссылки на другие сайты должны игнорироваться)
 * Язык: Java
 */

public class Main {

    public static void main(String[] args) {

        String url = "https://pikabu.ru/";
        int depth = 10000;

        Crawler.findHundred(url, depth);
        //System.out.println(Crawler.findHundred(url, depth));
    }
}
