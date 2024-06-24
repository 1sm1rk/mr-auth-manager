package de.homelabs.moonrat.authmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.homelabs.moonrat.authmanager.entity.Client;
import de.homelabs.moonrat.authmanager.service.ClientRepository;

@RestController
public class AuthManagerController {

	@Autowired
	ClientRepository clientRepo;
	
	@GetMapping("/auth/version")
	public String getVersion() {
		System.out.println("startup, test");
		return "0.1";
	}
	
	@GetMapping("/auth/clients")
	public List<Client> getAllClients() {
		return clientRepo.findAll();
	}
}
