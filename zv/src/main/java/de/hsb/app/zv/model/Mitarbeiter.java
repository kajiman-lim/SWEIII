package de.hsb.app.zv.model;

import java.util.Date;
import java.util.UUID;

import javax.faces.annotation.FacesConfig;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@FacesConfig
@Entity
@NamedQuery(name="SelecMitarbeiter", query="Select m from Mitarbeiter m")
@NamedQuery(name = "SessionMitarbeiter", query = "SELECT m FROM Mitarbeiter m WHERE m.user.username = ?1 AND m.user.password = ?2")
public class Mitarbeiter {
	@Id @GeneratedValue
	private UUID id;
	private String vorname;
	private String nachname;
	private Date geburtsdatum;
	private Position post;
	@OneToOne(cascade=CascadeType.ALL)
	private Benutzer user;
	public Mitarbeiter() {}
	public Mitarbeiter(Position post,String vorname, String nachname, Date geburtsdatum, Benutzer user) {
		this.post = post;
		this.vorname = vorname;
		this.nachname = nachname;
		this.geburtsdatum = geburtsdatum;
		this.user = user;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
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
	public Position getPost() {
		return post;
	}
	public void setPost(Position post) {
		this.post = post;
	}
	
}
