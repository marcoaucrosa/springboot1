package br.com.marco.sw.planets.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.marco.sw.planets.api.models.Planet;

@Repository
public interface PlanetRepository extends MongoRepository<Planet, String> {

	@Query("{'name': { $regex: '?0', $options: 'i' } }")
	List<Planet> findByName(String name);
	
	Optional<Planet> findById(String id);
	
	void deleteById(String id);
	
}
