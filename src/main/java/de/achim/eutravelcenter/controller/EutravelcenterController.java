package de.achim.eutravelcenter.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.achim.eutravelcenter.dao.ConnectionRequestDAO;
import de.achim.eutravelcenter.dao.StationDAO;
import de.achim.eutravelcenter.dbahn.BahnRequestService;
import de.achim.eutravelcenter.utils.BahnUtils;
import de.achim.eutravelcenter.utils.ReadStationData;

@RestController
public class EutravelcenterController {


	private ReadStationData rsd; 
	
	
	private BahnRequestService brs;
	
	public EutravelcenterController(ReadStationData rsd, BahnRequestService brs ) {
		this.rsd = rsd;
		this.brs = brs;
	}
	
	@RequestMapping(value ="/hello", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String helloWorld(
			@RequestParam String name) {
		
		return "hello " + name;
	}
	
	@RequestMapping(value ="/stations", produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody List<String> getStationList(
			@RequestParam String name) {
		
		List<String> stationNames = new ArrayList<>();
		try {
			
			List<StationDAO> stationsList = rsd.readAllStationDataForEurope();
			Set<StationDAO> setEuropeAllList = stationsList
					.stream()
					.filter(s -> s.getName().toLowerCase().startsWith(name.toLowerCase()))
					.collect(Collectors.toSet());
			setEuropeAllList
					.forEach(s -> stationNames.add(s.getName()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stationNames;
	}
	
	
	@PostMapping(value = "/connections")
	public List<String> requestConnection(@RequestBody ConnectionRequestDAO connections){
		List<String> connenctionLinks = new ArrayList<>();
		
		try {
			StationDAO startStationDAO = rsd.readEuropeStationDataForStation(connections.getStartStation());
			StationDAO destinationStationDAO = rsd.readEuropeStationDataForStation(connections.getDestinationStation());
			String startTravelDate = BahnUtils.makeBahnDatum(connections.getTravelStartDate());
			String startTravelTime = connections.getTravelStartTime();
			long requestTimeAsUnixTS = BahnUtils.getUnixTimeStamp();
			String startX =  BahnUtils.formatXYCoordinates(startStationDAO.getLongitude());
			String startY =  BahnUtils.formatXYCoordinates(startStationDAO.getLatitude());
			String startStationName = startStationDAO.getName();
			//check format: 008011160
			String startStationID = startStationDAO.getUic();
			String destinationX =  BahnUtils.formatXYCoordinates(destinationStationDAO.getLongitude());
			String destinationY =  BahnUtils.formatXYCoordinates(destinationStationDAO.getLatitude());
			String destinationStationID = destinationStationDAO.getUic();
			String destinationStationName = destinationStationDAO.getName();
			//tariffClass 2 = tariffTravellerType_1="E"??
			String tariffClass = connections.getTariffClass();		
			String numberOfTravellers = connections.getNumberOfTravellers();
			connenctionLinks = brs.getConnectionsFromDeutschBahn(startStationName, startX, startY, startStationID, 
					destinationStationName, destinationX, destinationY, destinationStationID, 
					requestTimeAsUnixTS, startTravelTime, startTravelDate, numberOfTravellers, tariffClass);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		String connection1 = "href=\"https://reiseauskunft.bahn.de/bin/query.exe/dn?ld=4399&amp;protocol=https:&amp;seqnr=1&amp;"
//				+ "ident=7i.02888599.1645435080&amp;rt=1&amp;rememberSortType=minDeparture&amp;\"\n";
//		connenctionLinks.add(connection1);
		return connenctionLinks;
	}
}
