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
		private volatile int solutionTime;
		private volatile int currentSolutionTime;
		
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

		public final static int NO_USER_INDEX = -1;
		
		public GamePartyModel () {	
			targetsList = new ArrayList<Target>();
			playedTargets = new ArrayList<Target>();
			pcs =  new PropertyChangeSupport(this);
			
			listenersList = new EventListenerList();
		}
		
		
		public List<Player> getWinners() {
			return winnersList;
		}

		public void refreshSolutionTime() {
			currentSolutionTime = solutionTime;
		}

		public void updateSolutionTime() {
			currentSolutionTime -= 1;
			
			if (currentSolutionTime == 0) {
				refreshSolutionTime();
			}
		}

		public void updateGameTime() {
			gameTime -= 1;
			
			if (gameTime == 0) {
				gameChrono.stop();
			}
		}

		public int getPlayersNb() {
			return playerList.size();
		}

		public int getTime() {
			return gameTime;
		}

		public int getSolutionTime() {
			return currentSolutionTime;
		}

		public int getNextTarget() {

			int nextTargetId = -1;

			if (targetsList.size() > 0) {
				Target randomTarget = targetsList.remove(new Random().nextInt(targetsList.size())); 
				playedTargets.add(randomTarget);
				nextTargetId = randomTarget.getId();				
			}

			return nextTargetId;
		}


		public void setPlayers(List<Player> players) {
			playerList = new ArrayList<Player>(players);
			winnersList = new ArrayList<Player>(players);
		}
		
		public List<Player> getPlayers() {
			return new ArrayList<Player>(playerList);
		}

		public void setTime(int t) {
			gameTime = t;

			if (t == -1) {
				gameTimeLabel.setText("∞");
			} else {	
				gameTimeLabel.setText(Integer.toString(t/60)+":00");
			}
		}

		public void setSolutionTime(int slTime) {
			

			if (solutionTime == -1) {
				solutionTimeLabel.setText("∞");
			} else {
				solutionTimeLabel.setText(Integer.toString(slTime/60)+":00");
			}
			currentSolutionTime = solutionTime;
		}

		public void setTargetsList(List<Target> targetsList) {
			this.targetsList = targetsList;
		}

		void init(List<Integer> options, List<Player> players) {
			
			gameTime = options.get(0)*60;
			solutionTime = options.get(1)*60;
			currentSolutionTime = options.get(1)*60;
			
			setPlayers(players);
			fireStateChanged();

			ready = true;
		}

		public void setCurrentPlayer(int i) {
			if (i >= 0) {
				currentPlayer = playerList.get(i);
			} else {
				currentPlayer = null;
			}
		}
		
		public boolean isReady() {
			return ready;
		}
		
		public boolean isStarted() {
			return started;
		}
		
		public void addPointToCurrentPlayer() {
			
			currentPlayer.addPoint();
			
			if (! winnersList.contains(currentPlayer)) {
				winnersList.add(currentPlayer);
			} else {
				
				ListIterator<Player> it = playerList.listIterator();
				
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
		
		
		
		
		
		public void startParty() {
			
			gameChrono = new Timer(1000, new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					updater = new Thread(new Runnable() {
						
						@Override
						public void run() {
							updateGameTime();
							updateSolutionTime();
							
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
			started = true;
		}
		
		public void pauseGame() {
			
			if (gameChrono != null) {
				gameChrono.stop();				
			}
		}
		
		public void resumeGame() {
			
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
	}