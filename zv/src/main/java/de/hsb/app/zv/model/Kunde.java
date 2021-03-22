package de.hsb.app.zv.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.faces.annotation.FacesConfig;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@FacesConfig
@NamedQuery(name="SelectKunden", query="Select k from Kunde k")
@Entity
public class Kunde implements Serializable {
	
	@Id @GeneratedValue
	private UUID kId;
	private String Anrede;
	private String vorname;
	private String nachname;	
	
	@Temporal(TemporalType.DATE)
	private Date geburtsdatum;	
	private String telNummer;	
	private String strasse;	
	private String stadt;

	public Kunde() {
		
	}
	
	public Kunde(String anrede, String vorname, String nachname, Date geburtsdatum, String telNummer,
			String strasse, String stadt) {
		super();
		Anrede = anrede;
		this.vorname = vorname;
		this.nachname = nachname;
		this.geburtsdatum = geburtsdatum;
		this.telNummer = telNummer;
		this.strasse = strasse;
		this.stadt = stadt;
	}
	
	public String getAnrede() {
		return Anrede;
	}

	public void setAnrede(String anrede) {
		Anrede = anrede;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public Date getGeburtsdatum() {
		return geburtsdatum;
	}

	public void setGeburtsdatum(Date geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}

	public String getTelNummer() {
		return telNummer;
	}

	public void setTelNummer(String telNummer) {
		this.telNummer = telNummer;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getStadt() {
		return stadt;
	}

	public void setStadt(String stadt) {
		this.stadt = stadt;
	}

	public UUID getkId() {
		return kId;
	}

	public void setkId(UUID kId) {
		this.kId = kId;
	}
	
	
	
	
}
