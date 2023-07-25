package com.retooling.pursalchi.service;

public enum ChickenOrigin {
	Bought("B"), Grown("G"), Load("L");
	
	private ChickenOrigin(String origin) {
		this.origin=origin;
	}
	
	public String getOrigin() {
		return origin;
	}
	
	private String origin;
	
}
