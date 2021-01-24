
/**
 * @author Etu
 *
 */
public class Commande {

	private String name;
	private String forme;
	private int pos[] = new int[2];
	private String color;
	private boolean cmdOk;
	private String figureName;
	
	
	public Commande(String name, String forme, int[] pos, String color, boolean cmdOk, String figureName) {
		this.name = name;
		this.forme = forme;
		this.pos = pos;
		this.color = color;
		this.cmdOk = cmdOk;
		this.figureName = figureName;
	}
	
	public Commande() {
		this.name = "";
		this.forme = "";
		this.pos[0] = -1;
		this.pos[1] = -1;
		this.color = "";
		this.cmdOk = false;
		this.figureName = "";
	}
	
	public void resetCommande() {
		this.name = "";
		this.forme = "";
		this.pos[0] = -1;
		this.pos[1] = -1;
		this.color = "";
		this.cmdOk = false;
		this.figureName = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getForme() {
		return forme;
	}

	public void setForme(String forme) {
		this.forme = forme;
	}

	public int[] getPos() {
		return pos;
	}
	
	public int getPosX() {
		return pos[0];
	}
	
	public int getPosY() {
		return pos[1];
	}
	
	public void setPos(int[] pos) {
		this.pos = pos;
	}

	public void setPos(int x, int y) {
		this.pos[0] = x;
		this.pos[1] = y;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isCmdOk() {
		return cmdOk;
	}

	public void setCmdOk(boolean cmdOk) {
		this.cmdOk = cmdOk;
	}
	
	
	

	public String getFigureName() {
		return figureName;
	}

	public void setFigureName(String figureName) {
		this.figureName = figureName;
	}

	public String getCommandeFormat() {
		String cmdString = "Palette:";
		switch(this.getName()) {
		case "Creer":
			cmdString+="Creer"+this.getForme();
			if(this.getPosX() != -1 && this.getPosY() != -1) {
				cmdString +=" x="+this.getPos()[0] + " y="+this.getPos()[1];
			}
			
			if(!this.getColor().equals("")) {
				cmdString+=" couleurFond="+this.getColor();
			}
			break;
			
		case "Deplacer":
			cmdString+="DeplacerObjetAbsolu nom=" + this.figureName + " x=" + this.getPos()[0] + " y=" + this.getPos()[1];
			break;
		case "Supprimer":
			cmdString+="SupprimerObjet nom=" + this.figureName;
			break;
		default:
			break;
		}
		
	return cmdString;
	}
	
	
	
	
}
