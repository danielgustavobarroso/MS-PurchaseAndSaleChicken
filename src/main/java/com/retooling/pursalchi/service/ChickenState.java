package com.retooling.pursalchi.service;

public enum ChickenState {
	Available("A"), Discarded("D"), Sold("S"), Dead("E");
	
	private ChickenState(String state) {
		this.state=state;
	}
	
	public String getState() {
		return state;
	}
	
	private String state;
	
}
