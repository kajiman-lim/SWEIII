package de.hsb.app.zv.model;

import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.FacesConfig;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@FacesConfig
@NamedQuery(name="SelectZimmer", query="Select z from Zimmer z")
//@NamedQuery(name = "SearchZimmer", query = "SELECT z FROM Zimmer z LEFT OUTER JOIN Reservierung r ON z.id=r.zimmer WHERE ((r.von not between ?1 and ?2) or r.von = null) AND ((r.bis not between ?1 and ?2) or r.bis = null)")
@NamedQuery(name = "SearchZimmer", query = "SELECT z FROM Zimmer z WHERE NOT EXISTS(SELECT r FROM Reservierung r WHERE r.zimmer=z.id AND ((r.von BETWEEN ?1 AND ?2) OR (r.bis BETWEEN ?1 AND ?2)))")
@NamedQuery(name="SelectNull", query="Select z FROM Zimmer z WHERE 0 != 0")
@Entity
@RequestScoped
public class Zimmer{
	@Id @GeneratedValue
	private UUID id;
//	@Column(unique=true)
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
