package crawler;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * В приложение необходимо встроить ролевую модель.
 * Пользователь обладает атрибутами: имя, пароль, электронная почта, список ролей. Пользователь должен иметь уникальные ключи по полям: имя пользователя и электронная почта.
 * Должен быть реализован CRUD для необходимых сущностей, а также поиск пользователя по имени и/или электронной почте.
 * <p>
 * Количество необходимых таблиц и параметров определяется разработчиком самостоятельно!
 * Ролевая модель должна встраивается в существующее решение и поэтому при реализации необходимо пользоваться следующими шаблонами проектирования:
 * • Domain Model (Подсказка: @Entity, @Table, @Id, @Column, @JoinColumn)
 * • Repository (Подсказка: JPA, @Repository, @Query)
 * • Service Layer (@Service, @Component, @Transactional)
 * <p>
 * copy: belyankin.v.s@sberbank.ru
 */

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
