import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class Player {
	private String pseudo;
	private int points;
	private EventListenerList listenerList;
	private ChangeEvent changeEvent;
	
	Player(String pseudo) {
		this.pseudo = pseudo;
		points = 0;
		listenerList = new EventListenerList();
	}
	
	public String getPseudo() {
		return pseudo;
	}
	
	public void setPoints(int points) {
		this.points = points;
		fireStateChanged();
	}
	
	public void addPoint() {
		this.points++;
		fireStateChanged();
	}
	
	public int getPoints() {
		return points;
	}
	
	public void addChangeListener(ChangeListener listener) {
		listenerList.add(ChangeListener.class, listener);
	}
	
	public void removeChangeListener(ChangeListener listener) {
		
		listenerList.remove(ChangeListener.class, listener);
	}
	
	protected void fireStateChanged() {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);
				}
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}
}
