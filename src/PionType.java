import java.awt.Color;

public enum PionType {

	VERT(Color.GREEN),
	BLEU(Color.BLUE),
	ROUGE(Color.RED),
	JAUNE(Color.YELLOW);
	
	private Color color;
	
	PionType(Color color) {
		
		this.color = color;
	}
	
	public Color getColor() {
		return this.color;
	}
	
}
