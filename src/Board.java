import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class Board {


	private static int rowNb;
	private static int colNb;
	private static int boardWidth;
	private static int boardHeight;
	private static int caseHeight;
	private static int caseWidth;

	private static final int DEFAULT_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width /2;
	private static final int DEFAULT_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height /2;

	private static List<Target> targetList;
	
	private static Board board;

	private static SortedSet<Case> map;
	
	private static EventListenerList listenerList;
	private static ChangeEvent changeEvent;
	
	private static boolean isReady;
	
	public Board(int boardW, int boardH, int rNb, int cNb) {

		boardWidth = boardW;
		boardHeight = boardH;

		rowNb = rNb;
		colNb = cNb;

		caseHeight = boardHeight / rowNb;
		caseWidth = boardWidth / colNb;
		
		board = this;
		
		listenerList = new EventListenerList();
	}

	public static Dimension size() {
		return new Dimension(boardWidth, boardHeight);
	}

	public static void create(int bW, int bH, int rwNb, int clNb) {
		new Board(bW, bH, rwNb, clNb);
	}

	public static void create(int rwNb, int clNb) {
		new Board(DEFAULT_WIDTH,DEFAULT_HEIGHT, rwNb, clNb);
	}

	public static int getColNb() {
		return boardWidth / caseWidth;
	}

	public static int getRowNb() {
		return boardHeight / caseHeight;
	}

	public static Dimension getCaseSize() {
		return new Dimension(caseWidth, caseHeight);
	}

	public static Dimension getCaseSize2() {
		return new Dimension(caseWidth*2, caseHeight*2);
	}

	public static Dimension getPionSize() {
		return new Dimension(caseWidth *3/4, caseHeight*3/4);
	}

	public static void setFaceIds(List<FaceID> imageIds) {
		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {

				map = new TreeSet<Case>();

				synchronized (map) {
					
					targetList = new ArrayList<Target>();

					for (int i = 0, lenght = imageIds.size();  i < lenght; i++) {

						FacePosition position = FacePosition.intToFacePosition(i+1);

						Case[][] cases = FaceGenerator.generateFace(imageIds.get(i), position).getCases();

						for (int j = 0; j < FaceGenerator.CASE_NUMBER; j++) { //ligne

							
							for (int k = 0; k < FaceGenerator.CASE_NUMBER; k++) {//colone

								Case c = cases[j][k];

								if (c.getType() != CaseType.VIDE && c.getType() != CaseType.TROU_NOIR) {									
									targetList.add(new Target(c.getType(), c.getColor()));
								}

								switch (position) {
								case TOP_LEFT:
									break;


								case TOP_RIGHT:
									c.setCoord(new Board.Coord(c.getCoord().getRow(), c.getCoord().getColumn()+ FaceGenerator.CASE_NUMBER));
									break;

								case BOTTOM_LEFT:
									c.setCoord(new Board.Coord(c.getCoord().getRow() + FaceGenerator.CASE_NUMBER, c.getCoord().getColumn()));
									break;

								case BOTTOM_RIGHT:
									c.setCoord(new Board.Coord(c.getCoord().getRow() + FaceGenerator.CASE_NUMBER, c.getCoord().getColumn() + FaceGenerator.CASE_NUMBER));
									break;					
								}

								map.add(c);	
							}


						}
					}
				}
								
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						fireStateChanged();
					}
				});
			}
		});

		th.start();
	}

	public static int caseWidht() {
		return caseWidth;
	}

	public static int caseHeight() {
		return caseHeight;
	}
	
	
	public boolean isReady() {
		return isReady;
	}
	
	public static int coordIndex(int row, int col) {
		return row * rowNb + col;
	}

	public static Case getCaseAt(Board.Coord c) {
		
		List<Case> mapCopy = new ArrayList<Case>(map); 
		
		return mapCopy.get(coordIndex(c.getRow(), c.getColumn()));
		
	}
	
	public static Case getCaseAt(int row, int column) {
		return new ArrayList<Case>(map).get(coordIndex(row, column));
	}
	
	public List<Target> getTargetList() {
		return targetList;
	}
	
	public static void testMatchOf(Pion model) {
		if (Board.getCaseAt(model.getCoord()).matchPion(model.getType())) {
			fireStateChanged();
		}
	}
	
	public void updateState() {
		isReady = true;
	}

	public static Board.Coord getNextCoord(Board.Coord origin, Direction direction) {

		List<Case> mapCopy = new ArrayList<Case>(map);

		Case oldDestination = mapCopy.get(coordIndex(origin.getRow(), origin.getColumn()));

		switch (direction) {
		case UP:

			for (int i = origin.getRow()-1; i >= 0; i--) {

				Case destination = mapCopy.get(coordIndex(i, origin.getColumn()));

				if (! destination.isFree()) {
					return oldDestination.getCoord();
				}

				if (!destination.canCrossTop()) {
					return destination.getCoord();
				}

				oldDestination = destination;
			}

		case DOWN:

			for (int i = origin.getRow()+1; i < getRowNb(); i++) {

				Case destination = mapCopy.get(coordIndex(i, origin.getColumn()));

				if (!destination.isFree()) {
					return oldDestination.getCoord();
				}

				if (!destination.canCrossDown()) {
					return destination.getCoord();
				}

				oldDestination = destination;

			}

		case LEFT:

			for (int i = origin.getColumn()-1; i >= 0; i--) {

				Case destination = mapCopy.get(coordIndex(origin.getRow(), i));

				if (!destination.isFree()) {
					return oldDestination.getCoord();
				}

				if (!destination.canCrossLeft()) {
					return destination.getCoord();
				}

				oldDestination = destination;

			}

		case RIGHT:
			for (int i = origin.getColumn()+1; i < colNb; i++) {

				Case destination = mapCopy.get(coordIndex(origin.getRow(), i));

				if (! destination.isFree()) {
					return oldDestination.getCoord();
				}

				if (!destination.canCrossRight()) {
					return destination.getCoord();
				}

				oldDestination = destination;
			}
		default:
			break;
		}
		return origin;
	}

	public static boolean canGoTo(Board.Coord origin, Direction direction) {	

		Case actualCase = getCaseAt(origin.getRow(), origin.getColumn());
		boolean canGo = false;

		switch (direction) {
		case UP: {
			if (actualCase.canCrossTop()) {
				Case upperCase = getCaseAt(origin.getRow()-1, origin.getColumn());
				canGo = upperCase.isFree();
			}
			break;
		}
		case DOWN: {
			if (actualCase.canCrossDown()) {
				Case lowerCase = getCaseAt(origin.getRow()+1, origin.getColumn());
				canGo = lowerCase.isFree();
			}
			break;
		}
		case LEFT: {
			if (actualCase.canCrossLeft()) {
				Case leftCase = getCaseAt(origin.getRow(), origin.getColumn()-1);
				canGo = leftCase.isFree();
			}
			break;
		}
		case RIGHT: {
			if (actualCase.canCrossRight()) {
				Case rightCase = getCaseAt(origin.getRow(), origin.getColumn()+1);
				canGo = rightCase.isFree();				
			}
		}
		default:
			break;
		}
		return canGo;
	}


	public void addChangeListener(ChangeListener listener) {
		listenerList.add(ChangeListener.class, listener);
	}
	
	public void removeChangeListener(ChangeListener listener) {
		
		listenerList.remove(ChangeListener.class, listener);
	}
	
	protected static void fireStateChanged() {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(board);
				}
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}



	public static class Coord implements Comparable<Coord>{

		private int row;
		private int colum;

		public Coord(int row, int column) {
			this.row = row;
			this.colum = column;
		}

		public Coord(Point point) {
			row = point.y / caseHeight;
			colum = point.x / caseWidth;
		}

		public int getRow() {
			return this.row;
		}

		public int getColumn() {
			return this.colum;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public void setColum(int colum) {
			this.colum = colum;
		}

		public boolean onUPBorder() {
			return this.row == 0;
		}

		public boolean onDownBorder() {
			return this.row == (getRowNb() -1);
		}

		public boolean onLeftBorder() {
			return this.colum == 0;
		}

		public boolean onRightBorder() {
			return this.colum == (getColNb() -1);
		}

		public Point toPoint() {			
			return new Point(colum *caseWidth+caseWidth/8, row*caseHeight+caseHeight/8);
		}

		@Override
		public String toString() {
			return "Coord( "+row+", "+colum+")";
		}

		public Direction directionTo(Coord c) {
			Direction d = Direction.NONE;


			if (c.colum == colum) {
				if (c.row > row) {
					d = Direction.DOWN;
				} else if (c.row < row ) {
					d = Direction.UP;
				}
			} else if (c.row == row) {
				if (c.colum > colum) {
					d = Direction.RIGHT;
				} else  {
					d = Direction.LEFT;
				}
			}

			return d;
		}


		@Override
		public boolean equals(Object obj) {

			if (obj instanceof Board.Coord) {

				Board.Coord other = (Board.Coord) obj;

				if (hashCode() == other.hashCode()) {	
					return true;
				}					
			}
			return false;
		}

		@Override
		public int hashCode() {
			return coordIndex(row, colum);
		}

		@Override
		public int compareTo(Board.Coord o) {
			return hashCode() - o.hashCode();
		}

	}
}
