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

public class IvyTest {

	private Ivy bus;
	
	private int state;
	
	private Stroke strokeTemp;
	

	HashMap<Stroke, String> strokes = new HashMap<Stroke, String>();
	
	int i,j;
	
	public IvyTest() throws IvyException {
		state = 0;
		i = 0;
		j = 0;
		
		
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
						j=0;
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
						j++;
						if(j==4) {
							saveLearn(strokes);
							System.exit(0); 
						}
						break;
					case 2:
						state = 0;
						System.out.println("relache aux coordonnées x:"+args[0]+" y:"+args[1]);
						strokeTemp.normalize();

						strokes.put(strokeTemp,getCmdName());
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
	
	public String getCmdName() {
		Scanner str = new Scanner(System.in);  // Create a Scanner object
	    System.out.println("Enter cmdName:");

	    String cmdName = str.nextLine();  // Read user input
	    return cmdName;
	}
	
	public void saveLearn(HashMap<Stroke, String> strokes) {
	       try {
	           FileOutputStream fileOutputStream = new FileOutputStream("saveLearningStrokes.txt");
	           ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

	           objectOutputStream.writeObject(strokes);

	           objectOutputStream.close();
	           fileOutputStream.close();
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	}
		
	public static void main(String args[]) throws IvyException {

		new IvyTest();  
		}

	


}
