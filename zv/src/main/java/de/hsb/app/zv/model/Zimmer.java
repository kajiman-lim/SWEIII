package de.hsb.app.zv.model;

import java.io.Serializable;
import java.util.UUID;

import javax.faces.annotation.FacesConfig;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@FacesConfig
@NamedQuery(name="SelectZimmer", query="Select z from Zimmer z")
@Entity
public class Zimmer implements Serializable {
	@Id @GeneratedValue
	private UUID id;
	private int nummer;
	private int betten;
	private ZimmerTyp zimmerTyp;
	private String beschreibung;
	
	public Zimmer(){}
	public Zimmer(int nummer, int betten, ZimmerTyp zimmerTyp, String beschreibung) {
		this.nummer = nummer;
		this.betten = betten;
		this.zimmerTyp = zimmerTyp;
		this.beschreibung = beschreibung;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public int getNummer() {
		return nummer;
	}

	public void setNummer(int nummer) {
		this.nummer = nummer;
	}

	public int getBetten() {
		return betten;
	}

	public void setBetten(int betten) {
		this.betten = betten;
	}

	public ZimmerTyp getZimmerTyp() {
		return zimmerTyp;
	}

	public void setZimmerTyp(ZimmerTyp zimmerTyp) {
		this.zimmerTyp = zimmerTyp;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	
	
}
