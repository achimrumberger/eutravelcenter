package de.achim.eutravelcenter.dbahn;

import org.springframework.beans.factory.annotation.Autowired;

import de.achim.eutravelcenter.dao.StationDAO;
import de.achim.eutravelcenter.utils.ReadStationData;

public class BahnRequestService {

	@Autowired
	private ReadStationData rsd;

	public StationDAO getDestinationData(String destination) throws Exception {

		return rsd.readUnicodeEuropeForStation(destination);

	}

	public StationDAO getStartPoointData(String startpoint) throws Exception {

		return rsd.readUnicodeEuropeForStation(startpoint);

	}
	


}
