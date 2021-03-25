package de.hsb.app.zv.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.FacesConfig;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@FacesConfig
@NamedQuery(name="SelectKunden", query="Select k from Kunde k")
@NamedQuery(name = "SearchKunden", query = "SELECT k FROM Kunde k WHERE k.user.username = ?1 AND k.user.password = ?2")
@NamedQuery(name = "SessionKunde", query = "SELECT k FROM Kunde k WHERE k.user.username = ?1 AND k.user.password = ?2")
@Entity
@RequestScoped
public class Kunde {
	@Id @GeneratedValue
	private UUID kId;
	private Anrede anrede;
	private String vorname;
	private String nachname;	
	@Temporal(TemporalType.DATE)
	private Date geburtsdatum;	
	private String telNummer;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "kunde_id", nullable = true)
	private List<Adresse> adresse;
	@OneToOne(cascade=CascadeType.ALL)
	private Kreditkarte kreditkarte;
	@OneToOne(cascade=CascadeType.ALL)
	private Benutzer user; 
	
	
	public Kunde() {}
	public Kunde(List<Adresse> adresse, Kreditkarte kreditkarte) {
		this.adresse = adresse;
		this.kreditkarte = kreditkarte;
	}
	public Kunde(Anrede anrede, String vorname, String nachname, Date geburtsdatum, String telNummer, List<Adresse> adresse) {
		super();
		this.anrede = anrede;
		this.vorname = vorname;
		this.nachname = nachname;
		this.geburtsdatum = geburtsdatum;
		this.telNummer = telNummer;
		this.adresse = adresse;
	}
	
	public Kunde(Anrede anrede, String vorname, String nachname, Date geburtsdatum, String telNummer, List<Adresse> adresse,
			Kreditkarte kreditkarte, Benutzer user) {
		this.anrede = anrede;
		this.vorname = vorname;
		this.nachname = nachname;
		this.geburtsdatum = geburtsdatum;
		this.telNummer = telNummer;
		this.adresse = adresse;
		this.kreditkarte = kreditkarte;
		this.user = user;
	}
	public Kunde(Benutzer user) {
		this.user = user;
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

	public List<Adresse> getAdresse() {
		return adresse;
	}
	public void setAdresse(List<Adresse> adresse) {
		this.adresse = adresse;
	}
	public Kreditkarte getKreditkarte() {
		return kreditkarte;
	}
	public void setKreditkarte(Kreditkarte kreditkarte) {
		this.kreditkarte = kreditkarte;
	}
	public Benutzer getUser() {
		return user;
	}
	public void setUser(Benutzer user) {
		this.user = user;
	}
}
