package de.hsb.app.zv.controller;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.FacesConfig;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@FacesConfig
@Named("navigator")
@RequestScoped
public class PageNavigator {
	public void login() throws IOException {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		context.redirect("login.xhtml");
	}
}
