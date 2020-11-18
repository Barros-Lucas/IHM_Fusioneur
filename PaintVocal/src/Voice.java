import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyApplicationListener;
import fr.dgac.ivy.IvyMessageListener;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;


public class Voice {

	
	private Ivy bus;
	
	private int state;
	
	public Voice() throws IvyException {
		
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

			

			
			
		} catch (IvyException ie){
			System.out.println("can't send my message on the bus");
		}
	}
	


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
