import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;


import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyApplicationListener;
import fr.dgac.ivy.IvyMessageListener;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;

import java.util.HashMap; 

import java.util.Scanner;

public class IvyFusion {

	private Ivy bus;
	
	private int state;
	
	public Commande cmd;
	

	HashMap<Stroke, String> strokes = new HashMap<Stroke, String>();
	
	int i,j;
	
	public IvyFusion() throws IvyException {
		this.state = 0;
		
		this.cmd = new Commande();
		
		// initialization, name and ready message
		bus = new Ivy("IvyFusion","Fusion_Ready", new IvyApplicationListener() {

			@Override
			public void connect(IvyClient client) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void disconnect(IvyClient client) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void die(IvyClient client, int id, String msgarg) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void directMessage(IvyClient client, int id, String msgarg) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		try {
			bus.start("localhost:2010");

			
			
			bus.bindMsg("Creer_:(.*)", new IvyMessageListener() {

				@Override
				public void receive(IvyClient client, String[] args) {	
					System.out.println(cmd.toString());
					System.out.println(args[0]);
//					int longueur = 50;
//					int hauteur = 25;
//					int x = 50;
//					int y = 50;
//					
//					bus.sendMsg("Palette:"+ "Creer"+objectName +" x="+x+" y="+y+" longueur="+longueur+" hauteur="+hauteur);
				}
				
			});
			
			bus.bindMsg("Deplacer", new IvyMessageListener() {

				@Override
				public void receive(IvyClient client, String[] args) {	
					System.out.println("deplace");
				}
				
			});
			
			bus.bindMsg("Supprimer", new IvyMessageListener() {

				@Override
				public void receive(IvyClient client, String[] args) {	
					System.out.println("supp");
				}
				
			});
			

			
		} catch (IvyException ie){
			System.out.println("can't send my message on the bus");
		}
	}
	

	
	public static void main(String args[]) throws IvyException {

		new IvyFusion();  
		}

	


}
