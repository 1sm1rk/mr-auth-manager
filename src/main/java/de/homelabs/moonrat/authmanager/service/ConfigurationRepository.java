package de.homelabs.moonrat.authmanager.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.homelabs.moonrat.authmanager.entity.MrAuthManagerConfiguration;

public interface ConfigurationRepository extends JpaRepository<MrAuthManagerConfiguration, Integer> {

	List<MrAuthManagerConfiguration> findAll();
	MrAuthManagerConfiguration getMrAuthManagerConfigurationById(Integer id);
	
}
