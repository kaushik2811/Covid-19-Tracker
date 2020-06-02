package io.java.covid19.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.java.covid19.service.CoronaVirusDataService;
import io.java.covid19.service.LocationStats;

@Controller
public class HomeController {
	
	@Autowired
	private CoronaVirusDataService coronaVirusDataService;
	
	@GetMapping("/")
	public String home(Model model) {
		List<LocationStats> allStats = coronaVirusDataService.getAllstats();
		model.addAttribute("locationStats",allStats);
		int totalCases = allStats.stream().mapToInt(stat->stat.getLatestTotalCases()).sum();
		int totalNewCases = allStats.stream().mapToInt(stat->stat.getDiffFromPrevDay()).sum();
		model.addAttribute("totalcases",totalCases);
		model.addAttribute("totalnewcases",totalNewCases);
		return "home";
	}
}
