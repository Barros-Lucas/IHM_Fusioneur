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

public class IvyFusion {

	private Ivy bus;
	
	private int state;
	
	public Commande cmd;
	
	private int[] posTemp = new int[2];
	
	private String figureName;
	

	HashMap<Stroke, String> strokes = new HashMap<Stroke, String>();
	
	int i,j;
	
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
					System.out.println("CLick ");
					
					switch(state) {
					case 1:
						state = 3;
						posTemp[0] = Integer.parseInt(args[0]);
						posTemp[1] = Integer.parseInt(args[1]);
						cmd.setCmdOk(false);
						break;
					case 2:
						state = 1;
						cmd.setPos(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
						cmd.setCmdOk(true);
						break;
					case 3:
						state = 3;
						posTemp[0] = Integer.parseInt(args[0]);
						posTemp[1] = Integer.parseInt(args[1]);
						cmd.setCmdOk(false);
						break;
					
					//mode ClickColorPicker
					case 9:
						String cmdString = "Palette:CreerRectangle couleurFond=GREEN ";
						try {
							bus.sendMsg(cmdString);
						} catch (IvyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
						state = 4;
						figureName = args[0];
						cmd.setCmdOk(true);
						break;
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
						state = 1;
						cmd.setCmdOk(true);
						break;
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
							System.out.println("creer");
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
					System.out.println("deplace");
				}
				
			});
			
			bus.bindMsg("Geste_:Supprimer", new IvyMessageListener() {

				@Override
				public void receive(IvyClient client, String[] args) {	
					System.out.println("supp");
				}
				
			});
			
			bus.bindMsg("Voice_:(.*):(.*)", new IvyMessageListener() {

				@Override
				public void receive(IvyClient client, String[] args) {
					System.out.println("voice!");
					
					switch(args[0]) {
					case "Pos":
						
						//content of Event ParolePos
						switch(state) {
						case 1:
							state = 2;
							cmd.setCmdOk(false);
							break;
						case 2:
							state = 2;
							cmd.setCmdOk(false);
							break;
						case 3:
							state = 1;
							cmd.setPos(posTemp);
							cmd.setCmdOk(true);
						default:
							break;
						}
						
						break;
						
					case "Color":
						
						//content of Event ParoleCouleurSimple
						switch(state) {
						case 1:
							state = 1;
							cmd.setColor(args[1]);
							cmd.setCmdOk(true);
							break;
						default:
							break;
						}
						
						break;
						
					case "ColorPicker":
						switch(state) {
						case 4:
							System.out.println(figureName);
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
							System.out.println("wait");
							try {
								cmd.wait();
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							System.out.println("wait2");
							state = 1;
							cmd.setCmdOk(true);
							
							}
							break;
						default:
							break;
						}
						break;
					case "Execute":
						
						//content of Event ParoleCouleurChoix
						if(cmd.isCmdOk()) {
							state = 0;
							String cmdString = cmd.getCommandeFormat();
							
							try {
								System.out.println(cmdString);
								bus.sendMsg(cmdString);
								cmd.resetCommande();
							} catch (IvyException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else {
							System.out.println("cmd not valide");
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
	
	
//	public String getNameObjetClicked(String x, String y) {
//		ArrayList<String> names = new ArrayList<String>();
//		boolean[] getAllPoints = {false};
//		ObjetPicked objPick = new ObjetPicked();
//		try {
//			
//			
//			bus.sendMsg("Palette:TesterPoint x="+x+" y="+y);
//			bus.bindMsg("Palette:ResultatTesterPoint x=(.*) y=(.*) nom=(.*)", new IvyMessageListener() {
//
//				@Override
//				public void receive(IvyClient client, String[] args) {
//					
//					names.add(args[2]);
//
//				}
//				
//			});
//			
//			bus.bindMsg("Palette:FinTesterPoint x=(.*) y=(.*)", new IvyMessageListener() {
//
//				@Override
//				public void receive(IvyClient client, String[] args) {
//					
//					//une fois fini on get sa couleur
//					if
//					try {
//						bus.sendMsg("Palette:DemanderInfo nom="+names.get(names.size()-1));
//						bus.bindMsg("Palette:Info nom=(.*) x=(.*) y=(.*) longueur=(.*) hauteur=(.*) couleurFond=(.*) couleurContour=(.*)", new IvyMessageListener() {
//
//							@Override
//							public void receive(IvyClient client, String[] args) {
//								objPick.setCouleurFond(args[5]);
//								System.out.println(args[5]);
//								getAllPoints[0] = true;
//							}
//						});
//					} catch (IvyException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//
//				}
//				
//			});			
//		} catch (IvyException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//
//		if(names.isEmpty()) {
//			return "";
//		}else {
//			return names.get(0);
//
//		}
//		
//	}
//	
//
//	public String getColorOnClick(String x,String y) {
//		
//		
//		String nameObjectPick = getNameObjetClicked(x,y);
//		System.out.println(nameObjectPick);
//
//		
////		String nameObjectPick = getNameObjetClicked(x,y);
////		System.out.println("nameOut :"+nameObjectPick);
////		if(nameObjectPick.equals("")) {
////			return "";
////		}else {
////			ObjetPicked objPick = getInfoObjectPicked(nameObjectPick);
////			return objPick.getCouleurFond();
////
////		}
//
//		return "";
//		
//	}
	
	public static void main(String args[]) throws IvyException {

		new IvyFusion();  
		}

	


}
