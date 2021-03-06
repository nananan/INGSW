package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.Fumetto;
import domain.Libreria;
import technicalService.GestoreDataBase;
import technicalService.TuplaFumetto;

public class PannelloCentrale extends JPanel
{
	File file;
	Image imageSfondo = null;
	
	private MyPanel panel;
	private static Libreria libreria = Libreria.getIstanza();
	
	HashMap<String,Fumetto> fumetti = new HashMap<>();
	HashMap<String,Fumetto> fumettiFiltrati = new HashMap<>();
	HashMap<String,Fumetto> fumettiCercati = new HashMap<>();
	ArrayList<BottoneFumetto> bottoniFumetti;
	
	private int indiceFumetti = 0;
	
	private ImageIcon imagePrev;
	private ImageIcon imageAvanti;
	private JButton bottoneAvantiFumetti;
	private JButton bottoneIndietroFumetti;
	
	int larghezza = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int altezza = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private Text textDiscover;
	
	private MyListener listener;
	private Text fumettiNonTrovati;
	
	private String arrayNumeriClassifica[];
	
	public PannelloCentrale(final MyPanel panel)
	{
		super();	
		setBackground(Color.GRAY);
		int larghezzaPannello = ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 3) - 40;
		setPreferredSize(new Dimension(larghezza-larghezzaPannello*2, altezza));
		this.setBorder(BorderFactory.createLineBorder(Color.black,1));
		setLayout(null);
		this.panel = panel;
		
		listener = new MyListener();
		
		bottoniFumetti = new ArrayList<>();
		GestoreDataBase.connetti();

		imagePrev = new ImageIcon("image/prev-icon.png");
		imageAvanti = new ImageIcon("image/next.png");
		
		bottoneAvantiFumetti = new JButton();
		setBottone(bottoneAvantiFumetti, imageAvanti);
		bottoneAvantiFumetti.setBounds((int)this.getPreferredSize().getWidth()-
				(int)bottoneAvantiFumetti.getPreferredSize().getWidth()/2-5, 
				(int) this.getPreferredSize().getHeight()/2, 30, 30);
		add(bottoneAvantiFumetti);
		
		bottoneAvantiFumetti.addActionListener(listener);
		
		bottoneIndietroFumetti = new JButton();
		setBottone(bottoneIndietroFumetti, imagePrev);
		bottoneIndietroFumetti.setBounds(15, (int)this.getPreferredSize().getHeight()/2, 30, 30);
		add(bottoneIndietroFumetti);
		
		bottoneIndietroFumetti.addActionListener(listener);
		
