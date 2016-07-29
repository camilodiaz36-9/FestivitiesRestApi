package com.prodigious.festivities.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.prodigious.festivities.domain.Festivity;
import com.prodigious.festivities.repository.FestivitiesRepository;

@RestController
public class FestivitiesController {
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
			new ClassPathResource("SpringConfig.xml").getPath());

	FestivitiesRepository festivitiesRepo = context.getBean(FestivitiesRepository.class);

	@RequestMapping(value = "/Festivities", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Festivity>> getAllFestivities() {
		List<Festivity> lf = (List<Festivity>) festivitiesRepo.findAll();
		if (lf.isEmpty()) {
			System.err.println("Error: List of Festivities not found -> getAllFestivities()");
			return new ResponseEntity<List<Festivity>>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<List<Festivity>>(lf, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/Festivities/name/{name}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Festivity>> getFestivityByName(@PathVariable("name") String name) {

		List<Festivity> f = festivitiesRepo.findByName(name);
		if (f == null) {
			System.err.println("Error: Festivity not found -> getFestivityByName()");
			return new ResponseEntity<List<Festivity>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Festivity>>(f, HttpStatus.OK);
	}

	@RequestMapping(value = "/Festivities/start/{start}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Festivity>> getFestivityByStartDate(@PathVariable("start") String start) {

		List<Festivity> f = festivitiesRepo.findByStart(start);
		if (f == null) {
			System.err.println("Error: Festivity not found -> getFestivityByStartDate()");
			return new ResponseEntity<List<Festivity>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Festivity>>(f, HttpStatus.OK);
	}

	@RequestMapping(value = "/Festivities/daterange/{start}/{end}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Festivity>> getFestivitiesByDateRange(@PathVariable("start") String start,
			@PathVariable("end") String end) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

		Date dateStart = new Date();
		Date dateEnd = new Date();
		try {
			dateStart = sdf.parse(start);
			dateEnd = sdf.parse(end);
		} catch (ParseException e) {
			System.err.println("Error: Wrong date format -> " + e.toString());
			return new ResponseEntity<List<Festivity>>(HttpStatus.BAD_REQUEST);
		}

		List<Festivity> f = new ArrayList<>();

		if (dateStart.before(dateEnd) || dateEnd.after(dateStart)) {
			System.out.println("OK. Let's fetch the data");
			f = festivitiesRepo.findByStartAndEnd(start, end);
		} else {
			System.err.println("Error: Inconsistent dates. The start date is greater " + "than the ending date.");
			return new ResponseEntity<List<Festivity>>(HttpStatus.BAD_REQUEST);
		}

		if (f.isEmpty()) {
			System.err.println("Error: List of Festivities not found -> getFestivitiesByDateRange()");
			return new ResponseEntity<List<Festivity>>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<List<Festivity>>(f, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/Festivities/place/{place}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Festivity>> getFestivityByPlace(@PathVariable("place") String place) {

		List<Festivity> f = festivitiesRepo.findByPlace(place);
		if (f == null) {
			System.err.println("Error: Festivity not found -> getFestivityByPlace()");
			return new ResponseEntity<List<Festivity>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Festivity>>(f, HttpStatus.OK);
	}

	@RequestMapping(value = "/Festivities/", method = RequestMethod.POST)
	public ResponseEntity<Void> createFestivity(@RequestBody Festivity f, UriComponentsBuilder ucBuilder) {

		List<Festivity> lf = (List<Festivity>) festivitiesRepo.findAll();

		for (Festivity fv : lf) {
			if (fv.equals(f)) {
				System.err.println("A Festivity with name " + f.getName() + " already exist");
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
		}

		festivitiesRepo.save(f);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/Festivities/{name}").buildAndExpand(f.getName()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/Festivities/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Festivity> updateFestivity(@PathVariable("id") String id, @RequestBody Festivity f) {

		Festivity currentFestivity = festivitiesRepo.findById(id);

		if (currentFestivity == null) {
			System.out.println("Festivity with id " + id + " not found");
			return new ResponseEntity<Festivity>(HttpStatus.NOT_FOUND);
		}

		currentFestivity.setName(f.getName());
		currentFestivity.setStart(f.getStart());
		currentFestivity.setEnd(f.getEnd());

		festivitiesRepo.save(currentFestivity);
		return new ResponseEntity<Festivity>(currentFestivity, HttpStatus.OK);
	}

}
