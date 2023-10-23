package com.example.searchengine;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Searcher {
    /**
     *
     * @param keyword to search
     * @param flippedIndexFileName the file where the search is performed.
     * @return the list of urls
     */
    public List<String> search(String keyword, String flippedIndexFileName){
        long duration = 0; //TODO: update the value in the code
        List<String> urls = new ArrayList<>();
        //TODO: complete
        String urlBase = "https://api.interactions.ics.unisg.ch/hypermedia-environment";
        //TODO: complete
        try {
            CSVReader csvReader = new CSVReader(new FileReader(flippedIndexFileName));
            List<String[]> csvLines = csvReader.readAll();
            for (String[] line : csvLines) {
                if (line.length > 0 && line[0].equals(keyword)) {
                    int numUrls = line.length -1 ;
                    // collect urls
                    for (int i = 1; i < numUrls; i++) {
                        String currentUrl = line[i];
                        String finalUrl = urlBase + currentUrl;
                        if (!urls.contains(currentUrl)) {
                            urls.add(finalUrl);
                        }

                    }
                }

            }

        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }

        System.out.println("duration searcher flipped: "+duration);
        for (String url : urls) {
            System.out.println(url);
        }
        return urls;
    }


}
