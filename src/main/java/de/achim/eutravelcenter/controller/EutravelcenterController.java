package de.achim.eutravelcenter.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.achim.eutravelcenter.dao.StationDAO;
import de.achim.eutravelcenter.utils.ReadStationData;

@RestController
public class EutravelcenterController {

	@Autowired
	private ReadStationData rsd; 
	
	public EutravelcenterController() {
	//	this.rsd = rsd;
	}
	
	@GetMapping("/hello")
	public String helloWorld(
			@RequestParam String name) {
		return "hello " + name;
	}
	
	@GetMapping("/stations")
	public List<String> getStationList(
			@RequestParam String name) {
		
		List<String> stationNames = new ArrayList<>();
		try {
			
			List<StationDAO> stationsList = rsd.readUnicodeEuropeA11();
			Set<StationDAO> setEuropeAllList = stationsList
					.stream()
					.filter(s -> s.getName().startsWith(name))
					.collect(Collectors.toSet());
			setEuropeAllList
					.forEach(s -> stationNames.add(s.getName()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stationNames;
	}
}
