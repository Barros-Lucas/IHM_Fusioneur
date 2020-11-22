import java.awt.Color;
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

			
			bus.bindMsg("sra5 Text=(.*) Confidence=(.*)", new IvyMessageListener() {
			
					@Override
					public void receive(IvyClient client, String[] args) {

						String texte = args[0];
						int confidence = Math.round((Float.parseFloat(args[1].replace(",", "."))*100));
						
						int confidenceMin = 60;
						
						if(confidence>=confidenceMin && !texte.contains(".")) {
							//ok
							
							String wordFamily = getFamilyWord(texte);
							String cleanWord = getCleanWord(texte);
							System.out.println("Voice ok : "+wordFamily+"/"+cleanWord);
							//send request to fusion
							try {
								bus.sendMsg("Voice_:"+wordFamily+":"+cleanWord);
							} catch (IvyException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							
						}else {
							//porly recognized
							System.out.println("Error: word porly recognized ");
						}
						
					}
				});
			
		} catch (IvyException ie){
			System.out.println("can't send my message on the bus");
		}
	}
		
	//return the family name of the word input as String. Data from grammar.grxml
	private String getFamilyWord(String texte) {
		String family = "";
		
		//Family from grammar.grxml
		switch(texte){
		
		//Position Family
		case "ici":
			family = "Pos";
			break;
		case "la":
			family = "Pos";
			break;
		case "a cette position":
			family = "Pos";
			break;
			
		//Color Family
		case "blanc":
			family = "Color";
			break;
		case "noir":
			family = "Color";
			break;
		case "rouge":
			family = "Color";
			break;
		case "vert":
			family = "Color";
			break;
		case "bleu":
			family = "Color";
			break;

			
		//Color picker Family
		case "de cette couleur":
			family = "ColorPicker";
			break;
			
		//execute Family
		case "envoie":
			family = "Execute";
			break;
			
			
		default:
			break;
	
		}
		return family;
	}
	
	private String getCleanWord(String texte) {
		String cleanWord = "";
		
		//Family from grammar.grxml
		switch(texte){
		
		//Position Family
		case "ici":
			cleanWord = "ici";
			break;
		case "la":
			cleanWord = "ici";
			break;
		case "a cette position":
			cleanWord = "ici";
			break;
			
		//Color Family
		case "blanc":
			cleanWord = "WHITE";
			break;
		case "noir":
			cleanWord = "BLACK";
			break;
		case "rouge":
			cleanWord = "RED";
			break;
		case "vert":
			cleanWord = "GREEN";
			break;
		case "bleu":
			cleanWord = "BLUE";
			break;
		default:
			break;
	
		}
			
		return cleanWord;
	}


	public static void main(String[] args) throws IvyException {


		new Voice();

	}

}
