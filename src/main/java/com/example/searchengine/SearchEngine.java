package com.example.searchengine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.*;


@RestController
public class SearchEngine {

	public final String indexFileName = "./src/main/resources/index.csv";

	public final String flippedIndexFileName = "./src/main/resources/index_flipped.csv";

	public final String startUrl = "https://api.interactions.ics.unisg.ch/hypermedia-environment/cc2247b79ac48af0";

	@Autowired
	Searcher searcher;

	@Autowired
	IndexFlipper indexFlipper;

	@Autowired
	SearchEngineProperties properties;

	Crawler crawler;

	@PostConstruct
	public void initialize(){
		if (properties.getCrawler().equals("multithread")){
			this.crawler = new MultithreadCrawler(indexFileName);
		} else {
			this.crawler = new SimpleCrawler(indexFileName);
		}
		if (properties.getCrawl()) {
			crawler.crawl(startUrl);
			indexFlipper.flipIndex(indexFileName, flippedIndexFileName);
		}
	}


	@GetMapping("/search")
	public ResponseEntity<String> search(@RequestParam("q") String query, @RequestHeader("Accept") String accept) {
		List<String> searching = searcher.search(query, flippedIndexFileName);

		System.out.println("searching:");
		System.out.println(searching);
		// If there's a single URL in the search results, return only the URL without HTML
		if (searching.size() == 1) {
			HttpHeaders headerMap = new HttpHeaders();
			headerMap.put("Location", Collections.singletonList(searching.get(0)));
			return new ResponseEntity<>(headerMap, HttpStatus.FOUND);
		}

		// Build and return the HTML response with unique links
		StringBuilder result = new StringBuilder();
		Set<String> addedItems = new HashSet<>();

		for (String item : searching) {
			if (addedItems.add(item)) { // HashSet's add() method returns false if the item is already in the set
				result.append("<a href=\"").append(item).append("\" target=\"_blank\">").append(item).append("</a><br>");
			}
		}

		return new ResponseEntity<>(result.toString(), HttpStatus.OK);
	}

	@GetMapping("/lucky")
	public ResponseEntity<String> lucky(@RequestParam("q") String query, @RequestHeader("Accept") String accept){
		HttpHeaders headerMap = new HttpHeaders();
		if(accept.contains("application/json")){
			headerMap.put("Content-Type", Collections.singletonList("application/json"));
			return new ResponseEntity<>(headerMap, HttpStatus.OK);
		}
		List<String> searching = searcher.search(query, flippedIndexFileName);
		StringBuilder result = new StringBuilder();

		if (!searching.isEmpty()) {
			// Generate a random index within the range of the list size
			int randomIndex = new Random().nextInt(searching.size());

			// Get the random element and append it to the result
			String randomItem = searching.get(randomIndex);
			result.append(randomItem);
			result.append("<a href=\"").append(randomItem).append("\" target=\"_blank\">").append(randomItem).append("</a><br>");
		}
		headerMap.put("Location", Collections.singletonList(searching.get(0)));

		return new ResponseEntity<>(headerMap, HttpStatus.FOUND);

	}





}
