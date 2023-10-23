package com.example.searchengine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class Crawler {

     final String indexFileName;

    private String baseUrl = "https://api.interactions.ics.unisg.ch/hypermedia-environment/";

    /**
     *
     * @param indexFileName the name of the file that is used as index.
     */
    protected Crawler(String indexFileName) {
        this.indexFileName = indexFileName;
    }

    /**
     *
     * @param url the url where the crawling starts
     */
    public abstract void crawl(String url);

    public  List<List<String>> getInfo(String urlString){
        List<String> keywords = new ArrayList<>();
        List<String> hyperlinks = new ArrayList<>();
        List<List<String>> returnList = new ArrayList<>();
        try {
            URL url = new URL(urlString);
            Elements elements; //TODO: initialize elements based on the webpage at the given url.
            //TODO: Use elements to put the keywords in the webpage in the list keywords.
            //TODO: Use elements to the hyperlinks to other pages in the environment in the list hyperlinks.
            // safe content in doc
            Document doc = Jsoup.connect(urlString).get();
            // retrieve links and paragraphs from doc object
            Elements links = doc.select("a[href]");
            Elements paragraph = doc.select("p");

            // Print the links and add to list
            //System.out.println("print crawler link: ");
            for (Element link : links) {
                //System.out.println(link.attr("href"));
                hyperlinks.add(link.attr("href").trim());
            }

            // Print keywords and add to list
            // System.out.println("print crawler words: ");
            for(Element p : paragraph){
                //System.out.println(p.text());
                keywords.add(p.text().trim());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        returnList.add(keywords);
        returnList.add(hyperlinks);
        return returnList;
    }
}
