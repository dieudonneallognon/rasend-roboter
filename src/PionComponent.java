
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PionComponent extends JComponent{
		
	private Pion model;
	private PionAnimator animator;
	private boolean focus;
	private boolean moving;
	
	private Rectangle movable;
	private Point casePos;
		
	public static final String PROP_COORD = "coord";
		
	public PionComponent(PionType type, Board.Coord initCoord) {
		
		animator = new PionAnimator(this, null);
	
		createModel(type, initCoord);
		
		createView();
		
		createController();
		
	}
	
	
	private void createView() {
		setPreferredSize(Board.getCaseSize2());
		setSize(getPreferredSize());		
		casePos = positionOnBoard(model.getCoord());
		movable = new Rectangle(Board.getPionSize());
	}


	private void createModel(PionType type, Board.Coord coord) {
		model = new Pion(type, coord);
	}
	
	private void createController() {
		focus = false;
		moving = false;
		
		model.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				firePropertyChange(PROP_COORD, model.getOldCoord(), model.getCoord());
			}
		});
		
		animator.addPropertyChangeListener(PionAnimator.PROP_ANIMATION_ENDED, new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				moving = false;
				Board.testMatchOf(model);
			}
		});
	}
	
	
	public boolean canMoveTo(Board.Coord coord) {
		return Board.canGoTo(model.getCoord(), model.getCoord().directionTo(coord));
	}
	
	public void move(Board.Coord coord) {	
		
		if (!moving) {
			Direction direction = model.getCoord().directionTo(coord);
			
			if (direction != Direction.NONE) {
				moving = true;
				animator.setStep(model.move(direction));
				animator.animate();			
			}
		}
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		//super.paintComponent(g);
		
		this.setLocation(casePos);
		
		movable.setLocation(
				(int) (getWidth()/2 - movable.getWidth()/2),
				(int) (getHeight()/2 - movable.getHeight()/2));
		
		drawPion(g);
		Toolkit.getDefaultToolkit().sync();
	}
	
	private void drawPion(Graphics g) {

		if (haveFocus() && !moving) {
			g.setColor(model.getType().getColor().darker().darker());
			
			if (model.canMoveUp()) {	
				//Haut
				g.fillRect(
						movable().x + movable().width/4,
						movable().y - movable().width/4,
						movable().width/2,
						movable().height);
				
				g.fillPolygon(new int[] {
						movable().x,
						movable().x + movable().width,
						movable().x + movable().width/2
				}, new int[] {
						movable().y - movable().width/4,
						movable().y - movable().width/4,
						movable().y - movable().width*3/4
				}, 3);
				
			}
			
			
			if (model.canMoveRight()) {				
				//Droite
				g.fillRect(
						movable().x + movable().width/4,
						movable().y + movable().width/4,
						movable().width,
						movable().height/2);
				
				
				g.fillPolygon(new int[] {
						movable().x + movable().width + movable().width*1/4,
						movable().x + movable().width + movable().width*1/4,
						movable().x + movable().width + movable().width*3/4
				}, new int[] {
						movable().y+4,
						movable().y+movable().width-4,
						movable().y+movable().width/2
				}, 3);
				
			}
			
			if (model.canMoveDown()) {				
				//Bas
				g.fillRect(
						movable().x + movable().width/4,
						movable().y + movable().width/4,
						movable().width/2,
						movable().height);
				
				g.fillPolygon(new int[] {
						movable().x,
						movable().x + movable().width,
						movable().x + movable().width/2
				}, new int[] {
						movable().y + movable().width + movable().width/4-2,
						movable().y + movable().width + movable().width/4-2,
						movable().y + movable().width + movable().width*3/4
				}, 3);
				
			}
			
			if (model.canMoveLeft()) {
				
				//Gauche
				g.fillRect(
						movable().x - movable().width/4,
						movable().y + movable().width/4,
						movable().width,
						movable().height/2);

				g.fillPolygon(new int[] {
						movable().x - movable().width*1/4,
						movable().x - movable().width*1/4,
						movable().x - movable().width*3/4
				}, new int[] {
						movable().y+4,
						movable().y+movable().width-4,
						movable().y+movable().width/2
				}, 3);
			}			
		}
		
		this.setBackground(null);
		g.setColor(model.getType().getColor().darker().darker());
		g.fillOval(movable.x-4, movable.y-4, movable().width+8, movable().height+8);
		g.setColor(model.getType().getColor().darker());
		g.fillOval(movable.x-2, movable.y-2, movable().width+4, movable().height+4);
		g.setColor(model.getType().getColor());		
		g.fillOval(movable.x, movable.y, movable().width, movable().height);
		
		Toolkit.getDefaultToolkit().sync();
	}
	
	
	public void setFocus(boolean focus) {
		 this.focus = focus;
	 }
	 
	 public boolean haveFocus() {
		 return focus == true;
	 }
	 
	 public Rectangle movable() {
		return this.movable;
	}


	public Pion getModel() {
		return this.model;
	}
	
	public Point positionOnBoard(Board.Coord c) {
		
		return new Point(
				c.getColumn()*Board.caseWidht() - getWidth()/2 + getWidth()/4,
				c.getRow()*Board.caseHeight() - getHeight()/2 + getHeight()/4);
	}
	
	public void setPosition(Point loc) {
		casePos = loc;
		this.setLocation(casePos);
	}
	
	public boolean inMove() {
		return moving;
	}


	public void rewind() {
		model.rewind();
		setPosition(positionOnBoard(model.getCoord()));
		repaint();
	}
}
