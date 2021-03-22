package de.hsb.app.zv.controller;

import java.io.Serializable;
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

import de.hsb.app.zv.model.Kunde;

@FacesConfig
@Named("kundenHandler")
@SessionScoped
public class KundenHandler implements Serializable{

	private DataModel<Kunde> kunden;
	private Kunde merkeKunde= new Kunde();
	
	@PersistenceContext(name = "kv-persistence-unit")
	private EntityManager em;
	
	@Resource
	private UserTransaction utx;

	
	@PostConstruct
	public void init() {
		em.persist(new Kunde("Herr", "Yevhenii", "Kapuler", new GregorianCalendar(1999, 3, 19).getTime(), "+491600000000", "Heider Ring 10", "27777 Ganderkesee"));
		kunden= new ListDataModel<>();
		kunden.setWrappedData(em.createNamedQuery("SelectKunden").getResultList());
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
