package com.apeeling.jpeeling;


import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.jsoup.Jsoup;

/**
 * @author stophman1
 */

public class HeathcliffScraper {
    public static void main(String[] args) throws IOException {
        String date = "2020-04-20";
        System.out.println(GetImage(LocalDate.parse(date)));
        date = "2019-04-20";
        System.out.println(GetImage(LocalDate.parse(date)));
    }

    public static String GetImage(LocalDate date) throws IOException {
        String theCliff = "https://www.gocomics.com/heathcliff/";
        String theDate = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String theUrl = theCliff + theDate;
        return Jsoup.connect(theUrl).get().getElementsByClass("js-comic-swipe").attr("data-image");
    }
}
