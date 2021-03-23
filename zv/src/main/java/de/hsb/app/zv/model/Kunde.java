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
	private Anrede anrede;
	private String vorname;
	private String nachname;	
	
	@Temporal(TemporalType.DATE)
	private Date geburtsdatum;	
	private String telNummer;	
	private Adresse adresse;
	private Kreditkarte kreditkarte;

	public Kunde() {}
	public Kunde(Adresse adresse, Kreditkarte kreditkarte) {
		this.adresse = adresse;
		this.kreditkarte = kreditkarte;
	}
	public Kunde(Anrede anrede, String vorname, String nachname, Date geburtsdatum, String telNummer, Adresse adresse, Kreditkarte kreditkarte) {
		super();
		this.anrede = anrede;
		this.vorname = vorname;
		this.nachname = nachname;
		this.geburtsdatum = geburtsdatum;
		this.telNummer = telNummer;
		this.adresse = adresse;
		this.kreditkarte = kreditkarte;
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
	public UUID getkId() {
		return kId;
	}

	public void setkId(UUID kId) {
		this.kId = kId;
	}
	public Anrede getAnrede() {
		return anrede;
	}
	public void setAnrede(Anrede anrede) {
		this.anrede = anrede;
	}
	public Adresse getAdresse() {
		return adresse;
	}
	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}
	public Kreditkarte getKreditkarte() {
		return kreditkarte;
	}
	public void setKreditkarte(Kreditkarte kreditkarte) {
		this.kreditkarte = kreditkarte;
	}
	
	
	
	
	
}
