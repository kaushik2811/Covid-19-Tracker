package io.java.covid19.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class CoronaVirusDataService {

	private static String virusData = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
	private List<LocationStats> allstats = new ArrayList<LocationStats>();
	
	
	public List<LocationStats> getAllstats() {
		return allstats;
	}

	@PostConstruct
	@Scheduled(cron="* * 1 * * *")
	public void fetchVirusData() {
		 List<LocationStats> newStats = new ArrayList<LocationStats>();
		try {
			URL url = new URL(virusData);
			 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			 connection.setRequestMethod("GET");
			 BufferedReader in = new BufferedReader(new InputStreamReader( connection.getInputStream())); 
			 Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
			 for (CSVRecord record : records) {
			    LocationStats locationStats = new LocationStats();
			    locationStats.setState(record.get("Province/State"));
			    locationStats.setCountry(record.get("Country/Region"));
			    int latestCases = Integer.parseInt(record.get(record.size()-1));
			    int preDayCases = Integer.parseInt(record.get(record.size()-2));
				locationStats.setLatestTotalCases(latestCases);
				locationStats.setDiffFromPrevDay(latestCases-preDayCases);
			    System.out.println(locationStats);
			    newStats.add(locationStats);
			 }
			 this.allstats=newStats; 
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
