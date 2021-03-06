package domain;

import java.awt.Image;
import java.sql.SQLException;


import technicalService.TuplaCapitolo;

public class Capitolo {
	
	private int numero;
	private String titolo;
	private String urlCapitolo;
	
	private Image[] pagine;
	private int numeroPagine;
	
	public Capitolo( TuplaCapitolo tuplaCapitolo) {
		
		numero = tuplaCapitolo.getNumero();
		titolo = tuplaCapitolo.getTitolo();
		urlCapitolo = tuplaCapitolo.getUrlPrimaPagina();
		
		numeroPagine = tuplaCapitolo.getNumeroPagine();
		pagine = new Image[numeroPagine];
		
	}
	
	public void setPagine(){
		
		pagine = new Image[numeroPagine];
	}
	public String getUrlCapitolo() {
		return urlCapitolo;
	}

	public Image[] getPagine() {
		return pagine;
	}

	public int getNumeroPagine() {
		return numeroPagine;
	}

	public int getNumero() {
		return numero;
	}
	public String getTitolo() {
		return titolo;
	}

}

