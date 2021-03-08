package de.hsb.app.zv.model;

import java.io.Serializable;

import javax.faces.annotation.FacesConfig;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

@FacesConfig
@NamedQuery(name="SelectZimmer", query="Select z from Zimmer z")
@Entity
public class Zimmer implements Serializable{
	private int nummer;
	private ZimmerTyp zimmerTyp;
}
