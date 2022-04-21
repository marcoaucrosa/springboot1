package br.com.marco.sw.planets.api.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.marco.sw.planets.api.models.Planet;
import br.com.marco.sw.planets.api.services.PlanetService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/planets")
public class PlanetController {
	//teste3
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PlanetService service;
	
	@ApiOperation(value="Lista todos os planetas")
	@GetMapping("/")
	public ResponseEntity<List<Planet>> findAll(@RequestParam(name = "name", required = false) String name) {
		List<Planet> planetList = new ArrayList<Planet>();
		if (name != null) {
			planetList = service.findByName(name);
		} else {
			planetList = service.findAll();
		}
		return ResponseEntity.ok().body(planetList);
	}
	
	@ApiOperation(value="Busca planeta por id")
	@GetMapping("/{id}")
	public ResponseEntity<Planet> findById(@PathVariable("id") String id) {
		logger.info("Buscando Planeta com ID: " + id);
		return ResponseEntity.ok().body(service.findById(id));
	}
	
	@ApiOperation(value="Cadastra planeta")
	@PostMapping("/")
	public ResponseEntity<Void> save(@Valid @RequestBody Planet planet) {
		logger.info("Salvando Planeta: " + "[ Nome: " + planet.getName() + " Clima: " + planet.getClimate() + " Terreno: " + planet.getTerrain() + " ]");
		planet = service.save(planet);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(planet.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@ApiOperation(value="Altera planeta")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Código inexistente")
	})
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable("id") String id, @Valid @RequestBody Planet planet) {
		logger.info("Atualizando Planeta: " + "[ ID: " + planet.getId() + " Nome: " + planet.getName() + " Clima: " + planet.getClimate() + " Terreno: " + planet.getTerrain() + " ]");
		planet.setId(id);
		service.update(planet);
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value="Remove planeta")
	@DeleteMapping("/{id}")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Código inexistente")
	})
	public ResponseEntity<Void> delete(@PathVariable("id") String id) {
		logger.info("Deletando Planeta com ID: " + id);
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value="Retorna planetas com paginação")
	@GetMapping("/page")
	public ResponseEntity<Page<Planet>> findPage(
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(name = "orderBy", defaultValue = "name" ) String orderBy,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {
		Page<Planet> planetList = service.findPage(page, linesPerPage, orderBy, direction);
		return ResponseEntity.ok().body(planetList);
	}
}
