import java.awt.Color;

public class Case implements Comparable<Case>{
	private Board.Coord coord;
	
	private boolean topLimit;
	private boolean bottomLimit;
	private boolean leftLimit;
	private boolean rightLimit;
	
	private CaseType type;
	private GameColor color;
	
	private CaseState state;
	
	public Case() {
		state = CaseState.FREE;
		type = CaseType.VIDE;
		topLimit = false;
		bottomLimit = false;
		leftLimit = false;
		rightLimit = false;
	}
	
public Case(Board.Coord coord, GameColor color, CaseType type) {
		
		this.coord = coord;
		this.type = type;
		this.color = color;
		this.state = CaseState.FREE;
	}
	
	public Case(Board.Coord coord, GameColor color, CaseType type, boolean uLim, boolean bLim, boolean lLim, boolean rLim) {
		
		this.coord = coord;
		
		topLimit = uLim;
		bottomLimit = bLim;
		leftLimit = lLim;
		rightLimit = rLim;
		
		this.type = type;
		this.color = color;
		this.state = CaseState.FREE;
	}
	
	public boolean canCrossTop() {
		return !topLimit;
	}
	
	public boolean canCrossDown() {
		return !bottomLimit;
	}
	
	public boolean canCrossLeft() {
		return !leftLimit;
	}
	
	public boolean canCrossRight() {
		return !rightLimit;
	}
	
	
	public Board.Coord getCoord() {
		return this.coord;
	}
	
	public CaseType getType() {
		return this.type;
	}
	
	public GameColor getColor(){
		return this.color;
	}
	
	public CaseState getSate () {
		return this.state;
	}
	
	public boolean isFree() {
		return this.state == CaseState.FREE;
	}
	
	
	public void setType(CaseType type) {
		this.type = type;
	}
	
	public void setColor(GameColor color) {
		this.color = color;
	}
	
	
	public void setCoord(Board.Coord coord) {
		this.coord = coord; 
	}
	
	void setState(CaseState state) {
		this.state = state;
	}
	
	
	public void setTopBorder(boolean b) {
		this.topLimit = b;
	}
	
	public void setLeftBorder(boolean b) {
		this.leftLimit = b;
	}
	
	public void setRightBorder(boolean b) {
		this.rightLimit = b;
	}
	
	public void setBottomBorder(boolean b) {
		this.bottomLimit = b;
	}
	
	
	public boolean matchPion(PionType pionType) {
		boolean result = false;
		
		if (color != null) {			
			result = pionType.name().equals(color.name());
		}
		return result;
	}
	
	@Override
	public int compareTo(Case o) {
		return coord.compareTo(o.getCoord());
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof Case) {
			
			Case other = (Case) obj;
						
			return getCoord().equals(other.getCoord());
		}
				
		return false;
	}
	
	@Override
	public int hashCode() {
		return getCoord().hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = null;
		s += coord.toString() + " " + (!canCrossTop() ? 'h' : "");
		s += (!canCrossRight() ? 'r' : "");
		s += (!canCrossLeft() ? 'l' : "");
		s+= (!canCrossDown() ? 'b' : "");
		return s;
	}
}
