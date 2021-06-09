package br.com.marco.sw.planets.api.services.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import br.com.marco.sw.planets.api.models.Planet;
import br.com.marco.sw.planets.api.repositories.PlanetRepository;
import br.com.marco.sw.planets.api.services.PlanetService;
import br.com.marco.sw.planets.api.services.exceptions.ObjectNotFoundException;
import br.com.marco.sw.planets.api.services.exceptions.PlanetNameIsExistsException;
import br.com.marco.sw.planets.api.services.utils.ServiceUtils;

@Service
public class PlanetServiceImpl implements PlanetService {

	@Autowired
	private PlanetRepository repostitory;

	@Autowired
	private MessageSource messageSource;

	@Value("${endpoint.api.starwars}")
	private String endpointApiSW;

	@Override
	public Planet save(Planet planet) {
		checkNameExistsToInsert(planet);
		return repostitory.save(planet);
	}

	@Override
	public List<Planet> findAll() {
		List<Planet> planets = repostitory.findAll();
		return planets.stream().map(p -> {
			Planet planet = new Planet(p);
			int countFilms = 0;
			try {
				String planetName;
				planetName = URLEncoder.encode(planet.getName(), "UTF-8");
				countFilms = countFilmsByPlanetName(planetName);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			planet.setCountFilms(countFilms);
			return planet;
			
		}).collect(Collectors.toList());
	}

	@Override
	public List<Planet> findByName(String name) {
		List<Planet> planets = repostitory.findByName(name);
		return planets.stream().map(p -> {
			Planet planet = new Planet(p);
			int countFilms = 0;
			try {
				String planetName;
				planetName = URLEncoder.encode(planet.getName(), "UTF-8");
				countFilms = countFilmsByPlanetName(planetName);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			planet.setCountFilms(countFilms);
			return planet;
		}).collect(Collectors.toList());
	}

	@Override
	public Planet findById(String id) {
		Optional<Planet> planet = repostitory.findById(id);
		if (!planet.isEmpty()) {
			planet.get().setCountFilms(countFilmsByPlanetName(planet.get().getName()));
		}
		return planet.orElseThrow(() -> new ObjectNotFoundException(
				"Planeta não encontrado com ID: " + id + " Tipo: " + Planet.class.getName()));
	}

	@Override
	public void delete(String id) {
		findById(id);
		repostitory.deleteById(id);
	}

	@Override
	public Planet update(Planet planet) {
		findById(planet.getId());
		checkNameExistsToUpdate(planet);
		return repostitory.save(planet);
	}

	@Override
	public Page<Planet> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repostitory.findAll(pageRequest);
	}

	private void checkNameExistsToInsert(Planet planet) {
		List<Planet> list = findByName(planet.getName());
		if (!list.isEmpty()) {
			throw new PlanetNameIsExistsException("name",
					messageSource.getMessage("name.alreadyexists", null, Locale.ROOT));
		}
	}

	private void checkNameExistsToUpdate(Planet planet) {
		List<Planet> list = findByName(planet.getName());
		Iterator<Planet> iterator = list.iterator();
		while (iterator.hasNext()) {
			Planet p = iterator.next();
			if (p.getName().equalsIgnoreCase(planet.getName()) && !(p.getId().equalsIgnoreCase(planet.getId()))) {
				throw new PlanetNameIsExistsException("name",
						messageSource.getMessage("name.alreadyexists", null, Locale.ROOT));
			}
		}
	}

	private int countFilmsByPlanetName(String name) {
		int count = 0;
		try {
			String url = endpointApiSW + name;
			JsonObject jsonObject = ServiceUtils.getBuilder(url);
			JsonArray results = jsonObject.getAsJsonArray("results");
			if (results.size() > 0) {
				JsonObject planet = results.get(0).getAsJsonObject();
				JsonArray films = planet.get("films").getAsJsonArray();
				count = films.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

}
