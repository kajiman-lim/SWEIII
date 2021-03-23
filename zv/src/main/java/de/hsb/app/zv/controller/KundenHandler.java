package de.hsb.app.zv.controller;

import java.io.Serializable;
import java.text.ParseException;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.FacesConfig;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import de.hsb.app.zv.model.Anrede;
import de.hsb.app.zv.model.Adresse;
import de.hsb.app.zv.model.Kreditkarte;
import de.hsb.app.zv.model.Kreditkartentyp;
import de.hsb.app.zv.model.Kunde;

@FacesConfig
@Named("kundenHandler")
@SessionScoped
public class KundenHandler implements Serializable{

	private DataModel<Kunde> kunden;
	private Kunde merkeKunde= new Kunde();
	
	@PersistenceContext(name = "zv-persistence-unit")
	private EntityManager em;
	
	@Resource
	private UserTransaction utx;

	
	@PostConstruct
	public void init() {
		Anrede[] anreden = getAnredeValues();
		Kreditkartentyp[] typen = getKreditkartentypValues();
		em.persist(new Kunde(anreden[0], "Yevhenii", "Kapuler", new GregorianCalendar(1999, 3, 19).getTime(), "+491600000000", new Adresse("Heider Ring 10", "2777", "Ganderkese"), new Kreditkarte(typen[0], "1234567890", "04/24", "Yevhenii Kapuler")));
		em.persist(new Kunde(anreden[0], "Daniel", "Keil", new GregorianCalendar(1999, 12, 4).getTime(), "+490101010101", new Adresse("Otto-Suhr-Str. 30", "27578", "Bremerhaven"), new Kreditkarte(typen[1], "0101010101", "10/22", "Daniel Keil")));
		em.persist(new Kunde(anreden[0], "Yevhenii", "Kapuler", new GregorianCalendar(1999, 3, 19).getTime(), "+491600000000", new Adresse("Heider Ring 10", "2777", "Ganderkese"), new Kreditkarte(typen[0], "1234567890", "04/24", "Yevhenii Kapuler")));
		em.persist(new Kunde(anreden[0], "Yevhenii", "Kapuler", new GregorianCalendar(1999, 3, 19).getTime(), "+491600000000", new Adresse("Heider Ring 10", "2777", "Ganderkese"), new Kreditkarte(typen[0], "1234567890", "04/24", "Yevhenii Kapuler")));
		kunden= new ListDataModel<>();
		kunden.setWrappedData(em.createNamedQuery("SelectKunden").getResultList());
	}
	
	
	
	public Anrede[] getAnredeValues() {
		return Anrede.values();
	}
	public Kreditkartentyp[] getKreditkartentypValues() {
		return Kreditkartentyp.values();
	}
	public DataModel<Kunde> getKunden() {
		return kunden;
	}

	public void setKunden(DataModel<Kunde> kunden) {
		this.kunden = kunden;
	}

	public Kunde getMerkeKunde() {
		return merkeKunde;
	}

	public void setMerkeKunde(Kunde merkeKunde) {
		this.merkeKunde = merkeKunde;
	}

}
