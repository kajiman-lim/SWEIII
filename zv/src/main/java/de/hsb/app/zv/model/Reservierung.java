package de.hsb.app.zv.model;

import java.io.Serializable;
import java.util.UUID;

import javax.faces.annotation.FacesConfig;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@FacesConfig
@NamedQuery(name="SelectReservierung", query="Select r from Reservierung r")
@Entity
public class Reservierung implements Serializable {
	@Id @GeneratedValue
	private UUID id;
	private Kunde kunde;
	private Zimmer zimmer;
	
	Reservierung(Zimmer zimmer) {
		this.zimmer=zimmer;
	}
}
