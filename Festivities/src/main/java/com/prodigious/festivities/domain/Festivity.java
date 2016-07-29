package com.prodigious.festivities.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "festivities")
public class Festivity {
	
	@Id
	private String id;
	private String name;
	private String place;
	private String start;
	private String end;
	
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
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	
	@Override
	public String toString(){
		return "ID: " + id + " | Name: " + name + " | Place: " + place + " | Start: " + start + " | End: " + end;
		
	}
	
}