package br.com.marco.sw.planets.api.models;

import javax.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "planets")
public class Planet {
	
	@Id
	private String id;
	@NotEmpty(message="{name.notempty}")
	private String name;
	@NotEmpty(message="{climate.notempty}")
	private String climate;
	@NotEmpty(message="{terrain.notempty}")
	private String terrain;
	@Transient
	private int countFilms;
	
	public Planet() {
		
	}
	
	public Planet(String id, String name, String climate, String terrain) {
		this.id = id;
		this.name = name;
		this.climate = climate;
		this.terrain = terrain;
	}
	
	public Planet(Planet planet) {
		this.id = planet.id;
		this.name = planet.name;
		this.climate = planet.climate;
		this.terrain = planet.terrain;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClimate() {
		return climate;
	}
	public void setClimate(String climate) {
		this.climate = climate;
	}
	public String getTerrain() {
		return terrain;
	}
	public void setTerrain(String terrain) {
		this.terrain = terrain;
	}
	public int getCountFilms() {
		return countFilms;
	}
	public void setCountFilms(int countFilms) {
		this.countFilms = countFilms;
	}

	@Override
	public String toString() {
		return String.format(
				"Planet[id=%s, name='%s', climate='%s', terrain='%s']",
				id, name, climate, terrain);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Planet other = (Planet) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
