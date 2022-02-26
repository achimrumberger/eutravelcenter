package de.achim.eutravelcenter.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import de.achim.eutravelcenter.dao.StationDAO;

@Service
public class ReadStationData {
	
	public List<StationDAO> readUnicodeEuropeA11() throws Exception {
		
		String line;
      File sourceFile = ResourceUtils.getFile("classpath:static/stations.csv");
		List<StationDAO> list = new ArrayList<>();
//		geht auch
//       File sourceFile = new ClassPathResource("static/stations.csv").getFile();
		FileReader fr = new FileReader(sourceFile, StandardCharsets.ISO_8859_1);
		BufferedReader reader = new BufferedReader(fr);

		while ((line = reader.readLine()) != null) {
			String[] tokens = line.split(";");
			if(tokens.length==74) {
				StationDAO sd = new StationDAO(tokens);
				list.add(sd);
				
			}

		}

		reader.close();
		return list;
	
	}


}
