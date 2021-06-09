package br.com.marco.sw.planets.api.service.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.marco.sw.planets.api.models.Planet;
import br.com.marco.sw.planets.api.repositories.PlanetRepository;
import br.com.marco.sw.planets.api.services.exceptions.ObjectNotFoundException;
import br.com.marco.sw.planets.api.services.impl.PlanetServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
class PlanetServiceImplTest {

	@Autowired
	private PlanetServiceImpl service;
	
	@MockBean
	private PlanetRepository repository;
	
	
	@BeforeEach
	public void setUp() {
		List<Planet> list1 = new ArrayList<Planet>();
		Planet planet = new Planet();
		planet.setId("60a91d03707d53605f1dc102");
		planet.setName("Tatooine");
		planet.setClimate("arid");
		planet.setTerrain("desert");
		list1.add(planet);
		
		Mockito.when(repository.findByName(planet.getName())).thenReturn(list1);
		
		List<Planet> list = new ArrayList<Planet>();
		Planet planet1 = new Planet();
		planet1.setId("60a91d03707d53605f1dc102");
		planet1.setName("Tatooine");
		planet1.setClimate("arid");
		planet1.setTerrain("desert");
		list.add(planet1);
		
		Planet planet2 = new Planet();
		planet2.setId("60a94f4168a765596e3c1bf8");
		planet2.setName("Alderaan");
		planet2.setClimate("temperate");
		planet2.setTerrain("grasslands, mountains");
		list.add(planet2);
		
		Mockito.when(repository.findAll()).thenReturn(list);
		
		Mockito.when(repository.findById(planet.getId())).thenReturn(java.util.Optional.of(planet));
	}

	@Test
	@DisplayName("Retorna uma lista com todos os planetas")
	void findAll_ReturnListPlanet_WhenSuccessful() {
		List<Planet> planets = service.findAll();
		assertEquals(2, planets.size());
	}

	@Test
	@DisplayName("Retorna uma lista de planetas que contém o nome")
	void findByName_ReturnListPlanet_WhenSuccessful() {
		List<Planet> planets = service.findByName("Tatooine");
		assertEquals("Tatooine", planets.get(0).getName());
	}

	@Test
	@DisplayName("Retorna o planeta com o id")
	void findById_ReturnPlanet_WhenSuccessful() {
		Planet planet = service.findById("60a91d03707d53605f1dc102");
		assertEquals("60a91d03707d53605f1dc102", planet.getId());
	}

	@Test
	@DisplayName("Lança exceção de objeto não encontrado ao tentar deletar um planeta com id inexistente")
	void deleteWithIdNotExists() {
		String id = "0a91d03707d53605f1dc102";
		assertThrows("Planeta não encontrado com ID: " + id + " Tipo: " + Planet.class.getName(),
				ObjectNotFoundException.class, () -> service.delete(id));
	}
}
