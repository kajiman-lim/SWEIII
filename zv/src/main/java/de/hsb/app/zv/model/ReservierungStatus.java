package de.hsb.app.zv.model;

public enum ReservierungStatus {
	RSRVD("Reserviert"), IN("In"), OUT("Out");
	private final String label;
	private ReservierungStatus(String label) {
		this.label = label;
	}
	public String getLabel() {
		return label;
	}
}
