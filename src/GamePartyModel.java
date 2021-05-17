import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.TreeSet;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class GamePartyModel {

		private volatile int gameTime;
		private volatile int reflexionTime;
		private volatile int currentReflexionTime;
		
		private boolean ready;
		private boolean started;
		
		private PropertyChangeSupport pcs;

		public String PROP_IS_READY = "isReady";

		private List<Target> targetsList;
		private List<Target> playedTargets;

		private List<Player> playerList;
		private List<Player> winnersList;

		private Player currentPlayer;
		
		private EventListenerList listenersList;
		private ChangeEvent changeEvent;
		
		private Timer gameChrono;
		
		private Thread updater;
		
		public GamePartyModel () {	
			targetsList = new ArrayList<Target>();
			playedTargets = new ArrayList<Target>();
			pcs =  new PropertyChangeSupport(this);
			
			listenersList = new EventListenerList();
		}
		
		
		public List<Player> getWinners() {
			return winnersList;
		}

		public void resetReflexionTime() {
			currentReflexionTime = reflexionTime;
		}

		public void updateReflexionTime() {
			
			if (reflexionTime > 0 && (currentReflexionTime -= 1) == 0) {
				resetReflexionTime();
			}
		}

		public void updateGameTime() {
			
			if (gameTime > 0 && ((gameTime -= 1) == 0)) {
				gameChrono.stop();
			}
		}

		public int getPlayersNb() {
			return playerList.size();
		}

		public int getTime() {
			return gameTime;
		}

		public int getReflexionTime() {
			return currentReflexionTime;
		}

		public int getNextTarget() throws EmptyTargetListException{			
			
			try {
				
				Target randomTarget = targetsList.remove(new Random().nextInt(targetsList.size())); 
				playedTargets.add(randomTarget);
				
				return randomTarget.getId();
				
			} catch (IllegalArgumentException e) {
				throw new EmptyTargetListException("No more target to play !");
			}
		}


		public void setPlayers(List<Player> players) {
			playerList = new ArrayList<Player>(players);
			winnersList = new ArrayList<Player>(players);
		}
		
		public List<Player> getPlayers() {
			return new ArrayList<Player>(playerList);
		}


		public void setTargetsList(List<Target> targetsList) {
			this.targetsList = targetsList;
		}

		void init(List<Integer> options, List<Player> players) {
			
			gameTime = options.get(0)*60;
			reflexionTime = options.get(1)*60;
			currentReflexionTime = options.get(1)*60;
			
			setPlayers(players);
			fireStateChanged();

			ready = true;
		}

		public void setCurrentPlayer(Player p) {
			try {
				currentPlayer = p;
			} catch (IndexOutOfBoundsException e) {
				currentPlayer = null;
			}
		}
		
		public int getCurrentPlayerNum() {
			return playerList.indexOf(currentPlayer);
		}
		
		public boolean isReady() {
			return ready;
		}
		
		public boolean hasStarted() {
			return started;
		}
		
		public void addPointToCurrentPlayer() {
			
			currentPlayer.addPoint();
			
			if (! winnersList.contains(currentPlayer)) {
				winnersList.add(currentPlayer);
			} else {
				
				ListIterator<Player> it = winnersList.listIterator();
				
				while (it.hasNext()) {
					Player pl = (Player) it.next();
					
					if (pl.getPoints() < currentPlayer.getPoints()) {
						it.remove();
					}
				}
			}
			
			fireStateChanged();
			
			currentPlayer = null;
		}
		
		public boolean chronoIsActive() {
			return gameTime > 0 || reflexionTime > 0;
		}
		
		
		
		
		
		public void startParty() {
			
			
			if (chronoIsActive()) {
				
				gameChrono = new Timer(1000, new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						updater = new Thread(new Runnable() {
							
							@Override
							public void run() {
								updateGameTime();
								updateReflexionTime();
								
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										fireStateChanged();
									}
								});
							}
						});
						updater.start();
					}
				});
				
				gameChrono.start();
			}
			started = true;			
		}
		
		public void stopChrono() {
			
			if (gameChrono != null) {
				gameChrono.stop();				
			}
		}
		
		public void startChrono() {
			
			if (gameChrono != null) {
				gameChrono.start();
			}
		}
		
		
		public Target getCurrentTarget() {
			return playedTargets.get(playedTargets.size()-1);
		}
		
		protected void fireStateChanged() {
			Object[] listeners = listenersList.getListenerList();
			
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
		
		public void addChangeListener(ChangeListener listener) {
			listenersList.add(ChangeListener.class, listener);
		}
		
		public void removeChangeListener(ChangeListener listener) {
			listenersList.remove(ChangeListener.class, listener);
		}
		
		
		public static class EmptyTargetListException extends Exception {
			
			public EmptyTargetListException(String msg) {
				super(msg);
			}
			
		}
	}