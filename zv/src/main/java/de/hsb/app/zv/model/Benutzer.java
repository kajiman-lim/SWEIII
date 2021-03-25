package de.hsb.app.zv.model;

import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.FacesConfig;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@FacesConfig
@NamedQuery(name="SelectBenutzer",query="SELECT b FROM Benutzer b")
@NamedQuery(name = "SelectLogin", query = "SELECT b FROM Benutzer b WHERE b.username = ?1 AND b.password = ?2")
@NamedQuery(name = "SelectAlready", query = "SELECT b FROM Benutzer b WHERE b.username = ?1")
@RequestScoped
@Entity
public class Benutzer {
	@Id @GeneratedValue
	private UUID id;
	@Column(unique=true)
	private String username;
	private String password;
	private Rolle role;
	public Benutzer() {}
	public Benutzer(String username, String password, Rolle role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Rolle getRole() {
		return role;
	}
	public void setRole(Rolle role) {
		this.role = role;
	}
}
