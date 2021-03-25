package de.hsb.app.zv.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.LinkedList;

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
public class KundenHandler implements Serializable {
	private static final long serialVersionUID = 1L;
	private DataModel<Kunde> kunden;
	private Kunde merkeKunde = new Kunde();
	private DataModel<Benutzer> user;
	private Benutzer merkeUser = new Benutzer();

	@PersistenceContext(name = "zv-persistence-unit")
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

//		Anrede[] anreden = getAnredeValues();
//		Kreditkartentyp[] typen = getKreditkartentypValues();
//		Rolle[] rollen = getRolleValues();
		em.persist(new Kunde(Anrede.HERR, "Yevhenii", "Kapuler", new GregorianCalendar(1999, 3, 19).getTime(),
				"+491600000000", new LinkedList<Adresse>(
						Arrays.asList(new Adresse[] { new Adresse("An der Karlstadt 8", "27568 ", "Bremerhaven")})),
				 new Kreditkarte(Kreditkartentyp.MASTER, "1234567890", new GregorianCalendar(2025, 3, 19).getTime(), "Yevhenii Kapuler"),  new Benutzer("eugen", "123",
						Rolle.KUNDE)));
		em.persist(new Kunde(Anrede.HERR, "Daniel", "Keil", new GregorianCalendar(1998, 4, 20).getTime(),
				"+491600000001", new LinkedList<Adresse>(
						Arrays.asList(new Adresse[] { new Adresse("An der Karlstadt 9", "27568 ", "Bremerhaven")})),
				 new Kreditkarte(Kreditkartentyp.MASTER, "1234567891", new GregorianCalendar(2025, 3, 15).getTime(), "Daniel Keil"),  new Benutzer("keil", "123",
						Rolle.ADMIN)));	
		em.persist(new Kunde(Anrede.HERR, "Kajiman", "Chongbang", new GregorianCalendar(1994, 4, 20).getTime(),
				"+491600000003", new LinkedList<Adresse>(
						Arrays.asList(new Adresse[] { new Adresse("Friedrich-Eber-Strasse 50", "27570 ", "Bremerhaven")})),
				 new Kreditkarte(Kreditkartentyp.AMEX, "1234567892", new GregorianCalendar(2026, 3, 15).getTime(), "Daniel Keil"),  new Benutzer("chongbang", "123",
						Rolle.KUNDE)));
		em.persist(new Kunde(Anrede.HERR, "Malte", "Bothen", new GregorianCalendar(1999, 4, 21).getTime(),
				"+491600000004", new LinkedList<Adresse>(
						Arrays.asList(new Adresse[] { new Adresse("Friedrich-Eber-Strasse 51", "27570 ", "Bremerhaven")})),
				 new Kreditkarte(Kreditkartentyp.AMEX, "1234567893", new GregorianCalendar(2023, 3, 15).getTime(), "Daniel Keil"),  new Benutzer("bothen", "123",
						Rolle.KUNDE)));	
		kunden = new ListDataModel<>();
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
		user = new ListDataModel<>();
		user.setWrappedData(
				em.createNamedQuery("SelectAlready").setParameter(1, merkeUser.getUsername()).getResultList());
		if (!user.isRowAvailable()) {
			merkeKunde = new Kunde(new Benutzer(merkeUser.getUsername(), merkeUser.getPassword(), Rolle.KUNDE));
			return "kundenDaten";
		} else {
			return "registrieren";
		}
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

	public Benutzer getMerkeUser() {
		return merkeUser;
	}

	public void setMerkeUser(Benutzer merkeUser) {
		this.merkeUser = merkeUser;
	}

	public DataModel<Benutzer> getUser() {
		return user;
	}

	public void setUser(DataModel<Benutzer> user) {
		this.user = user;
	}

}
