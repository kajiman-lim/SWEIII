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

import de.hsb.app.zv.model.Adresse;
import de.hsb.app.zv.model.Anrede;
import de.hsb.app.zv.model.Kreditkartentyp;
import de.hsb.app.zv.model.Kunde;
import de.hsb.app.zv.model.Benutzer;
import de.hsb.app.zv.model.Kreditkarte;
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
	public String neu() {
		user = new ListDataModel<>();
		user.setWrappedData(
				em.createNamedQuery("SelectAlready").setParameter(1, merkeUser.getUsername()).getResultList());
		if (!user.isRowAvailable()) {
			merkeKunde = new Kunde(new Benutzer(merkeUser.getUsername(), merkeUser.getPassword(), Rolle.KUNDE), new Adresse(), new Kreditkarte());

			return "kundenDaten";
		} else {
			return "registrieren";
		}
	}

	@Transactional
	public String speichern() {
		merkeKunde = em.merge(merkeKunde);
		em.persist(merkeKunde);
		kunden.setWrappedData(em.createNamedQuery("SelectKunden").getResultList());
		return "kundenSeite";
	}

	public String edit() {
		HttpSession session = SessionUtils.getSession();
		merkeKunde = (Kunde) session.getAttribute("kunde");
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
