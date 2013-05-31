package com.gpxrecorder.model;

public class Route {

	public static final String ROUTE_ID = "routeId";
	
	private String name;
	private int id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Route(String name, int id) {
		super();
		this.name = name;
		this.id = id;
	}

}
