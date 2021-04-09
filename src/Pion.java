import java.awt.Point;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class Pion {

	private Board.Coord oldCoord;
	private Board.Coord coord;
	private Board.Coord rewindCoord;

	private int count;

	private PionType type;

	private EventListenerList listenerList;
	private ChangeEvent changeEvent;
	
	private StepMaker stepUp = new StepMaker() {
		@Override
		public void makeStepOn(PionComponent pc) {
			pc.setPosition(new Point(pc.getX(), pc.getY() - PionAnimator.getPxStep()));
		}
	};
	
	private StepMaker stepDown = new StepMaker() {
		@Override
		public void makeStepOn(PionComponent pc) {
			pc.setPosition(new Point(pc.getX(), pc.getY() + PionAnimator.getPxStep()));
		}
	};
	
	private StepMaker stepLeft = new StepMaker() {
		@Override
		public void makeStepOn(PionComponent pc) {
			pc.setPosition(new Point(pc.getX() - PionAnimator.getPxStep(), pc.getY()));
		}
	};
	
	private StepMaker stepRight = new StepMaker() {
		@Override
		public void makeStepOn(PionComponent pc) {
			pc.setPosition(new Point(pc.getX() + PionAnimator.getPxStep(), pc.getY()));
		}
	};

	private static final int NEXT_BLOCK_UP = 0;
	private static final int NEXT_BLOCK_LEFT = 0;
	private static final int NEXT_BLOCK_DOWN = Board.getRowNb()-1;
	private static final int NEXT_BLOCK_RIGHT = Board.getColNb()-1;
	protected static final int STEP = 2;



	public Pion(PionType type, Board.Coord c) {
		this.type = type;
		this.coord = c;
		this.oldCoord = new Board.Coord(c.getRow(), c.getColumn());
		this.rewindCoord = new Board.Coord(c.getRow(), c.getColumn());
		
		this.count = 0;

		listenerList = new EventListenerList();
	}

	public StepMaker move(Direction direction) {

		StepMaker step = null;
		
		oldCoord = new Board.Coord(coord.getRow(), coord.getColumn());
		Board.Coord nextCoord = Board.getNextCoord(coord, direction);
			
		switch (direction) {
		case UP: {
			coord.setRow(nextCoord.getRow());
			step = stepUp;
			break;
		}
		case DOWN: {
			coord.setRow(nextCoord.getRow());	
			step = stepDown;
			break;
		}
		case LEFT: {
			coord.setColum(nextCoord.getColumn());
			step = stepLeft;
			break;
		}
		case RIGHT: {
			coord.setColum(nextCoord.getColumn());
			step = stepRight;
			break;
		}
		default:
			break;
		}
		
		fireStateChanged();
		return step;
	}


	public boolean canMoveUp() {
		return Board.canGoTo(coord, Direction.UP); 
	}

	public boolean canMoveDown() {
		return Board.canGoTo(coord, Direction.DOWN); 
	}

	public boolean canMoveLeft() {
		return Board.canGoTo(coord, Direction.LEFT);
	}

	public boolean canMoveRight() {
		return Board.canGoTo(coord, Direction.RIGHT);
	}

	public Board.Coord getCoord() {
		return coord; 
	}

	public PionType getType() {
		return this.type;
	}

	public Board.Coord getOldCoord() {
		return this.oldCoord;
	}

	public int getCount() {
		return this.count;
	}

	public void rewind() {
		if (!rewindCoord.equals(coord)) {
			oldCoord = new Board.Coord(coord.getRow(), coord.getColumn());
			coord = new Board.Coord(rewindCoord.getRow(), rewindCoord.getColumn());
			count = 0;			
			fireStateChanged();			
		}
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
	
	public void setCoord(Board.Coord coord) {
		this.coord = coord;
	}
	
	public void setOldCoord(Board.Coord oldCoord) {
		this.oldCoord = oldCoord;
	}
	
	public void setRewindCoord(Board.Coord rewindCoord) {
		this.rewindCoord = rewindCoord;
	}
}
