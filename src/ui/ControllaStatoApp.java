package ui;

import domain.AppManager;

public class ControllaStatoApp extends Thread
{
	int numeroTentativo;
	
	private final static int NUM_MAX_TENTATIVI = 5;
	@Override
	public void run()
	{
		while(true){
			while(!AppManager.dbConnesso()){
				AppManager.provaConnessione();
				numeroTentativo++;
				if(numeroTentativo == NUM_MAX_TENTATIVI){
					System.out.println("errore connessione");
				}
			}
			numeroTentativo=0;
			try
			{
				sleep(1000);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
