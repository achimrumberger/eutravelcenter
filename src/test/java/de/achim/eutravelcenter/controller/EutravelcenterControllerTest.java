package de.achim.eutravelcenter.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import de.achim.eutravelcenter.dao.StationDAO;
import de.achim.eutravelcenter.utils.ReadStationData;

@WebMvcTest
public class EutravelcenterControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
//	@Autowired
//	private EutravelcenterController ec;
//	
	@MockBean
	private ReadStationData rsd; 
	
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
		when(rsd.readUnicodeEuropeA11()).thenReturn(list);
		
		this.mockMvc.perform(get("/stations?name=Achim")) 
       .andDo(print()) 
       .andExpect(status().isOk())
       .andExpect(content().string(containsString("Achim")));
	}

}
