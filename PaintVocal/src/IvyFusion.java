import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;



import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyApplicationListener;
import fr.dgac.ivy.IvyMessageListener;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;

import java.util.HashMap; 

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class IvyFusion {
	
    Timer timer = new Timer(true);

	private Ivy bus;
	
	private int state;
	
	public Commande cmd;
	
	private int[] posTemp = new int[2];
	
	private String figureName;
	
	//boolean check if object is already picked for deplacer cmd
	private boolean deplacerObjPick;
	//boolean check if Pos is already picked for deplacer cmd
	private boolean deplacerPos;
	

	HashMap<Stroke, String> strokes = new HashMap<Stroke, String>();
	
	int i,j;
	
	public void SendCmd(Commande cmd) {
		System.out.println("Envoie receive | "+cmd.getName());
		//content of Event ParoleCouleurChoix
		if(cmd.isCmdOk()) {
			System.out.println("Commande is ok");
			state = 0;
			String cmdString = cmd.getCommandeFormat();
			
			try {
				System.out.println(cmdString);
				bus.sendMsg(cmdString);
				cmd.resetCommande();
				System.out.println("Commande send");
			} catch (IvyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			cmd.resetCommande();
			System.out.println("cmd not valide");
			state = 0;
		}
	}
	
	public IvyFusion() throws IvyException {
		this.state = 0;
		
		figureName = "";
		
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

			bus.bindMsg("Palette:MouseClicked x=(.*) y=(.*)", new IvyMessageListener() {

				@Override
				public void receive(IvyClient client, String[] args) {
					
					switch(state) {
					case 1:
						System.out.println("Click triggered");
						state = 3;
						posTemp[0] = Integer.parseInt(args[0]);
						posTemp[1] = Integer.parseInt(args[1]);
						cmd.setCmdOk(false);
						break;
					case 2:
						System.out.println("Click triggered");
						state = 1;
						cmd.setPos(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
						cmd.setCmdOk(true);
						break;
					case 3:
						System.out.println("Click triggered");
						state = 3;
						posTemp[0] = Integer.parseInt(args[0]);
						posTemp[1] = Integer.parseInt(args[1]);
						cmd.setCmdOk(false);
						break;
					case 9:
						System.out.println("Click triggered");
						state = 10;
						cmd.setPos(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
						deplacerPos = true;
						if(deplacerObjPick) {
							cmd.setCmdOk(true);
						}else {
							cmd.setCmdOk(false);
						}
						break;
					case 10:
						System.out.println("Click triggered");
						//just update pos, unique update to do
						state = 10;
						cmd.setPos(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
						break;
					
					default:
						break;
				}

					
				}
				
			});
			
			
			bus.bindMsg("Palette:MouseEntered nom=(.*)", new IvyMessageListener() {

				@Override
				public void receive(IvyClient client, String[] args) {	
				
					switch(state) {
					case 1:
						System.out.println("Mouse on figure triggered");
						state = 4;
						figureName = args[0];
						cmd.setCmdOk(true);
						break;
					case 5:
						System.out.println("Mouse on figure triggered");
						state = 6;
						figureName = args[0];
						cmd.setCmdOk(false);
						break;
					case 10:
						System.out.println("Mouse on figure triggered");
						state = 6;
						figureName = args[0];
						cmd.setCmdOk(false);
						break;
					case 11:
						System.out.println("Mouse on figure triggered");
						state = 12;
						figureName = args[0];
						cmd.setCmdOk(false);
					default:
						break;
					}
					
				}
				
			});
			
			bus.bindMsg("Palette:MouseExited nom=(.*)", new IvyMessageListener() {

				@Override
				public void receive(IvyClient client, String[] args) {	
				
					switch(state) {
					case 4:
						System.out.println("Mouse out figure triggered");
						state = 1;
						cmd.setCmdOk(true);
						break;
					case 6:
						System.out.println("Mouse out figure triggered");
						state = 5;
						cmd.setCmdOk(false);
					case 12:
						System.out.println("Mouse out figure triggered");
						state = 11;
					default:
						break;
					}
					
				}
				
			});
			
			
			
			bus.bindMsg("Geste_:Creer_:(.*)", new IvyMessageListener() {

				@Override
				public void receive(IvyClient client, String[] args) {	
					
					
					switch(state) {
						case 0:
							System.out.println("Creer");
							timer.schedule(new TimerTask() {
								@Override
								public void run() {
									SendCmd(cmd);

								}
								
							}, 5000);
							state = 1;
							cmd.setName("Creer");
							cmd.setForme(args[0]);
							cmd.setCmdOk(true);
							break;
						default:
							break;
					}
					
				}
				
			});
			
			bus.bindMsg("Geste_:Deplacer", new IvyMessageListener() {

				@Override
				public void receive(IvyClient client, String[] args) {	
					switch(state) {
					case 0:
						System.out.println("Deplacer");
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								SendCmd(cmd);

							}
							
						}, 5000);
						state = 5;
						cmd.setName("Deplacer");
						deplacerObjPick = false;
						deplacerPos = false;
						cmd.setCmdOk(false);
						
						break;
					default:
						break;
					}
				}
				
			});
			
			bus.bindMsg("Geste_:Supprimer", new IvyMessageListener() {

				@Override
				public void receive(IvyClient client, String[] args) {	
					switch(state) {	
					case 0:
						System.out.println("Supprimer");
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								SendCmd(cmd);

							}
							
						}, 5000);
						
						state = 11;
						cmd.setName("Supprimer");
						cmd.setCmdOk(false);

					default:
						break;
					}
				}
				
			});
			
			bus.bindMsg("Voice_:(.*):(.*)", new IvyMessageListener() {

				@Override
				public void receive(IvyClient client, String[] args) {
					
					switch(args[0]) {
					case "Pos":
						
						//content of Event ParolePos
						switch(state) {
						case 1:
							System.out.println("Position Voice receive");
							state = 2;
							cmd.setCmdOk(false);
							break;
						case 2:
							System.out.println("Position Voice receive");
							state = 2;
							cmd.setCmdOk(false);
							break;
						case 3:
							System.out.println("Position Voice receive");
							state = 1;
							cmd.setPos(posTemp);
							cmd.setCmdOk(true);
						case 5:
							System.out.println("Position Voice receive");
							state = 9;
							cmd.setCmdOk(false);
							break;
						case 8:
							System.out.println("Position Voice receive");
							state = 9;
							cmd.setCmdOk(false);
							break;
						case 9:
							System.out.println("Position Voice receive");
							state = 9;
							cmd.setCmdOk(false);
							break;
						default:
							break;
						}
						
						break;
						
					case "Color":
						
						//content of Event ParoleCouleurSimple
						switch(state) {
						case 1:
							System.out.println("Color Voice receive");
							state = 1;
							cmd.setColor(args[1]);
							System.out.println(cmd.getColor());
							cmd.setCmdOk(true);
							break;
						default:
							break;
						}
						
						break;
						
					case "ColorPicker":
						switch(state) {
						case 4:
							System.out.println("ColorPicker Voice receive");
							synchronized (cmd) {
							try {
								
								bus.bindMsg("Palette:Info nom=(.*) x=(.*) y=(.*) longueur=(.*) hauteur=(.*) couleurFond=(.*) couleurContour=(.*)", new IvyMessageListener() {

									@Override
									public void receive(IvyClient client, String[] args) {	
										cmd.setColor(args[5]);
										System.out.println("notify");
										synchronized (cmd) {
											cmd.notify();
										}
									}
									
								});
								
								
								bus.sendMsg("Palette:DemanderInfo nom="+figureName);
								
								

															
							
							} catch (IvyException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							System.out.println("Wait for getting information ColorPicker");
							try {
								cmd.wait();
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							System.out.println("Wait for getting information ColorPicker");
							state = 1;
							cmd.setCmdOk(true);
							
							}
							break;
						default:
							break;
						}
						break;
						
					case "FormeObject":
						String forme = "";
						boolean formeOk = false;

						//content of Event Voice Forme object
						switch(state) {
						case 6:
							System.out.println("Forme Object Voice receive");
							//test object right object forme between selected and voice
							forme = args[1];
							
							formeOk = false;
							System.out.println(figureName);
							System.out.println(forme);
							System.out.println(figureName.contains("R"));
							System.out.println((forme.contains("R") || forme.contains("Object")));
							//actually have just 2 types,Rectangle and Ellipse
							if(figureName.contains("R") && (forme.contains("R") || forme.contains("Object"))) {
								formeOk = true;
							}
							if(figureName.contains("E")&& (forme.contains("E") || forme.contains("object"))) {
								formeOk = true;
							}
							
							if(formeOk) {
								state = 8;
								deplacerObjPick = true;
								cmd.setFigureName(figureName);
								if(deplacerPos) {
									cmd.setCmdOk(true);
								}else {
									cmd.setCmdOk(false);
								}
								System.out.println("Forme recognized and corresponding");
							}else {
								System.out.println("ERROR: object did not correspond with your voice signal");
								System.out.println("Deplacer canceled");
								state = 0;
								cmd.resetCommande();
								deplacerObjPick = false;
							}

							break;
						case 12:
							forme = args[1];
							formeOk = false;
							System.out.println(figureName);
							if(figureName.contains("R") && (forme.contains("R") || forme.contains("Object"))) {
								formeOk = true;
							}
							if(figureName.contains("E")&& (forme.contains("E") || forme.contains("object"))) {
								formeOk = true;
							}
							if(formeOk) {
								state = 13;
								cmd.setCmdOk(true);
								cmd.setFigureName(figureName);
								System.out.println("Forme recognized and corresponding");
							}else {
								state = 0;
								cmd.setCmdOk(false);
								cmd.resetCommande();
								System.out.println("ERROR: suppression canceled, object type not conforme with voice");
							}

						default:
							break;
						}
						
						break;
					case "Execute":
						System.out.println("Envoie receive");
						//content of Event ParoleCouleurChoix
						if(cmd.isCmdOk()) {
							System.out.println("Commande is ok");
							state = 0;
							String cmdString = cmd.getCommandeFormat();
							
							try {
								System.out.println(cmdString);
								bus.sendMsg(cmdString);
								cmd.resetCommande();
								System.out.println("Commande send");
							} catch (IvyException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else {
							cmd.resetCommande();
							System.out.println("cmd not valide");
							state = 0;
						}
						
						break;
						
					default:
						break;
					}
					
					
					

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
