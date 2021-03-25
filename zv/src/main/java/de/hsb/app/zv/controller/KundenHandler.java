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
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

import de.hsb.app.zv.model.Anrede;
import de.hsb.app.zv.model.Adresse;
import de.hsb.app.zv.model.Kreditkarte;
import de.hsb.app.zv.model.Kreditkartentyp;
import de.hsb.app.zv.model.Kunde;
import de.hsb.app.zv.model.Benutzer;
import de.hsb.app.zv.model.Rolle;

@FacesConfig
@Named("kundenHandler")
@SessionScoped
public class KundenHandler implements Serializable{
	private static final long serialVersionUID = 1L;
	private DataModel<Kunde> kunden;
	private Kunde merkeKunde= new Kunde();
	
	@PersistenceContext(name="zv-persistence-unit")
	private EntityManager em;
	@Resource
	private UserTransaction utx;

	
	@PostConstruct
	public void init() {
		try {
			utx.begin();
		} catch (NotSupportedException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		Anrede[] anreden = getAnredeValues();
		Kreditkartentyp[] typen = getKreditkartentypValues();
		Rolle[] rollen = getRolleValues();
		em.persist(new Kunde(anreden[0], "Yevhenii", "Kapuler", new GregorianCalendar(1999, 3, 19).getTime(), "+491600000000", new Adresse("Heider Ring 10", "2777", "Ganderkese"), /*new Kreditkarte(typen[0], "1234567890", "04/24", "Yevhenii Kapuler"),*/ new Benutzer("Eugen", "123", rollen[1])));
		em.persist(new Kunde(anreden[0], "Daniel", "Keil", new GregorianCalendar(1999, 12, 4).getTime(), "+490101010101", new Adresse("Otto-Suhr-Str. 30", "27578", "Bremerhaven"), /*new Kreditkarte(typen[1], "0101010101", "10/22", "Daniel Keil"),*/ new Benutzer("GucciDaniel", "1337", rollen[0])));
		em.persist(new Kunde(anreden[0], "Yevhenii", "Kapuler", new GregorianCalendar(1999, 3, 19).getTime(), "+491600000000", new Adresse("Heider Ring 10", "2777", "Ganderkese"), /*new Kreditkarte(typen[0], "1234567890", "04/24", "Yevhenii Kapuler"),*/ new Benutzer("Eugen2", "123", rollen[1])));
		em.persist(new Kunde(anreden[0], "Yevhenii", "Kapuler", new GregorianCalendar(1999, 3, 19).getTime(), "+491600000000", new Adresse("Heider Ring 10", "2777", "Ganderkese"), /*new Kreditkarte(typen[0], "1234567890", "04/24", "Yevhenii Kapuler"),*/ new Benutzer("Eugen3", "123", rollen[1])));
		
		kunden= new ListDataModel<>();
		kunden.setWrappedData(em.createNamedQuery("SelectKunden").getResultList());
		
		try {
			utx.commit();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (RollbackException e) {
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	
	@Transactional
	public String speichern() {
		merkeKunde = em.merge(merkeKunde);
		em.persist(merkeKunde);
		kunden.setWrappedData(em.createNamedQuery("SelectKunden").getResultList());
		return "kundenDaten";
	}
	public String edit() {
		HttpSession session = SessionUtils.getSession();
		merkeKunde = (Kunde) session.getAttribute("kunde");
		return "neuerKunde";
	}
	public String neu() {
		
		return "kundenDaten";
	}
	public String zurück() {
		return "kundenDaten";
	}
	public String editKreditkarte() {
		merkeKunde = kunden.getRowData();
		return "kreditkarte";
	}
	public String editAdresse() {
		merkeKunde = kunden.getRowData();
		return "adresse";
	}
	
	
	
	
	public Anrede[] getAnredeValues() {
		return Anrede.values();
	}
	public Kreditkartentyp[] getKreditkartentypValues() {
		return Kreditkartentyp.values();
	}
	public Rolle[] getRolleValues() {
		return Rolle.values();
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
