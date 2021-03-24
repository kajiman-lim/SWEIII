package de.hsb.app.zv.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.FacesConfig;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

import de.hsb.app.zv.model.Reservierung;
import de.hsb.app.zv.model.Zimmer;
import de.hsb.app.zv.model.ZimmerTyp;

@FacesConfig
@Named("zimmerHandler")
@SessionScoped
public class ZimmerHandler implements Serializable{
	private DataModel<Zimmer> zimmer;
	private List<Zimmer> filteredZimmer;
	private Zimmer merkeZimmer;
	
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
		ZimmerTyp[] zimmerTypen = getZimmerTypValues();
		em.persist(new Zimmer(21, 2, zimmerTypen[1], "Einfaches Doppelzimmer mit 2 Betten"));
		em.persist(new Zimmer(22, 1, zimmerTypen[0], "Einfaches Einzelzimmer"));
		em.persist(new Zimmer(23, 1, zimmerTypen[0], "Einfaches Einzelzimmer"));
		em.persist(new Zimmer(24, 1, zimmerTypen[1], "Einfaches Doppelzimmer mit Doppelbett"));
		em.persist(new Zimmer(25, 3, zimmerTypen[2], "Einfaches Familienzimmer mit einem Doppelbett und zwei Einzelbetten"));
		em.persist(new Zimmer(31, 2, zimmerTypen[4], "Luxuriöses Doppelzimmer mit 2 Betten"));
		em.persist(new Zimmer(32, 1, zimmerTypen[3], "Luxuriöses Einzelzimmer"));
		em.persist(new Zimmer(33, 1, zimmerTypen[3], "Luxuriöses Einzelzimmer mit Blick auf die braune Weser"));
		em.persist(new Zimmer(34, 2, zimmerTypen[4], "Luxuriöses Doppelzimmer mit Doppelbett und Blick auf die braune Weser"));
		em.persist(new Zimmer(41, 2, zimmerTypen[5], "Prunkvolle Präsidentensuite mit individueller Ausstattung"));
		
		zimmer = new ListDataModel<>();
		zimmer.setWrappedData(em.createNamedQuery("SelectZimmer").getResultList());
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
	public String viewZimmer() {
		merkeZimmer = zimmer.getRowData();
		return "detailansicht";
	}
	public String back() {
		return "alleZimmer";
	}
	public String reservieren() {
		Reservierung reservierung = new Reservierung(merkeZimmer);
		return "reservierungen";
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
	
}
