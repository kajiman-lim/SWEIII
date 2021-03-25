package de.hsb.app.zv.controller;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.FacesConfig;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

import de.hsb.app.zv.model.Benutzer;
import de.hsb.app.zv.model.Kunde;
import de.hsb.app.zv.model.Reservierung;
import de.hsb.app.zv.model.ReservierungStatus;
import de.hsb.app.zv.model.Rolle;
import de.hsb.app.zv.model.Zimmer;
import de.hsb.app.zv.model.ZimmerTyp;

@FacesConfig
@Named("zimmerHandler")
@SessionScoped
public class ZimmerHandler implements Serializable {
	private static final long serialVersionUID = 1L;
	private DataModel<Zimmer> zimmer;
	private List<Zimmer> filteredZimmer;
	private Zimmer merkeZimmer;
	private Kunde merkeKunde = new Kunde();
	private DataModel<Reservierung> reservierungen;
	private Reservierung reservierung = new Reservierung();

	@PersistenceContext(name = "zv-persistence-unit")
	private EntityManager em;
	@Resource
	private UserTransaction utx;

	@PostConstruct
	public void init() {
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			e.printStackTrace();
		}
		ZimmerTyp[] zimmerTypen = getZimmerTypValues();
		em.persist(new Zimmer(21, 2, zimmerTypen[1], "Einfaches Doppelzimmer mit 2 Betten"));
		em.persist(new Zimmer(22, 1, zimmerTypen[0], "Einfaches Einzelzimmer"));
		em.persist(new Zimmer(23, 1, zimmerTypen[0], "Einfaches Einzelzimmer"));
		em.persist(new Zimmer(24, 1, zimmerTypen[1], "Einfaches Doppelzimmer mit Doppelbett"));
		em.persist(new Zimmer(25, 3, zimmerTypen[2],
				"Einfaches Familienzimmer mit einem Doppelbett und zwei Einzelbetten"));
		em.persist(new Zimmer(31, 2, zimmerTypen[4], "Luxuriöses Doppelzimmer mit 2 Betten"));
		em.persist(new Zimmer(32, 1, zimmerTypen[3], "Luxuriöses Einzelzimmer"));
		em.persist(new Zimmer(33, 1, zimmerTypen[3], "Luxuriöses Einzelzimmer mit Blick auf die braune Weser"));
		em.persist(new Zimmer(34, 2, zimmerTypen[4],
				"Luxuriöses Doppelzimmer mit Doppelbett und Blick auf die braune Weser"));
		em.persist(new Zimmer(41, 2, zimmerTypen[5], "Prunkvolle Präsidentensuite mit individueller Ausstattung"));

		zimmer = new ListDataModel<>();
		zimmer.setWrappedData(em.createNamedQuery("SelectNull").getResultList());
		reservierungen = new ListDataModel<>();
		reservierungen.setWrappedData(em.createNamedQuery("SelectReservierung").getResultList());
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			e.printStackTrace();
		}

	}

	public String viewZimmer() {
		merkeZimmer = zimmer.getRowData();
		return "detailansicht";
	}

	@Transactional
	public String reservieren() {
		HttpSession session = SessionUtils.getSession();
		merkeKunde = (Kunde) session.getAttribute("kunde");
		Date currentDate = new Date();
		Date checkIn = reservierung.getVon();
		Date checkOut = reservierung.getBis();
		reservierung = new Reservierung(currentDate, checkIn, checkOut, merkeKunde, merkeZimmer,
				ReservierungStatus.RSRVD);
		reservierung = em.merge(reservierung);
		em.persist(reservierung);
		reservierungen.setWrappedData(em.createNamedQuery("SelectReservierung").getResultList());
		searchZimmer();
		return "kundenSeite.xhtml";
	}

	@Transactional
	public void searchZimmer() {
		Date checkIn = reservierung.getVon();
		Date checkOut = reservierung.getBis();
		Query query = em.createNamedQuery("SearchZimmer");
		query.setParameter(1, checkIn);
		query.setParameter(2, checkOut);
		zimmer = new ListDataModel<>();
		zimmer.setWrappedData(query.getResultList());
	}
	public String meineReservierung() {
		HttpSession session = SessionUtils.getSession();
		merkeKunde = (Kunde) session.getAttribute("kunde");
		UUID id = merkeKunde.getkId();
		Query query = em.createNamedQuery("SelectOnlyMy");
		query.setParameter(1, id);
		reservierungen = new ListDataModel<>();
		reservierungen.setWrappedData(query.getResultList());
		return "meineReservierung";
	}
	
	public String newZimmer() {
		merkeZimmer = new Zimmer();
		return "neuesZimmer";
	}

	@Transactional
	public String save() {
		merkeZimmer = em.merge(merkeZimmer);
		em.persist(merkeZimmer);
		zimmer.setWrappedData(em.createNamedQuery("SelectZimmer").getResultList());
		return "alleZimmer";
	}

	public String edit() {
		merkeZimmer = zimmer.getRowData();
		return "neuesZimmer";
	}

	@Transactional
	public String delete() {
		merkeZimmer = zimmer.getRowData();
		merkeZimmer = em.merge(merkeZimmer);
		em.remove(merkeZimmer);
		zimmer.setWrappedData(em.createNamedQuery("SelectZimmer").getResultList());
		return "admin_alleZimmer";
	}

	public void back() throws IOException {
		HttpSession session = SessionUtils.getSession();
		Benutzer login = (Benutzer)session.getAttribute("login");
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		if (login != null) {
			if(login.getRole().equals(Rolle.ADMIN)) {
				context.redirect("adminSeite.xhtml");
			}else {
				context.redirect("kundenSeite.xhtml");
			}
		}else {
			context.redirect("login.xhtml");
		}
	}

	public LocalDateTime today() {
		return LocalDateTime.now();
	}

	public LocalDateTime tomorrow() {
		return LocalDateTime.now().plusDays(1);
	}

	public ZimmerTyp[] getZimmerTypValues() {
		return ZimmerTyp.values();
	}

	public DataModel<Zimmer> getZimmer() {
		return zimmer;
	}

	public void setZimmer(DataModel<Zimmer> zimmer) {
		this.zimmer = zimmer;
	}

	public Zimmer getMerkeZimmer() {
		return merkeZimmer;
	}

	public void setMerkeZimmer(Zimmer merkeZimmer) {
		this.merkeZimmer = merkeZimmer;
	}

	public List<Zimmer> getFilteredZimmer() {
		return filteredZimmer;
	}

	public void setFilteredZimmer(List<Zimmer> filteredZimmer) {
		this.filteredZimmer = filteredZimmer;
	}

	public Kunde getMerkeKunde() {
		return merkeKunde;
	}

	public void setMerkeKunde(Kunde merkeKunde) {
		this.merkeKunde = merkeKunde;
	}

	public DataModel<Reservierung> getReservierungen() {
		return reservierungen;
	}

	public void setReservierungen(DataModel<Reservierung> reservierungen) {
		this.reservierungen = reservierungen;
	}

	public Reservierung getReservierung() {
		return reservierung;
	}

	public void setReservierung(Reservierung reservierung) {
		this.reservierung = reservierung;
	}

}
