package de.hsb.app.zv.model;

public enum Position {
	MGR("Manager"),RCP("Receptionist");
	private final String label;
	private Position(String label) {
		this.label = label;
	}
	public String getLabel() {
		return label;
	}
}
