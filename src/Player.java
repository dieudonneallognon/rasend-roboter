public class Player {
	private String pseudo;
	private int points;
	
	Player(String pseudo) {
		this.pseudo = pseudo;
		points = 0;
	}
	
	public String getPseudo() {
		return pseudo;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}
	
	public void addPoint() {
		this.points++;
	}
	
	public int getPoints() {
		return points;
	}
}
