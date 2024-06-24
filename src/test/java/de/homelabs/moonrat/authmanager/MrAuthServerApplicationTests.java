package de.homelabs.moonrat.authmanager;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.homelabs.moonrat.authmanager.entity.Client;
import de.homelabs.moonrat.authmanager.service.ClientRepository;

//@DataJpaTest
//@Transactional
@SpringBootTest()
class MrAuthServerApplicationTests {
	Logger logger = LoggerFactory.getLogger(MrAuthServerApplicationTests.class);
	
	@Autowired
	ClientRepository clientRepo;
	
/*	@Test 
	void createTestdata() {
		Client client = new Client();
		client.setClientAllowed(true);
		client.setClientDescription("testclient");
		client.setClientKey("döljgffdjgljdkg".getBytes());
		client.setClientType(1);
		clientRepo.save(client);
		
		List<Client> clients = clientRepo.findAll();
		for (Client client2 : clients) {
			logger.info("id:"+client2.getId());
		}
	}
*/
	@Test
	void testClients() {
		logger.info("testclients!");
		List<Client> clients = clientRepo.findAll();
		for (Client client : clients) {
			logger.info("id:"+client.getId());
		}
	}

}
