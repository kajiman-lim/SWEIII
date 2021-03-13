package de.hsb.app.zv.model;

public enum ZimmerTyp {
	STANDARD_EINZEL("Standard Einzel"), STANDARD_DOPPEL("Standard Doppel"), STANDARD_VIERER("Standard Vierer"),
	PREMIUM_EINZEL("Premium Einzel"), PREMIUM_DOPPEL("Premium Doppel"), PRÄSIDENTENSUITE("Präsidentensuite");
	private final String label;
	private ZimmerTyp(String label){
		this.label = label;
	}
	public String getLabel() {
		return label;
	}
}
