package com.example.searchengine;

import com.opencsv.CSVWriter;

import java.io.*;
import java.util.*;

public class SimpleCrawler extends Crawler {


    protected SimpleCrawler(String indexFileName) {
        super(indexFileName);
    }

    public void crawl(String startUrl){
        try {

            long searchingTime = System.currentTimeMillis();

            Set<String[]> lines = explore(startUrl, new HashSet<>(), new HashSet<>());
            FileWriter fileWriter = new FileWriter(indexFileName);
            CSVWriter writer = new CSVWriter(fileWriter, ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, "\r\n"); //TODO: macOS and Linux users should change Line to "\n".

            for (String[] line : lines) {
                writer.writeNext(line);
            }

            // additional line: close writer!!!!
            writer.close();
            long endingTime = System.currentTimeMillis();
            long duration = endingTime - searchingTime;
            System.out.println("duration simple crawler: " + duration);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param startUrl the url where the crawling operation starts
     * @param lines stores the lines to print on the index file
     * @param visited stores the urls that the program has already visited
     * @return the set of lines to print on the index file
     */
    public Set<String[]> explore(String startUrl, Set<String[]> lines, Set<String> visited){
        //int dur = 20;  @Joey for testing with test.java (main)(otherwise it takes more than 2 min with the whole input)
        SimpleCrawler simpleCrawler = new SimpleCrawler(indexFileName);
        Queue<String> urlQueue = new LinkedList<>(); // Queue to store encountered URLs to be visited
        // trim end
        // 1. Get the locator of the current URL
        String currentUrl = startUrl;
        String[] splittedUrl = currentUrl.split("/");
        currentUrl = "/" + splittedUrl[splittedUrl.length - 1].trim();
        urlQueue.add(currentUrl); // Add the start URL to the queue

        //while (!urlQueue.isEmpty() && (dur > 0)) {  @Joey for testing with test.java
        while (!urlQueue.isEmpty()) {
            //dur--;  @Joey for testing with test.java
            // 2. Get the next URL to visit from the queue
            currentUrl = urlQueue.remove();
            // 3. Process the result of GET INFO (Crawler.java)
            String urlBase = "https://api.interactions.ics.unisg.ch/hypermedia-environment";
            List<List<String>> resultGetInfo = simpleCrawler.getInfo(urlBase + currentUrl);
            // 4. current content: Combine locator URL and tokens into a single string
            StringBuilder combinedString = new StringBuilder();
            combinedString.append(currentUrl);
            for (String token : resultGetInfo.get(0)) {
                token = token.trim();
                combinedString.append(",").append(token);
            }
            // Add the combined string to the 'lines' set
            lines.add(new String[]{combinedString.toString().replaceAll("\\s+", " ")});

            // 5. Get the links for the next level and add them to the queue
            List<String> linkList = resultGetInfo.get(1);
            visited.add(currentUrl);
            for (String link : linkList) {
                String newLink = "/" + link;
                if (!visited.contains(newLink)) {
                    urlQueue.add(newLink); // Add unvisited links to the queue
                }
            }
        }
        System.out.println("Done index.csv!");
        return lines;

    }

}
