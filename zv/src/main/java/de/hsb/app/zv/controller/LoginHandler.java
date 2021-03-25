package de.hsb.app.zv.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.LinkedList;

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
import javax.transaction.UserTransaction;

import de.hsb.app.zv.model.Adresse;
import de.hsb.app.zv.model.Anrede;
import de.hsb.app.zv.model.Benutzer;
import de.hsb.app.zv.model.Kreditkarte;
import de.hsb.app.zv.model.Kunde;
import de.hsb.app.zv.model.Mitarbeiter;
import de.hsb.app.zv.model.Position;
import de.hsb.app.zv.model.Rolle;

@Named("loginHandler")
@FacesConfig
@SessionScoped
public class LoginHandler implements Serializable{
	private static final long serialVersionUID = 1L;
	@PersistenceContext(name="zv-persistence-unit")
	private EntityManager em;
	@Resource
	private UserTransaction utx;
	private DataModel<Benutzer> loginUser;
	private DataModel<Mitarbeiter> loginMitarbeiter;
	private DataModel<Kunde> loginKunde;
	private Benutzer user = new Benutzer();
	
	@PostConstruct
	public void init() {
		try {
			utx.begin();
		} catch (NotSupportedException | SystemException e) {
			e.printStackTrace();
		}
			em.persist(new Kunde(Anrede.HERR,"Finn","Stein",new GregorianCalendar(1995,6,15).getTime(),"+07719807061324",new LinkedList<Adresse>(),new Kreditkarte(),new Benutzer("fstein","12345",Rolle.KUNDE)));
			em.persist(new Mitarbeiter(Position.MGR,"Homo","Habilis",new GregorianCalendar(1992,1,15).getTime(),new Benutzer("root","root",Rolle.ADMIN)));
		try {
			utx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			e.printStackTrace();
		}
	}

	public String login() {
		return "login";
	}
	
	public void logIn() throws IOException {
		loginUser = new ListDataModel<>();
		String username = user.getUsername();
		String password = user.getPassword();
		Query query = em.createNamedQuery("SelectLogin");
		query.setParameter(1,username);
		query.setParameter(2,password);
		loginUser.setWrappedData(query.getResultList());
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		if (loginUser.isRowAvailable()) {
			HttpSession session = SessionUtils.getSession();
			session.setAttribute("username", user.getUsername());
			session.setAttribute("login",loginUser.getRowData());
			Benutzer u = (Benutzer)session.getAttribute("login");
			if(u.getRole().equals(Rolle.ADMIN)) {			
				loginMitarbeiter = new ListDataModel<>();
				query = em.createNamedQuery("SessionMitarbeiter");
				query.setParameter(1,username);
				query.setParameter(2,password);
				loginMitarbeiter.setWrappedData(query.getResultList());
				session.setAttribute("mitarbeiter",loginMitarbeiter.getRowData());
				context.redirect("adminSeite.xhtml");
			}else {
				loginKunde = new ListDataModel<>();
				query = em.createNamedQuery("SessionKunde");
				query.setParameter(1,username);
				query.setParameter(2,password);
				loginKunde.setWrappedData(query.getResultList());
				session.setAttribute("kunde",loginKunde.getRowData());	
				context.redirect("kundenSeite.xhtml");
			}
		} else {
			context.redirect("login.xhtml");
		}
	}

	public void checkLoggedIn() throws IOException {
		HttpSession session = SessionUtils.getSession();
		Benutzer login = (Benutzer)session.getAttribute("login");
		if (login == null) {
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			context.redirect("login.xhtml");
		}
	}
	
	public void checkLogAdmin() throws IOException {
		HttpSession session = SessionUtils.getSession();
		Benutzer login = (Benutzer)session.getAttribute("login");
		if (login == null || !login.getRole().equals(Rolle.ADMIN)) {      
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			context.redirect("login.xhtml");
		}
	}

	public void checkLogKunde() throws IOException {
		HttpSession session = SessionUtils.getSession();
		Benutzer login = (Benutzer)session.getAttribute("login");
		if (login == null || !login.getRole().equals(Rolle.KUNDE)) {
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			context.redirect("login.xhtml");
		}
	}

	public void ifLoggedIn() throws IOException {
		HttpSession session = SessionUtils.getSession();
		Benutzer login = (Benutzer)session.getAttribute("login");
		if (login != null) {
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			if(login.getRole().equals(Rolle.ADMIN)) {
				context.redirect("adminSeite.xhtml");
			}else {
				context.redirect("kundenSeite.xhtml");
			}
		}
	}

	public void logOut() throws IOException{
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		context.redirect("login.xhtml");
	}
	
	public String hello() throws IOException {
		HttpSession session = SessionUtils.getSession();
		Benutzer login = (Benutzer)session.getAttribute("login");
		if (login != null) {
			if(login.getRole().equals(Rolle.ADMIN)) {
				Mitarbeiter m = (Mitarbeiter)session.getAttribute("mitarbeiter");
				return m.getVorname()+" "+m.getNachname();
			}else {
				Kunde k = (Kunde)session.getAttribute("kunde");
				return k.getVorname()+" "+k.getNachname();
			}
		}else {
			return "Please log in !";
		}
	}
		
	public DataModel<Benutzer> getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(DataModel<Benutzer> loginUser) {
		this.loginUser = loginUser;
	}

	public DataModel<Mitarbeiter> getLoginMitarbeiter() {
		return loginMitarbeiter;
	}

	public void setLoginMitarbeiter(DataModel<Mitarbeiter> loginMitarbeiter) {
		this.loginMitarbeiter = loginMitarbeiter;
	}

	public DataModel<Kunde> getLoginKunde() {
		return loginKunde;
	}

	public void setLoginKunde(DataModel<Kunde> loginKunde) {
		this.loginKunde = loginKunde;
	}

	public Benutzer getUser() {
		return user;
	}

	public void setUser(Benutzer user) {
		this.user = user;
	}
	
}
