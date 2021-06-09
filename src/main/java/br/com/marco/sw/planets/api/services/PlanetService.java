package br.com.marco.sw.planets.api.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.marco.sw.planets.api.models.Planet;

@Service
public interface PlanetService {
	
	Planet save(Planet planet);
	
	Planet update(Planet planet);
	
	List<Planet> findAll();
	
	List<Planet> findByName(String name);
	
	Planet findById(String id);
	
	void delete(String id);
	
	Page<Planet> findPage(Integer page, Integer linesPerPage, String orderBy, String direction);
}
