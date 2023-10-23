package com.example.searchengine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.*;

public class MultithreadCrawler extends Crawler {

    private ThreadPoolTaskExecutor executorService;

    private CopyOnWriteArraySet<String> visited;

    private CopyOnWriteArraySet<String[]> lines;

    private ObserveRunnable observeRunnable;

    private boolean done = false;

    public MultithreadCrawler(String indexFileName){
        //TODO: initialize
        super(indexFileName);

        int availableCores = Runtime.getRuntime().availableProcessors();
        executorService = new ThreadPoolTaskExecutor();
        executorService.setCorePoolSize(availableCores);
        executorService.initialize();

        visited = new CopyOnWriteArraySet<>();
        lines = new CopyOnWriteArraySet<>();

        /* 
            Needs to be initialised before observerThread to ensure that observeRunnable is properly associated 
            with the observerThread and can monitor the executorService correctly. 
        */
        observeRunnable = new ObserveRunnable(this);

        this.crawl("https://api.interactions.ics.unisg.ch/hypermedia-environment/cc2247b79ac48af0");

    }

    public void crawl(String startUrl){
        double startTime = System.currentTimeMillis();
        System.out.println(startTime);
        //TODO: complete
        executorService.submit(new CrawlerRunnable(this, startUrl));
        Thread observerThread = new Thread(observeRunnable);
        observerThread.start();

        // Wait for observerThread to complete
        try {
            observerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 

        double endTime = System.currentTimeMillis();
        System.out.println(endTime);
        double duration = endTime - startTime;
        System.out.println("duration: " + duration);
    }

    /*
      TODO: complete class.
      The purpose of this runnable is to do two tasks:
      1. Process the page at the given url (startUrl).
      2. Create new jobs for the hyperlinks found in the page.
      The instances of this class are used as input to the executorService.submit method.
    */
    class CrawlerRunnable implements Runnable{

        MultithreadCrawler crawler;

        String startUrl;

        public CrawlerRunnable(MultithreadCrawler crawler, String startUrl){
            this.crawler = crawler;
            this.startUrl = startUrl;
        }

        @Override
        public void run() {

            String[] line = new String[4];
            
            try {
                // Fetch and parse the webpage at startUrl
                Document document = Jsoup.connect(startUrl).get();

                // Extract hyperlinks from the webpage
                Elements links = document.select("a[href]");

                // Extract words from the webpage
                Elements paragraph = document.select("p");

                line[0] = "/" + startUrl.split("/")[4];

                for (Element link : links) {
                    String href = link.attr("abs:href");

                    // Check if the link hasn't been visited already
                    if (!crawler.visited.contains(href)) {
                        // Mark the link as visited
                        crawler.visited.add(href);

                        // Create a new crawl job for the link
                        crawler.executorService.submit(new CrawlerRunnable(crawler, href));
                    }
                }

                int count = 1;
                // Process the webpage (extract data and update lines) 
                for (Element p : paragraph) {
                    line[count] = p.text().trim();
                    count++;
                }

                crawler.lines.add(line);
                System.out.println(line);
                
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /*TODO: complete class.
        The purpose of this class is to monitor whether the executorService has completed all its threads and then print
        the lines on the index.csv file.
        This runnable should be run on a thread separate from the executorService.
     */
    class ObserveRunnable implements Runnable {
        private MultithreadCrawler crawler;


        public ObserveRunnable(MultithreadCrawler crawler) {
            this.crawler = crawler;
        }

        @Override
        public void run() {
            //TODO: complete

            System.out.println("Observer thread started.");

            while (true) {
                if (executorService.getActiveCount() == 0 && executorService.getQueueSize() == 0) {
                    try {

                        System.out.println("Observer thread completed.");

                        FileWriter fileWriter = new FileWriter(indexFileName);
                        CSVWriter writer = new CSVWriter(fileWriter, ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, "\r\n"); //TODO: macOS and Linux users should change Line to "\n".

                        for (String[] line : lines) {
                            writer.writeNext(line);
                        }

                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                } else {
                    try {
                        // Sleep and check again
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
