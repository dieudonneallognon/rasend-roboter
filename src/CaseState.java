
public enum CaseState {
	OCCUPED(true),
	FREE(false);
	
	private boolean state;
	
	private CaseState(boolean state) {
		this.state = state;
	}
}
