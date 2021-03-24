package de.hsb.app.zv.model;

public enum Anrede {
	HERR("Herr"), FRAU("Frau"), FIRMA("Firma");	
	private final String label;
	private Anrede(String label) {
		this.label = label;
	}
	public String getLabel() {
		return label;
	}
}
