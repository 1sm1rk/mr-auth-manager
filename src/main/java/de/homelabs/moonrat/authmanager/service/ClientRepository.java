package de.homelabs.moonrat.authmanager.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.homelabs.moonrat.authmanager.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {

	List<Client> findAll();
}
