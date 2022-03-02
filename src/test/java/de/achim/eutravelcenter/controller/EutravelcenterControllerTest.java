package de.achim.eutravelcenter.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.achim.eutravelcenter.dao.ConnectionRequestDAO;
import de.achim.eutravelcenter.dao.StationDAO;
import de.achim.eutravelcenter.dbahn.BahnRequestService;
import de.achim.eutravelcenter.utils.ReadStationData;


@WebMvcTest(controllers = EutravelcenterController.class)
public class EutravelcenterControllerTest {
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ReadStationData rsd; 

	@MockBean
	private BahnRequestService brs;

	
	//private EutravelcenterController ec = new EutravelcenterController(rsd, brs);


	@Test
	public void testHelloWord() throws Exception {
		System.out.println("-------");
		this.mockMvc.perform(get("/hello?name=Achim")) 

		// Ausgabe des Request zu Testzwecken
		.andDo(print()) 

		// Prüfung des HTTP-Statuscodes
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("hello Achim")));
	}
	
	@Test
	public void testGetStationList() throws Exception {
		List<StationDAO> list = new ArrayList<>();
		String[] testdate = {
				"6778", "Achim", "achim", "8013746", "53.015986","	9.030446","DE","Europe/Berlin", "","",
				"","","","","","","","","","",
				"","","","","","","","","","",
				"","","","","","","","","","",
				"","","","","","","","","","",
				"","","","","","","","","","",
				"","","","","","","","","","",
				"","","",""							
		};

		StationDAO dao = new StationDAO(testdate);
		list.add(dao);
		when(rsd.readAllStationDataForEurope()).thenReturn(list);

		this.mockMvc.perform(get("/stations?name=Achim")) 
		.andDo(print()) 
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("Achim")));
	}
	
	@Test
	public void testRequestConnection() throws Exception {

		ConnectionRequestDAO connectionDAO = new ConnectionRequestDAO("Kirchheim (Teck)", "Stuttgart Hbf", "12:00", "03/06/2022", "2", "1");
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson=ow.writeValueAsString(connectionDAO );
		String[] testdate = {
				"6778", "Achim", "achim", "8013746", "53.015986","	9.030446","DE","Europe/Berlin", "","",
				"","","","","","","","","","",
				"","","","","","","","","","",
				"","","","","","","","","","",
				"","","","","","","","","","",
				"","","","","","","","","","",
				"","","","","","","","","","",
				"","","",""							
		};

		StationDAO dao = new StationDAO(testdate);
		when(rsd.readEuropeStationDataForStation(anyString())).thenReturn(dao);
		
		List<String> result = new ArrayList<>();
		result.add("https://reiseauskunft.bahn.de/bin/query.exe/dn?ld=4395&protocol=https:&seqnr=1&ident=jt.02265895.1646239655&rt=1&rememberSortType=minDeparture&");
		
		
		when(brs.getConnectionsFromDeutschBahn(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString(), anyString(), anyString(), anyString()))
				.thenReturn(result);
		this.mockMvc.perform(post("/connections")
				.contentType(APPLICATION_JSON_UTF8)
				.content(requestJson))
		.andDo(print()) 
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("reiseauskunft")));
	}

}
