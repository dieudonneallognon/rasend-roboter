import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BoardComponent extends JPanel {

	protected static final String PROP_READY = "ready";

	protected static final String PROP_HAVE_MATCH = "match";

	private List<BufferedImage> imageFile;

	private boolean acceptInteraction;

	private Board model;	
	PionComponent [] pionList;
	PionComponent currentFocused;

	public BoardComponent(List<BufferedImage> list) {

		this.imageFile = list;

		createModel();
		createView();
		placeComponents();
		this.setPreferredSize(new Dimension(Board.getCaseSize().width*16, Board.getCaseSize().height*16));
		createController();
	}



	private void createModel() {
		model = new Board(700, 700, 16, 16);
	}

	private void createView() {

		Random randRow = new Random();
		Random randCol= new Random();

		Set<Board.Coord> randomCoords = new HashSet<Board.Coord>();

		while (randomCoords.size() < 4) {
			
			int row = randRow.nextInt(16);
			int col = randCol.nextInt(16);
			
			if ( (row != 7 && row != 8) || (col != 7 && col != 8)) {
				randomCoords.add(new Board.Coord(row, col));
			}
		}

		pionList = new PionComponent[4];

		for (int i = 0; i < 4; i++) {
			pionList[i] = new PionComponent(PionType.values()[i], (Board.Coord)(randomCoords.toArray()[i]));
		}
	}

	private void createController() {

		model.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (model.isReady() && currentFocused != null) {
					firePropertyChange(PROP_HAVE_MATCH, null, Board.getCaseAt(currentFocused.getModel().getCoord()));
					acceptInteraction = false;
				} else {
					for (PionComponent pion : pionList) {
						Board.getCaseAt(pion.getModel().getCoord()).setState(CaseState.OCCUPED);					
					}
					model.updateState();
					firePropertyChange(PROP_READY, null, model.getTargetList());					
				}
			}
		});

		for (PionComponent pion : pionList) {

			pion.addPropertyChangeListener(PionComponent.PROP_COORD, new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Board.getCaseAt((Board.Coord)evt.getOldValue()).setState(CaseState.FREE);
					Board.getCaseAt((Board.Coord)evt.getNewValue()).setState(CaseState.OCCUPED);
				}
			});

			pion.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {

					if (acceptInteraction) {

						PionComponent pionComponent = (PionComponent)e.getSource();

						if (currentFocused == null) {	
							setPionFocusOn(pionComponent);
						} else if (currentFocused == pionComponent) {
							releasePionFocus();
						}
					} else {
						if (currentFocused != null) {
							releasePionFocus();
						}
					}
				}
			});
		}

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (acceptInteraction) {					
					Board.Coord clikPos = new Board.Coord(e.getPoint());

					if (currentFocused != null
							&& !currentFocused.getModel().getCoord().equals(clikPos)
							&& currentFocused.canMoveTo(clikPos)) {
						currentFocused.move(clikPos);
					} 
				}

			}
		});
	}

	private void setPionFocusOn(PionComponent pion) {
		pion.setFocus(true);						
		pion.repaint();
		currentFocused = pion;
	}

	public void releasePionFocus() {
		if (currentFocused != null) {
			currentFocused.setFocus(false);
			currentFocused.repaint();
			currentFocused = null;			
		}
	}

	private void placeComponents() {
		for (PionComponent pionComponent : pionList) {
			this.add(pionComponent);
		}
	}
	
	public boolean acceptInteractions() {
		return acceptInteraction;
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		try {
			g.drawImage(imageFile.get(0), getWidth()/2, 0, getWidth()/2, getHeight()/2, this);
			g.drawImage(imageFile.get(1), getWidth()/2, getHeight()/2, getWidth()/2, getHeight()/2, this);
			g.drawImage(imageFile.get(2), 0, getHeight()/2,getWidth()/2, getHeight()/2, this);
			g.drawImage(imageFile.get(3), 0, 0, getWidth()/2, getHeight()/2, this);
		} catch (IndexOutOfBoundsException e) {
			repaint();
		}
		g.setColor(Color.PINK);
		for (int i = 0; i <= Board.size().width; i+= Board.getCaseSize().width) {

			g.fillRect(i, 0, 1, getHeight());
			g.fillRect(0, i, getWidth(), 1);
		}
	}



	public void rewindPions() {
		for (PionComponent pionComponent : pionList) {
			pionComponent.rewind();
		}
		releasePionFocus();
	}



	public void updatePionPosition() {
		for (PionComponent pionComponent : pionList) {

			Board.Coord currentCoord = pionComponent.getModel().getCoord();

			pionComponent.getModel().setOldCoord(new Board.Coord(currentCoord.getRow(), currentCoord.getColumn()));
			pionComponent.getModel().setRewindCoord(new Board.Coord(currentCoord.getRow(), currentCoord.getColumn()));
			pionComponent.getModel().resetCount();
		}
	}
	
	public int getMovesCount() {
		
		int moves = 0;
		
		for (PionComponent pionComponent : pionList) {
			moves += pionComponent.getModel().getCount();
		}
		
		return moves;
	}



	public void setInteractions(boolean b) {
		acceptInteraction = b;
	}
}
