package de.achim.eutravelcenter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

import de.achim.eutravelcenter.controller.EutravelcenterController;

@SpringBootTest
class EutravelcenterApplicationTests {
	
	@Autowired
	private EutravelcenterController ec;
	

	@Test
	void contextLoads() {
		assertThat(ec).isNotNull();
	}

}
