package de.hsb.app.zv.model;

import java.io.Serializable;
import java.util.Date;

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
	@Id @GeneratedValue
	private int id;
	private Kreditkartentyp typ;
	private String nummer;
	@Temporal(TemporalType.DATE)
	private Date gueltigBis;
	private String inhaber;
	
	public Kreditkarte() {};
	/*public Kreditkarte(Kreditkartentyp typ, String nummer, Date gueltigBis, String inhaber) {
		this.typ = typ;
		this.nummer = nummer;
		this.gueltigBis = gueltigBis;
		this.inhaber = inhaber;
	}*/
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Date getGueltigBis() {
		return gueltigBis;
	}
	public void setGueltigBis(Date gueltigBis) {
		this.gueltigBis = gueltigBis;
	}
	public String getInhaber() {
		return inhaber;
	}
	public void setInhaber(String inhaber) {
		this.inhaber = inhaber;
	}
	
}
