package ui;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import domain.Fumetto;
import domain.Volume;

public class BottoneCapitolo extends MyButton
{
	private Volume volume;
	private int numeroCapitolo;
	
	public BottoneCapitolo(String nome, int larghezza, Color color, final Volume volume,
			final Image immagineCopertinaFumetto, final int numeroCapitolo, final MyPanel panel,
			final Fumetto fumetto)
	{
		super(nome, larghezza, color);
		
		this.volume = volume;
		this.numeroCapitolo = numeroCapitolo;
		
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				super.mouseReleased(e);
				
				System.out.println("premo bottone");
				panel.PremiPerCapitolo(volume, fumetto, numeroCapitolo, immagineCopertinaFumetto);
			}
//			@Override
//			public void mouseEntered(MouseEvent e)
//			{
//				super.mouseEntered(e);
//				System.out.println(e.getX());
//				System.out.println(e.getY());
//			}
		});
		
	}
	
	public Volume getVolume()
	{
		return volume;
	}

	public int getNumeroCapitolo()
	{
		return numeroCapitolo;
	}
	
//	@Override
//	public void paintComponents(Graphics g)
//	{
//		super.paintComponents(g);
//	}
	
}