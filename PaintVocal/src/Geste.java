import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;


import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyApplicationListener;
import fr.dgac.ivy.IvyMessageListener;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class Geste {

	private Ivy bus;
	
	private int state;
	
	private Stroke strokeTemp;
	

	HashMap<Stroke, String> strokes = new HashMap<Stroke, String>();
	
	
	
	int i;
	
	public Geste() throws IvyException {
		state = 0;
		i = 0;

		strokes = deserializable();
		
		
		// initialization, name and ready message
		bus = new Ivy("Test","Test Ready", new IvyApplicationListener() {

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

			
			
			bus.bindMsg("Palette:MousePressed x=(.*) y=(.*)", new IvyMessageListener() {

				@Override
				public void receive(IvyClient client, String[] args) {
					switch(state) {
					case 0:
						state = 1;
						System.out.println("Clic aux coordonnées x:"+args[0]+" y:"+args[1]);			
						strokeTemp= new Stroke();
						break;
					default:
						break;
					};		
				}
				
			});
			
			bus.bindMsg("Palette:MouseDragged x=(.*) y=(.*)", new IvyMessageListener() {

				@Override
				public void receive(IvyClient client, String[] args) {
					switch(state) {
					case 0:
						break;
					case 1:
						state = 2;
						System.out.println("drag aux coordonnées x:"+args[0]+" y:"+args[1]);
						strokeTemp.addPoint(Integer.parseInt(args[0]), Integer.parseInt(args[1]));						
						
						break;
					case 2:
						state = 2;
						System.out.println("drag aux coordonnées x:"+args[0]+" y:"+args[1]);
						strokeTemp.addPoint(Integer.parseInt(args[0]), Integer.parseInt(args[1]));						
					
						
						break;
					default:
						break;
					};		
				}
				
			});
			
			bus.bindMsg("Palette:MouseReleased x=(.*) y=(.*)", new IvyMessageListener() {

				@Override
				public void receive(IvyClient client, String[] args) {
					switch(state) {
					case 0:
						break;
					case 1:
						state = 0;
						System.out.println("relache sans rien faire");
						strokeTemp.init();

						break;
					case 2:
						state = 0;
						System.out.println("relache aux coordonnées x:"+args[0]+" y:"+args[1]);
						strokeTemp.normalize();

						
						executeCmd(getObjectName(strokeTemp));
						i++;
					
						
						break;
					
					
					default:
						break;
						
					};		
				}




				
			});
			
			
		} catch (IvyException ie){
			System.out.println("can't send my message on the bus");
		}
	}
	
	private HashMap<Stroke, String> deserializable() {
		HashMap<Stroke, String> stro = new HashMap<Stroke, String>();
		try {
            FileInputStream fileInputStream = new FileInputStream("saveLearningStrokes.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

           stro = (HashMap<Stroke, String>) objectInputStream.readObject();
            

            fileInputStream.close();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
		return stro;
	}

	public String getObjectName(Stroke strokeTemp) {

		double minimal = Double.MAX_VALUE;
		String cmd = "";
        for (Entry<Stroke, String> mapentry : strokes.entrySet()) {
        	double dist = mapentry.getKey().distance(strokeTemp);
           if(dist<minimal) {
        	   minimal = dist;
        	   cmd = mapentry.getValue();
           }
        }
        return cmd;
		
	}
	
	private void executeCmd(String objectName) {
		System.out.println(objectName);
		
		try {
			switch(objectName) {
			case "Rectangle":
				bus.sendMsg("Creer_:"+objectName);
				break;
			case "Ellipse":
				bus.sendMsg("Creer_:"+objectName);
				break;
			case "Deplacer":
				bus.sendMsg("Deplacer");
				break;
			case "Supprimer":
				bus.sendMsg("Supprimer");
				break;
			default:
				break;
			}

			
			
		} catch (IvyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	public static void main(String args[]) throws IvyException {

		new Geste();  
		}

	


}
