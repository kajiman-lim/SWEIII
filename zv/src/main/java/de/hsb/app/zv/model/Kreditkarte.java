package de.hsb.app.zv.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQuery(name="SelectKreditkarten", query="Select k from Kreditkarte k")
@RequestScoped
public class Kreditkarte implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue
	private UUID id;
	private Kreditkartentyp typ;
	private String nummer;
	private String gueltigBis;
	private String inhaber;
	
	public Kreditkarte() {};
	public Kreditkarte(Kreditkartentyp typ, String nummer, String gueltigBis, String inhaber) {
		this.typ = typ;
		this.nummer = nummer;
		this.gueltigBis = gueltigBis;
		this.inhaber = inhaber;
	}
	public Kreditkartentyp getTyp() {
		return typ;
	}
	public void setTyp(Kreditkartentyp typ) {
		this.typ = typ;
	}
	public String getNummer() {
		return nummer;
	}
	public void setNummer(String nummer) {
		this.nummer = nummer;
	}
	public String getGueltigBis() {
		return gueltigBis;
	}
	public void setGueltigBis(String gueltigBis) {
		this.gueltigBis = gueltigBis;
	}
	public String getInhaber() {
		return inhaber;
	}
	public void setInhaber(String inhaber) {
		this.inhaber = inhaber;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
}
