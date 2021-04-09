import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Random;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RasendeRoboter {
	
	private JFrame mainFrame;

	private JButton rewindButton;
	private JButton resetButton;
	private JButton passTurnButton;
	
	private String enCour;
	

	//private PionComponent pion;
	private BoardComponent board;

	private final JTextArea[] playerNameTextArea = new JTextArea[4];
	private final JLabel[] playerScoreLabels = new JLabel[4];
	private final JButton[] playersButton = new JButton [4];

	private JLabel targetImage;
	private JLabel robotImage;

	private static JLabel gameTimeLabel;
	private static JLabel solutionTimeLabel;
	private GameData gameManager;
	private Timer gameTimer;

	public RasendeRoboter() {

		gameManager = new GameData();
		gameTimer = new Timer(1000, null);

		createView();
		placeComponent();
		createController();
	}

	private void createView() {
		mainFrame = new JFrame("RasendRoboter");

		rewindButton = new JButton("Rewind");
		resetButton = new JButton("Reset Game");
		passTurnButton = new JButton("Passer le tour");

		board = new BoardComponent(BoardGenerator.generateImages());
		resetButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		resetButton.setBackground(Color.BLUE.darker());
		resetButton.setForeground(Color.WHITE);

		rewindButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
		rewindButton.setBackground(Color.YELLOW);
		rewindButton.setForeground(Color.YELLOW.darker().darker());		

		passTurnButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
		passTurnButton.setBackground(Color.RED);
		passTurnButton.setForeground(Color.RED.darker().darker());

		for(int i = 0; i < 4; i++) {
			playerNameTextArea[i] = new JTextArea();
			playerNameTextArea[i].setEditable(false);
			playerNameTextArea[i].setLineWrap(true);
			playerNameTextArea[i].setWrapStyleWord(true);
			playerNameTextArea[i].setBackground(mainFrame.getBackground());
			playerScoreLabels[i] = new JLabel();
			playersButton[i] = new JButton("Trouvé");
			playersButton[i].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
			playersButton[i].setBackground(Color.GREEN);
			playersButton[i].setForeground(Color.GREEN.darker().darker());
		}

		targetImage = new JLabel();
		robotImage = new JLabel();

		gameTimeLabel = new JLabel("--");
		solutionTimeLabel = new JLabel("--");
	}

	private void placeComponent() {

		JPanel p = new JPanel(new BorderLayout()); {

			p.add(resetButton, BorderLayout.NORTH);

			JPanel s = new JPanel(new GridLayout(3, 0)); {				
				JPanel q = new JPanel(new GridLayout(2, 0)); {
					JPanel r = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
						r.add(new JLabel("Game Time (minutes) : "));
						r.add(gameTimeLabel);
						r.setOpaque(false);
					}
					q.add(r);

					r = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
						r.add(new JLabel("Time Remaining (minutes) : "));
						r.add(solutionTimeLabel);
						r.setOpaque(false);
					}
					q.setBackground(Color.WHITE);
					q.add(r);
				}
				s.add(q);

				q = new JPanel(new GridLayout(2, 2)); {
					JPanel r = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
						r.add(new JLabel("Target : "));
						r.add(targetImage);
						r.setOpaque(false);
					}
					q.add(r);
					r = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
						r.add(new JLabel("Robot : "));
						r.add(robotImage);
						r.setOpaque(false);
					}
					q.add(r);
					q.setBackground(Color.GRAY);
				}
				s.add(q);
				
				q = new JPanel(new BorderLayout()); {
					JPanel t = new JPanel(new FlowLayout(FlowLayout.CENTER));{
						t.add(new JLabel("Scores des joueurs"));
					}
					q.add(t, BorderLayout.NORTH);
					
					t = new JPanel(new GridLayout(4, 0)); {
						for (int i = 0; i < 4; i++) {	
							JPanel r = new JPanel(new FlowLayout(FlowLayout.RIGHT)); {							
								r.add(playerNameTextArea[i]);
								r.add(playerScoreLabels[i]);
								r.add(playersButton[i]);							
								playersButton[i].setVisible(false);
							}
							t.add(r);
						}
					}
					q.add(t);
				}
				s.add(q);
			}
			p.add(s, BorderLayout.CENTER);
			

			s = new JPanel(new GridLayout(2, 0)); {
				s.add(rewindButton);
				s.add(passTurnButton);
			}
			p.add(s, BorderLayout.SOUTH);
		}

		mainFrame.add(board, BorderLayout.CENTER);
		mainFrame.add(p, BorderLayout.EAST);
	}

	private void createController() {

		disactiveButton(rewindButton);
		disactiveButton(passTurnButton);

		board.addPropertyChangeListener(BoardComponent.PROP_READY, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				gameManager.setTargetsList((List<Target>)evt.getNewValue());
				showTarget(gameManager.getNextTarget());
			}
		});

		board.addPropertyChangeListener(BoardComponent.PROP_HAVE_MATCH, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {

				Target currentTarget = gameManager.playedTargets.get(gameManager.playedTargets.size()-1);
				Case matchedCase = (Case)evt.getNewValue();

				if (currentTarget.getType() == matchedCase.getType() && currentTarget.getColor() == matchedCase.getColor()) {

					gameManager.currentPlayer.addPoint();
					gameManager.setCurrentPlayer(GameData.NO_USER_INDEX);

					board.updatePionPosition();
					board.releasePionFocus();

					disactiveButton(rewindButton);
					disactiveButton(passTurnButton);

					gameTimer.stop();

					int nextId = gameManager.getNextTarget();

					if (nextId != -1) {
						showTarget(nextId);
						for(int i = 0, size = gameManager.getPlayersNb(); i < size; i++) {							
							activeButton(playersButton[i]);
						}
						gameTimer.start();
					} else {
						
						showWinner();
					}
					gameManager.refreshSolutionTime();
				}
			}
		});

		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (JOptionPane.showConfirmDialog(null,
						"     La partie n'est pas terminée.\nVoulez-vous quand même la quitter ?",
						"Partie en cours",
						JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

		mainFrame.addWindowFocusListener(new WindowAdapter() {

			@Override
			public void windowGainedFocus(WindowEvent e) {
				if (! gameTimer.isRunning()) {
					gameTimer.start();
				}
			}
		});

		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						//new PlateauTest().display();
						//new RasendeRoboter().display();
						//mainFrame.dispose();
					}
				});
			}
		});

		for (int i = 0, size = playersButton.length; i < size; i++) {

			int index = i;

			playersButton[i].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					disactiveButton((JButton) e.getSource());
					activeButton(rewindButton);
					activeButton(passTurnButton);
					switchToUser(index);					
					board.setInteractions(true);
				}
			});
		}

		rewindButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				board.rewindPions();
			}
		});
		
		passTurnButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				disactiveButton(passTurnButton);
				disactiveButton(rewindButton);
				board.setInteractions(false);
				for (int i = 0, size = gameManager.getPlayersNb(); i < size; i++) {
					activeButton(playersButton[i]);
				}
				gameManager.refreshSolutionTime();
				board.rewindPions();
			}
		});

		gameTimer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				gameManager.updateGameTime();

				if (board.acceptInteractions()) {
					gameManager.updateSolutionTime();
				}

				if (gameManager.getTime() <= 0) {					
					showWinner();
				}
			}
		});
		
		mainFrame.addKeyListener(new KeyAdapter() {
			
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			// gettOut pour recuperer la varible qui decroit avec les seconde de la main du joueur il es declarer et mis a chaque fois a jour dans la calss Board.java
				if(gameManager.getTime() ==59) {
					//no time gére le temps qui est attribuer au jouer la fonction est diponible dans la calss Board.java
				//bord.noTime();
				//bord.timer.start();
				}
				if (e.getKeyChar() =='˛') {
					//System.out.println(bord.gettOut());
					if(gameManager.getTime() !=59) {
						JOptionPane.showMessageDialog(null, enCour + "en cour de jeu, veuillez attendre la fin des 60 sec");
					}else {		
						JOptionPane.showMessageDialog(null, gameManager.playerList.get(0).getPseudo() + "en cour de jeu");
						this.keyPressed = e.getKeyChar();
						enCour = gameManager.playerList.get(0).getPseudo();
					}
				}
				
				if (e.getKeyChar() =='0') {
					if(gameManager.getTime() !=59) {
						JOptionPane.showMessageDialog(null, enCour + "en cour de jeu, veuillez attendre la fin des 60 sec");
						
					}else {
						
						this.keyPressed = e.getKeyChar();
						JOptionPane.showMessageDialog(null,gameManager.playerList.get(1).getPseudo() + "en cour de jeu");
						enCour = gameManager.playerList.get(1).getPseudo();
					}
				}
				if (e.getKeyChar() =='-') {
					if(gameManager.getTime() !=59) {
						JOptionPane.showMessageDialog(null, enCour + "en cour de jeu, veuillez attendre la fin des 60 sec");
					}else {
						
						this.keyPressed = e.getKeyChar();
						JOptionPane.showMessageDialog(null, gameManager.playerList.get(2).getPseudo() + "en cour de jeu");
						enCour = gameManager.playerList.get(2).getPseudo();

					}
				}
				if (e.getKeyChar() =='c') {
					if(gameManager.getTime() != 59) {
						JOptionPane.showMessageDialog(null, enCour + "en cour de jeu, veuillez attendre la fin des 60 sec");
					}else {
						 
						this.keyPressed = e.getKeyChar();
						JOptionPane.showMessageDialog(null, gameManager.playerList.get(3).getPseudo() + "en cour de jeu");
						enCour = gameManager.playerList.get(3).getPseudo();
					}
				}
				
				}
		});
	}
	
	private void showWinner() {
		String text = "";
		
		for (Player winner : gameManager.getWinners()) {
			text += winner.getPseudo() + "avec "+ winner.getPoints()+" points\n";
		}
		
		JOptionPane.showMessageDialog(null, "La partie est terminée ! Le(s) gagnant(s): \n"+text);
		
		for (JButton btn : playersButton) {
			disactiveButton(btn);
		}
		
		disactiveButton(passTurnButton);
		disactiveButton(rewindButton);
	}


	private void switchToUser(int userIndex) {
		gameManager.setCurrentPlayer(userIndex);

		for (int i = 0, size = gameManager.getPlayersNb(); i < size; i++) {
			if (userIndex != i) {
				disactiveButton(playersButton[i]);
			}
		}
	}

	private void activeButton(JButton button) {
		button.setEnabled(true);
		button.setBackground(button.getBackground().brighter());
		button.repaint();
	}

	private void disactiveButton(JButton button) {
		button.setEnabled(false);
		button.setBackground(button.getBackground().darker());
		button.repaint();
	}

	public void display(List<Integer> options) {
		gameManager.init(options);

		mainFrame.setResizable(false);
		mainFrame.pack();
		mainFrame.setVisible(true);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setPlayers(options.get(0));
	}

	private void showTarget(int targetId) {

		ResourceLoarder resourceLoarder = new ResourceLoarder(ResourceLoarder.TARGETS_IMAGES_DIR);

		ImageIcon targetIcon = new ImageIcon(resourceLoarder.getResource("target"+targetId+".png").getAbsolutePath());
		targetIcon.getImage().flush();
		targetImage.setIcon(targetIcon);

		ImageIcon robotIcon = new ImageIcon(resourceLoarder.getResource(targetId/4+".png").getAbsolutePath());
		robotIcon.getImage().flush();
		robotImage.setIcon(robotIcon);		
	}


	private void setPlayers(int playersNb) {

		ArrayList<Player> players = new ArrayList<Player>();

		for (int i = 0; i < playersNb; i++) {

			final int index = i;
			Player p = new Player((JOptionPane.showInputDialog("Nom du joueur "+(i+1)+" :")));

			p.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					playerScoreLabels[index].setText(p.getPoints()+"");
				}
			});

			playerNameTextArea[i].setText(p.getPseudo());
			playerScoreLabels[i].setText(p.getPoints()+"");
			playersButton[i].setVisible(true);
			players.add(p);
			
		}
		gameManager.setPlayers(players);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				RasendeRoboter game = new RasendeRoboter();
				game.display(GameOptionDialog.showOptionDialog(game.mainFrame));
				/*ArrayList<Integer> opt = new ArrayList<Integer>();
				opt.add(1);
				opt.add(1*60);
				opt.add(7*60);
				game.display(opt);*/
				//				JOptionPane.showMessageDialog(null, panel, "Paramètres de la partie",
				//						JOptionPane.PLAIN_MESSAGE);

				/*try {
					playersNb = Integer.parseInt(JOptionPane.showInputDialog("Entrez le nombre de joueurs: "));
				} catch (NumberFormatException e) {
					JOptionPane.showConfirmDialog(null, "La valeur entrée est incorrecte", "Erreur", JOptionPane.ERROR_MESSAGE);
				}*/
			}
		});
	}

	public class GameData {

		private volatile int gameTime;
		private volatile int solutionTime;
		private volatile int currentSolutionTime;

		private List<Target> targetsList;
		private List<Target> playedTargets;

		private List<Player> playerList;

		private Player currentPlayer;

		public final static int NO_USER_INDEX = -1;

		public GameData() {	
			targetsList = new ArrayList<Target>();
			playedTargets = new ArrayList<Target>();
		}

		public TreeSet<Player> getWinners() {
			
			TreeSet<Player> winners = new TreeSet<Player>(new Comparator<Player>() {

				@Override
				public int compare(Player p1, Player p2) {
					return p1.getPoints() - p2.getPoints();
				}
			});
			
			
			for (Player player : playerList) {
				winners.add(player);
			}
			
			for (Player player : winners.headSet(winners.first(), false)) {
				winners.remove(player);
			}
			
			return winners;
		}

		public void refreshSolutionTime() {
			currentSolutionTime = solutionTime;
		}

		public void updateSolutionTime() {

			Thread updater = new Thread(new Runnable() {

				@Override
				public void run() {
					currentSolutionTime -= 1;

					int min = currentSolutionTime / 60;
					int sec = currentSolutionTime % 60;

					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {							
							solutionTimeLabel.setText(min+":"+sec);
						}
					});

				}
			});
			updater.start();
		}

		public void updateGameTime() {

			Thread updater = new Thread(new Runnable() {

				@Override
				public void run() {
					gameTime -= 1;

					int min = gameTime / 60;
					int sec = gameTime % 60;

					SwingUtilities.invokeLater(new Runnable() {		
						@Override
						public void run() {							
							gameTimeLabel.setText(min+":"+sec);
						}
					});
				}
			});
			updater.start();
		}

		public int getPlayersNb() {
			return playerList.size();
		}

		public int getTime() {
			return gameTime;
		}

		public int getSolutionTime() {
			return solutionTime;
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
			playerList = players; 
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
			solutionTime = slTime;

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

		void init(List<Integer> options) {
			this.setTime(options.get(1)*60);
			this.setSolutionTime(options.get(2)*60);
			
			System.out.println(options.get(0));
		}

		public void setCurrentPlayer(int i) {
			if (i >= 0) {
				currentPlayer = playerList.get(i);
			} else {
				currentPlayer = null;
			}
		}
	}
}
