package com.example.searchengine;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
@Component
public class IndexFlipper {

    public void flipIndex(String indexFileName, String flippedIndexFileName) {
        try {
            CSVReader csvReader = new CSVReader(new FileReader(indexFileName));
            List<String[]> csvLines = csvReader.readAll();
            Set<String[]> lines = new HashSet<>();
            //TODO: define lines to contain the lines that should be printed to index_flipped.csv
            // processing of the index file
            for (String[] line : csvLines) {
                // get url of current line
                String urlLine = line[0];
                // extract all unique tokens from the line
                Set<String> tokens = new HashSet<>(); // Use a set to store unique tokens
                // ensure that there are no duplicates
                for (int i = 1; i < line.length; i++) {
                    String currentToken = line[i];
                    if (!tokens.contains(currentToken)) {
                        tokens.add(currentToken);
                    }
                }

                // For each unique token, create a line with all URLs
                for (String token : tokens) {
                    List<String> urls = new ArrayList<>();
                    urls.add(urlLine); // add url of current token / line
                    // check every line: is there the same token? add url to list
                    for (String[] otherLine : csvLines) {
                        for (int i = 1; i < otherLine.length; i++) {
                            if (otherLine[i].equals(token) && !urls.contains(otherLine[0])) {
                                urls.add(otherLine[0]);
                            }

                        }
                    }
                    lines.add(new String[]{token, String.join(",", urls)});
                }
            }
            FileWriter fileWriter = new FileWriter(flippedIndexFileName);
            CSVWriter writer = new CSVWriter(fileWriter, ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, "\r\n");
            //CSVWriter writer = new CSVWriter(new FileWriter(flippedIndexFileName), ',', CSVWriter.NO_QUOTE_CHARACTER, ' ', "\r\n");
            for (String[] line : lines) {
                writer.writeNext(line);
                // Expected :[/8a5ab92512fe56f2 , /57050a8402c6623c , /9f8b163241d1839e]
                //Actual   : [/8a5ab92512fe56f2, /57050a8402c6623c, /9f8b163241d1839e]
                // Attention: tests only pass, if the csv file is not formated as on the task description file, there are no whitespaces allowed!
            }
            System.out.println("writing to file done!");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
