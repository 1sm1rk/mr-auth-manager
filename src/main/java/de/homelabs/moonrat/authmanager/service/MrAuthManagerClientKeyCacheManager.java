package de.homelabs.moonrat.authmanager.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.homelabs.moonrat.authmanager.entity.Client;
import de.homelabs.moonrat.authmanager.entity.MrAuthManagerConfiguration;
import jakarta.annotation.PostConstruct;

@Service
public class MrAuthManagerClientKeyCacheManager {
	Logger log = LoggerFactory.getLogger(MrAuthManagerClientKeyCacheManager.class);
	private final ConcurrentHashMap<String, byte[]> clientWithKeyMap;
	private MrAuthManagerConfiguration authManagerConfiguration;
	
	@Autowired
	ClientRepository clientRepo;
	
	@Autowired
	ConfigurationRepository configRepo;
	
	public MrAuthManagerClientKeyCacheManager() {
		clientWithKeyMap = new ConcurrentHashMap<String, byte[]>(255);
	}

	@PostConstruct
	private void init() {
		reloadCache();
		reloadConfiguration();
	}
	
	/**
	 * setup or reload the in-memory client cache 
	 */
	public int reloadCache() {
		clientWithKeyMap.clear();
		List<Client> clients = clientRepo.findAll();
		log.info("reloading cache, "+clients.size()+" entries found");
		
		for (Client client : clients) {
			clientWithKeyMap.put(client.getClientId(), client.getClientKey());
		}
		return clients.size();
	}
	
	public boolean reloadConfiguration() {
		//TODO: find better ref than id = 1
		this.authManagerConfiguration = configRepo.getMrAuthManagerConfigurationById(1);
		return true;
	}
	
	/**
	 * get the client key 
	 * 
	 * @param clientId
	 * @return byte[] client key
	 */
	public byte[] getClientKeyByClientId(String clientId) {
		if (clientWithKeyMap.containsKey(clientId)) {
			return clientWithKeyMap.get(clientId);
		} else {
			return null;
		}
	}
	
	public MrAuthManagerConfiguration getConfiguration() {
		return this.authManagerConfiguration;
	}
}
