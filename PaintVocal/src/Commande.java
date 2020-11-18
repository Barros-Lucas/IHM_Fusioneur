
public class Commande {

	private String action;
	private String object;
	private double x,y;
	private String color;
	
	private int tmpParole;
	private double tmpX,tmpY;
	
	
	public Commande(String action, String object, double x, double y, String color, int tmpParole, double tmpX, double tmpY) {
		this.action = action;
		this.object = object;
		this.x = x;
		this.y = y;
		this.color = color;
		this.tmpParole = tmpParole;
		this.tmpX = tmpX;
		this.tmpY = tmpY;
	}

	public Commande() {
		this.action = "";
		this.object = "";
		this.x = 0;
		this.y = 0;
		this.color = "";
		this.tmpParole = 500;
		this.tmpX = 0;
		this.tmpY = 0;
	}
	
	

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public double getTmpX() {
		return tmpX;
	}

	public void setTmpX(double tmpX) {
		this.tmpX = tmpX;
	}

	public double getTmpY() {
		return tmpY;
	}

	public void setTmpY(double tmpY) {
		this.tmpY = tmpY;
	}

	public int getTmpParole() {
		return tmpParole;
	}

	@Override
	public String toString() {
		return action + "_" + object + "" + x + "," + y + "_" + color;
	}
	
	
	
	
}
