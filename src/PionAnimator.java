import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;


public class PionAnimator {

	private static final int ANIMATION_DELAY = 1;
	private static int pxStep = 1;

	private PionComponent pion;
	private StepMaker step;

	private EventListenerList listenerList = new EventListenerList();
	private ChangeEvent changeEvent;
	private PropertyChangeSupport pcs;

	public static final String PROP_ANIMATION_ENDED = "animationEnded";

	public PionAnimator(PionComponent pc, StepMaker step) {

		this.pion = pc;
		this.step = step;
		pcs =  new PropertyChangeSupport(this);
	}

	public void setStep(StepMaker step) {
		this.step = step;
	}

	public void animate() {

		Thread animThread = new Thread(new Animator());
		animThread.start();
	}

	private class Animator implements Runnable{		
		@Override
		public void run() {

			synchronized (step) {

				Point nextPos = pion.positionOnBoard(pion.getModel().getCoord());

				while (! pion.getLocation().equals(nextPos)) {

					calculatePxStep(pion.getLocation(), nextPos);
					step.move(pion);
					
					try {
						SwingUtilities.invokeAndWait( new Runnable() {
							@Override
							public void run() {							
								fireStateChanged();
							}
						});
					} catch (InvocationTargetException | InterruptedException e1) {
						e1.printStackTrace();
					} 
				}				
				pcs.firePropertyChange(PROP_ANIMATION_ENDED, false, true);
			}	 
		}
	}

	private void calculatePxStep(Point actualPos, Point nextPos) {

		int src, dest;

		if (actualPos.x != nextPos.x) {
			src = actualPos.x;
			dest = nextPos.x;
		} else {
			src = actualPos.y;
			dest = nextPos.y;
		}

		int distance = Math.abs(Math.abs(dest) - Math.abs(src)); 

		pxStep = (distance / 20 > 0) 
				? (distance / 20) 
				: (distance / 5 > 0)
				? distance / 5 : 1; 
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

	public void addPropertyChangeListener(String propName, PropertyChangeListener lst) {			
		pcs.addPropertyChangeListener(propName, lst);
	}

	public void removePropertyChangeListener(PropertyChangeListener lst) {

		pcs.removePropertyChangeListener(lst);
	}

	public static int getPxStep() {
		return pxStep;
	}
}