		arrayNumeriClassifica = new String[8];
		for (int i = 0; i < arrayNumeriClassifica.length; i++)
		{
			int indice = i+1;
			arrayNumeriClassifica[i] = "image/number"+indice+".png";
		}
		
	}
	
	private void setBottone(JButton bottone, ImageIcon imageIcon)
	{
		Image imageScaled = imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		imageIcon.setImage(imageScaled);
		
		bottone.setIcon(imageIcon);
		bottone.setPressedIcon(imageIcon);
		bottone.setBorderPainted(false);
		bottone.setFocusPainted(false);
		bottone.setContentAreaFilled(false);
		bottone.setBackground(this.getBackground());
	}
	
	public void setRicercaFiltri(ArrayList<String> filtri, int statoFumetto, int tipoFumetto)
	{
		textDiscover = new Text("Ricerca", 32, Color.DARK_GRAY);
		textDiscover.setBounds(10, 10, (int)textDiscover.getPreferredSize().getWidth(), 
				(int)textDiscover.getPreferredSize().getHeight());
		add(textDiscover);
		bottoniFumetti = new ArrayList<>();
		
//		if (filtri.size() == 0 && statoFumetto == 0 && tipoFumetto == 0)
//		{
//			panel.PremiPerDiscover();
//			return;
//		}
		libreria.caricaFumettiPerFiltri(filtri, statoFumetto, tipoFumetto);
		
		aggiungiFumettoAlPannello(libreria.fumettiFiltratiCorrente());
	}

	public void setTopRead()
	{
		textDiscover = new Text("Più Letti", 32, Color.DARK_GRAY);
		textDiscover.setBounds(10, 10, (int)textDiscover.getPreferredSize().getWidth(), 
				(int)textDiscover.getPreferredSize().getHeight());
		add(textDiscover);
		aggiungiFumettoClassificheAlPannello(libreria.caricaPiuLetti());
		
	}
	
	public void setTopRated()
	{
		rimuoviImmaginiPresenti();
		textDiscover = new Text("Più Votati", 32, Color.DARK_GRAY);
		textDiscover.setBounds(10, 10, (int)textDiscover.getPreferredSize().getWidth(), 
				(int)textDiscover.getPreferredSize().getHeight());
		add(textDiscover);
		aggiungiFumettoClassificheAlPannello(libreria.caricaPiuVotati());
		
	}
	
	public void setRicerca(String tipoDaCercare, String nomeDaCercare)
	{
		textDiscover = new Text("Ricerca", 32, Color.DARK_GRAY);
		textDiscover.setBounds(10, 10, (int)textDiscover.getPreferredSize().getWidth(), 
				(int)textDiscover.getPreferredSize().getHeight());
		add(textDiscover);
		
		bottoneIndietroFumetti.setVisible(false);
		bottoneAvantiFumetti.setVisible(false);
		
		if (tipoDaCercare.equals("Autore"))
			aggiungiFumettoAlPannello(libreria.caricaFumettiPerAutore(nomeDaCercare));
		else if (tipoDaCercare.equals("Artista"))
			aggiungiFumettoAlPannello(libreria.caricaFumettiPerArtista(nomeDaCercare));
		else if (tipoDaCercare.equals("Fumetto"))
			aggiungiFumettoAlPannello(libreria.caricaFumettiPerNome(nomeDaCercare));
	}
	
	public void setDiscover()
	{
		textDiscover = new Text("Scopri", 32, Color.DARK_GRAY);
		textDiscover.setBounds(10, 10, (int)textDiscover.getPreferredSize().getWidth(), 
				(int)textDiscover.getPreferredSize().getHeight());
		add(textDiscover);
		
		aggiungiFumettoAlPannello(libreria.fumettiCorrenti());
	}
	
	public void rimuoviImmaginiPresenti()
	{
		if (fumettiNonTrovati != null)
			remove(fumettiNonTrovati);
		for (BottoneFumetto bottoneFumetto : bottoniFumetti)
			remove(bottoneFumetto);
	}
	
	private void aggiungiStringaFumettoNonTrovato()
	{
		fumettiNonTrovati = new Text("Fumetti non trovati", 24, Color.WHITE);
		fumettiNonTrovati.setBounds(20, 20 + textDiscover.getY() + 
				(int)textDiscover.getPreferredSize().getHeight(), 
				(int)fumettiNonTrovati.getPreferredSize().getWidth(),
				(int)fumettiNonTrovati.getPreferredSize().getHeight());
		add(fumettiNonTrovati);
	}
	
	private void aggiungiFumettoAlPannello(final Fumetto[] fumettiDaAggiungere)
	{
		int j=0, i=0;
		indiceFumetti = 0;
		bottoniFumetti.clear();
		if (fumettiDaAggiungere.length == 0)
		{
			aggiungiStringaFumettoNonTrovato();
			return;
		}
		
		if (!libreria.haPrecedente())
			bottoneIndietroFumetti.setVisible(false);
		if (!libreria.haSuccessivo())
			bottoneAvantiFumetti.setVisible(false);
		
		for(int z = 0; z < fumettiDaAggiungere.length; z++, indiceFumetti++, j++)
		{
//			System.out.println("Pannello Centrale: "+indiceFumetti);
			if (fumettiDaAggiungere[z] != null)
			{
//				System.out.println(fumettiDaAggiungere[z].getNome());
				BottoneFumetto bottoneFumetto = new BottoneFumetto(
						fumettiDaAggiungere[z].getCopertina(), fumettiDaAggiungere[z], panel);
				
				bottoneFumetto.setPreferredSize(new Dimension(200,300));
				bottoniFumetti.add(bottoneFumetto);
				
				if (j == 0)
					bottoniFumetti.get(j).setBounds(53,10 + textDiscover.getX() +
							(int)textDiscover.getPreferredSize().getHeight(), 200,300);
				else
				{
					if (j % 4 == 0)
					{
						bottoniFumetti.get(j).setBounds(53,10 + 
								(int)bottoniFumetti.get(j-1).getPreferredSize().getHeight()+
								bottoniFumetti.get(j-1).getY(), 200,300);
						
						i += bottoniFumetti.get(j).getPreferredSize().getHeight()+10;
					}
					else
						bottoniFumetti.get(j).setBounds(10 + (int)bottoniFumetti.get(j-1).getPreferredSize().getWidth()+bottoniFumetti.get(j-1).getX(),10+textDiscover.getX() +
								(int)textDiscover.getPreferredSize().getHeight()+i, 200,300);
				}
				this.add(bottoniFumetti.get(indiceFumetti));
//				if (i > (int)getPreferredSize().getHeight())
//				{
//					setPreferredSize(new Dimension((int)getPreferredSize().getWidth(), (int)getPreferredSize().getHeight()+i+(int)bottoniFumetti.get(j).getPreferredSize().getHeight()));
//				}

			}
		}		
	}
	
	private void aggiungiFumettoClassificheAlPannello(final Fumetto[] fumettiDaAggiungere)
	{
		int j=0, i=0;
		if (!libreria.haPrecedente())
			bottoneIndietroFumetti.setVisible(false);
		if (!libreria.haSuccessivo())
			bottoneAvantiFumetti.setVisible(false);
		
		for(int z = 0; z < fumettiDaAggiungere.length; z++, indiceFumetti++, j++)
		{
//			System.out.println("Pannello Centrale: "+indiceFumetti);
			if (fumettiDaAggiungere[z] != null)
			{
//				System.out.println(fumettiDaAggiungere[z].getNome());
				BottoneFumetto bottoneFumetto = new BottoneFumetto(
						fumettiDaAggiungere[z].getCopertina(), fumettiDaAggiungere[z], panel);
				
				
				bottoneFumetto.setPreferredSize(new Dimension(200,300));
				bottoniFumetti.add(bottoneFumetto);
				
				if (j == 0)
					bottoniFumetti.get(j).setBounds(53,10 + textDiscover.getX() +
							(int)textDiscover.getPreferredSize().getHeight(), 200,300);
				else
				{
					if (j % 4 == 0)
					{
						bottoniFumetti.get(j).setBounds(53,10 + 
								(int)bottoniFumetti.get(j-1).getPreferredSize().getHeight()+
								bottoniFumetti.get(j-1).getY(), 200, 300);
						
						i += bottoniFumetti.get(j).getPreferredSize().getHeight()+10;
					}
					else
						bottoniFumetti.get(j).setBounds(10 + (int)bottoniFumetti.get(j-1).getPreferredSize().getWidth()+bottoniFumetti.get(j-1).getX(),10+textDiscover.getX() +
								(int)textDiscover.getPreferredSize().getHeight()+i, 200,300);
				}
				this.add(bottoniFumetti.get(indiceFumetti));
				bottoniFumetti.get(indiceFumetti).setLayout(null);
				
				ImageIcon numero = new ImageIcon(arrayNumeriClassifica[indiceFumetti]);
				Image imageScaled = numero.getImage().getScaledInstance(50,50, Image.SCALE_SMOOTH);
				numero.setImage(imageScaled);
				
				JLabel numeroClassifica = new JLabel(numero);
				numeroClassifica.setPreferredSize(new Dimension(50,50));
				numeroClassifica.setBounds((int)bottoniFumetti.get(indiceFumetti).getPreferredSize().getWidth() - (int)numeroClassifica.getPreferredSize().getWidth()
						- bottoniFumetti.get(indiceFumetti).getInsets().bottom,
						(int)bottoniFumetti.get(indiceFumetti).getPreferredSize().getHeight() - (int)numeroClassifica.getPreferredSize().getHeight()
						- bottoniFumetti.get(indiceFumetti).getInsets().bottom,
						(int)numeroClassifica.getPreferredSize().getWidth(), (int)numeroClassifica.getPreferredSize().getHeight());
				
				numeroClassifica.setOpaque(true);
				numeroClassifica.setBackground(Color.BLACK);
				bottoniFumetti.get(indiceFumetti).add(numeroClassifica);
			}
		}		
	}
	
	private class MyListener implements ActionListener 
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			Object source = e.getSource();
			if (source == bottoneAvantiFumetti)
			{
				GestoreDataBase.connetti();
				for (int i = 0; i < bottoniFumetti.size(); i++)
					remove(bottoniFumetti.get(i));
			
				libreria.fumettiSuccessivi();
				aggiungiFumettoAlPannello(libreria.fumettiCorrenti());
				bottoneIndietroFumetti.setVisible(true);
				repaint();
			}
			else if (source == bottoneIndietroFumetti)
			{
				for (int i = 0; i < bottoniFumetti.size(); i++)
					remove(bottoniFumetti.get(i));
				
				libreria.fumettiPrecedenti();
				aggiungiFumettoAlPannello(libreria.fumettiCorrenti());
				bottoneAvantiFumetti.setVisible(true);
				repaint();
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		for (BottoneFumetto bottoneFumetto : bottoniFumetti) 
		{
			g.drawImage(bottoneFumetto.getImageScaled(), 0,0, this);
		}
		super.paintComponent(g);
	}
}
