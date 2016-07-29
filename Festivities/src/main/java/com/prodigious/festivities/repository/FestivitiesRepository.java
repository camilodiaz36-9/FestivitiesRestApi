package com.prodigious.festivities.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.prodigious.festivities.domain.Festivity;

public interface FestivitiesRepository extends CrudRepository<Festivity,Long>{
	List<Festivity> findByName(String name);
	List<Festivity> findByStart(String start);
	List<Festivity> findByStartAndEnd(String start, String end);
	List<Festivity> findByPlace(String place);
	Festivity findById(String id);
}
