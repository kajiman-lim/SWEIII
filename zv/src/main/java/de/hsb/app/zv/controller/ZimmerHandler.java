package de.hsb.app.zv.controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
import javax.transaction.UserTransaction;

import de.hsb.app.zv.model.Zimmer;
import de.hsb.app.zv.model.ZimmerTyp;

@Named("zimmerHandler")
public class ZimmerHandler {
	private DataModel zimmer;
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
		ZimmerTyp[] zimmerTypen = ZimmerTyp.getValues();
		em.persist(new Zimmer(21, 2, zimmerTypen[1], "Einfaches Doppelzimmer mit 2 Betten"));
		em.persist(new Zimmer(22, 1, zimmerTypen[0], "Einfaches Einzelzimmer"));
		em.persist(new Zimmer(23, 1, zimmerTypen[0], "Einfaches Einzelzimmer"));
		em.persist(new Zimmer(24, 1, zimmerTypen[1], "Einfaches Doppelzimmer mit Doppelbett"));
		em.persist(new Zimmer(31, 2, zimmerTypen[3], "Luxuriöses Doppelzimmer mit 2 Betten"));
		em.persist(new Zimmer(32, 1, zimmerTypen[2], "Luxuriöses Einzelzimmer"));
		em.persist(new Zimmer(33, 1, zimmerTypen[2], "Luxuriöses Einzelzimmer mit Blick auf die braune Weser"));
		em.persist(new Zimmer(34, 2, zimmerTypen[3], "Luxuriöses Doppelzimmer mit Doppelbett und Blick auf die braune Weser"));
		em.persist(new Zimmer(41, 2, zimmerTypen[4], "Prunkvolle Präsidentensuite mit individueller Ausstattung"));
		
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
	
	
	
	public String neu() {
		return "neuesZimmer";
	}
}
