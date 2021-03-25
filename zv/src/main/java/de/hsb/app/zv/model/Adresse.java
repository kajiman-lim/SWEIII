package de.hsb.app.zv.model;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Size;

@Entity
@NamedQuery(name="SelectAdressen", query="Select a from Adresse a")
@RequestScoped
public class Adresse{
	@Id @GeneratedValue
	private Integer id;
	@Size(min=3, max=30)
	private String strasse;
	private String plz;
	@Size(min=3, max=30)
	private String ort;
	
	public Adresse() {}
	public Adresse(String strasse, String plz, String ort) {
		this.strasse = strasse;
		this.plz = plz;
		this.ort = ort;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}
	
}
