package de.hsb.app.zv.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
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
	private List<Reservierung> filteredReservierungen;
	private DataModel<Zimmer> searchedZimmer;
	private Zimmer merkeZimmer;
	private Kunde merkeKunde = new Kunde();
	private DataModel<Reservierung> reservierungen;
	private Reservierung reservierung = new Reservierung();

	private static final String FILENAME = "reservierung.pdf";
    private static final String CONTENT_TYPE = "application/pdf";
    private Date date = new Date();
	
	SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
	
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
		searchedZimmer = new ListDataModel<>();
		searchedZimmer.setWrappedData(em.createNamedQuery("SelectNull").getResultList());
		zimmer = new ListDataModel<>();
		zimmer.setWrappedData(em.createNamedQuery("SelectZimmer").getResultList());
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

	public String viewSearchedZimmer() {
		merkeZimmer = searchedZimmer.getRowData();
		return "detailansicht";
	}

	public String viewSearchedZimmer_buchen() {
		merkeZimmer = searchedZimmer.getRowData();
		return "detailansicht_buchen";
	}

	public ReservierungStatus[] getStatusValues() {
		return ReservierungStatus.values();
	}

	public String action() {
		reservierung = reservierungen.getRowData();
		return "action";
	}

	@Transactional
	public String saveAction() {
		reservierung = em.merge(reservierung);
		em.persist(reservierung);
		reservierungen.setWrappedData(em.createNamedQuery("SelectReservierung").getResultList());
		return "alleReservierungen";
	}

	public String cancelAction() {
		return "alleReservierungen";
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
		searchedZimmer = new ListDataModel<>();
		searchedZimmer.setWrappedData(query.getResultList());
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

	public String alleReservierung() {
		reservierungen = new ListDataModel<>();
		reservierungen.setWrappedData(em.createNamedQuery("SelectReservierung").getResultList());
		return "alleReservierungen";
	}

	public String alleZimmer() {
		zimmer = new ListDataModel<>();
		zimmer.setWrappedData(em.createNamedQuery("SelectZimmer").getResultList());
		return "admin_alleZimmer";
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
		return "admin_alleZimmer";
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
		Benutzer login = (Benutzer) session.getAttribute("login");
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		if (login != null) {
			if (login.getRole().equals(Rolle.ADMIN)) {
				context.redirect("adminSeite.xhtml");
			} else {
				context.redirect("kundenSeite.xhtml");
			}
		} else {
			context.redirect("login.xhtml");
		}
	}

	public void importToPdf() throws IOException {
		
		HttpSession session = SessionUtils.getSession();
		merkeKunde = (Kunde) session.getAttribute("kunde");
		UUID id = merkeKunde.getkId();
		Query query = em.createNamedQuery("SelectOnlyMy");
		query.setParameter(1, id);
		reservierungen = new ListDataModel<>();
		reservierungen.setWrappedData(query.getResultList());
		
		
		
		FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();
		
	    
	    PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage( page );
		
		PDFont fontR = PDType1Font.TIMES_ROMAN;
		PDFont fontB = PDType1Font.HELVETICA_BOLD;
		
		
		
		PDPageContentStream contentStream = new PDPageContentStream(document, page);
		
		contentStream.beginText();
		contentStream.setFont( fontR, 13 );
		contentStream.setLeading(13);
		contentStream.newLineAtOffset( 60, 660 );
		contentStream.showText(merkeKunde.getAnrede() + " " + merkeKunde.getVorname() + " " + merkeKunde.getNachname());
		
		contentStream.newLine();
		contentStream.showText(merkeKunde.getAdresse().getStrasse());
		
		contentStream.newLine();
		contentStream.showText(merkeKunde.getAdresse().getPlz() + " " + merkeKunde.getAdresse().getOrt());
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont(fontB, 10);
		contentStream.setLeading(13);
		contentStream.newLineAtOffset(400, 640);
		contentStream.showText("Kontakt:");

		contentStream.setFont(fontR, 10);
		contentStream.setNonStrokingColor(134, 136, 138);
		contentStream.newLine();
		contentStream.showText("An der Karlstadt 8");
		
		contentStream.newLine();
		contentStream.showText("27568 Bremerhaven");
		
		contentStream.newLine();
		contentStream.showText("Tel. + 49 6171 / 000001");


		contentStream.newLine();
		contentStream.showText("Fax. + 49 6171 / 000002");
		
		contentStream.newLine();
		contentStream.showText("mobil + 49 160 123 456 79");
		
		contentStream.setLeading(14);
		
		contentStream.setFont(fontR, 12);
		contentStream.setNonStrokingColor(0, 0, 0);
		contentStream.newLine();
		contentStream.showText("Datum:" + formatter.format(getDate()));
		
		contentStream.newLine();
		contentStream.showText("Rechnungs-Nr.: 000001" );
		
		contentStream.newLine();
		contentStream.showText("Kunden-Nr.: 0001");
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont( fontB, 20 );
		contentStream.setLeading(20);
		contentStream.newLineAtOffset( 60, 510 );
		contentStream.showText( "Ihre Reservierung" );
		
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont(fontR, 13);
		contentStream.setLeading(13);
		contentStream.newLineAtOffset( 60, 470 );
		contentStream.showText("Zimmertyp                    ");
		contentStream.showText("Von              ");
		contentStream.showText("Bis              ");
		contentStream.showText("Bettenanzahl            ");
		contentStream.newLine();
		for(Reservierung res: reservierungen) {
			contentStream.showText(res.getZimmer().getZimmerTyp() + "     ");
			contentStream.showText(res.getVon() + "     ");
			contentStream.showText(res.getBis() + "     ");
			contentStream.showText(res.getZimmer().getBetten() + "     ");
			contentStream.newLine();
		}
		
		contentStream.endText();
		
		
		contentStream.close();
		
		document.save( "reserv.pdf");
	
	    document.close();
	    
	 
	    
	    FileInputStream inStream = new FileInputStream("reserv.pdf");
	    
	    ec.responseReset(); 
	    ec.setResponseContentType(CONTENT_TYPE); 
	    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + FILENAME + "\""); 
	    
	    OutputStream output = ec.getResponseOutputStream();
	    
	    byte[] buffer = new byte[4096];
        int bytesRead = -1;
         
        while ((bytesRead = inStream.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }

        inStream.close();
	    fc.responseComplete(); 
		
		
		//System.out.print("import");
		
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

	public DataModel<Zimmer> getSearchedZimmer() {
		return searchedZimmer;
	}

	public void setSearchedZimmer(DataModel<Zimmer> searchedZimmer) {
		this.searchedZimmer = searchedZimmer;
	}

	public List<Reservierung> getFilteredReservierungen() {
		return filteredReservierungen;
	}

	public void setFilteredReservierungen(List<Reservierung> filteredReservierungen) {
		this.filteredReservierungen = filteredReservierungen;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
