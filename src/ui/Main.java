package ui;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFrame;

import technicalService.DataBase;


public class Main {

	public static void main(String[] args) throws IOException 
	{
		
		try {
			JFrame f = new FrameLogin();
			DataBase.connect();
//		while (!((FrameLogin) f).getPanel().pareCheHaInseritoTutto())
//		{
////			System.out.println("non devo fare niente");
//		}
//		f.setVisible(false);
			f = new MyFrame(((FrameLogin) f).getPanel().getTextName());
			f.repaint();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				DataBase.disconnect();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
