package de.hsb.app.zv.model;

import java.util.Date;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.FacesConfig;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@FacesConfig
@NamedQuery(name="SelectReservierung", query="Select r from Reservierung r")
@NamedQuery(name="SelectOnlyMy",query="Select r FROM Reservierung r WHERE r.kunde.kId=?1")
@Entity
@RequestScoped
public class Reservierung{
	@Id @GeneratedValue
	private UUID id;
	@Temporal(TemporalType.DATE)
	private Date von;
	@Temporal(TemporalType.DATE)
	private Date bis;
	@Temporal(TemporalType.DATE)
	private Date am;
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "kunde_id", nullable = true)
	private Kunde kunde;
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name = "zimmer_id", nullable = true)
	private Zimmer zimmer;
<<<<<<< HEAD


	private ReservierungStatus status;
	
	public Reservierung() {}
	public Reservierung(Date am,Date von, Date bis, Kunde kunde, Zimmer zimmer,ReservierungStatus status) {
		this.am = am;
		this.von = von;
		this.bis = bis;
		this.kunde = kunde;
		this.zimmer = zimmer;
		this.status = status;

=======
	private ReservierungStatus status;
	
	public Reservierung() {}
	public Reservierung(Date am,Date von, Date bis, Kunde kunde, Zimmer zimmer,ReservierungStatus status) {
		this.am = am;
		this.von = von;
		this.bis = bis;
		this.kunde = kunde;
		this.zimmer = zimmer;
		this.status = status;
>>>>>>> branch 'master' of https://github.com/kajiman-lim/SWEIII.git
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public Date getVon() {
		return von;
	}
	public void setVon(Date von) {
		this.von = von;
	}
	public Date getBis() {
		return bis;
	}
	public void setBis(Date bis) {
		this.bis = bis;
	}
	public Kunde getKunde() {
		return kunde;
	}
	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}
	public Zimmer getZimmer() {
		return zimmer;
	}
	public void setZimmer(Zimmer zimmer) {
		this.zimmer = zimmer;
	}
	public Date getAm() {
		return am;
	}
	public void setAm(Date am) {
		this.am = am;
	}
	public ReservierungStatus getStatus() {
		return status;
	}
	public void setStatus(ReservierungStatus status) {
		this.status = status;
	}
	
}
