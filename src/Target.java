
public class Target {
	
	private CaseType type;
	private GameColor color;
	private int id;
	
	public Target(CaseType type, GameColor color) {
		this.type = type;
		this.color = color;
		
		id = color.ordinal() * GameColor.values().length + type.ordinal();
	}
	
	public CaseType getType() {
		return this.type;
	}
	
	public GameColor getColor() {
		return this.color;
	}
	
	public int getId() {
		return color.ordinal() * GameColor.values().length + type.ordinal();
	}
	
	@Override
	public String toString() {
		return type.name() + "--" + color.name() + "---" + id;
	}
}
