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
import java.beans.PropertyChangeSupport;
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
import javax.swing.event.EventListenerList;

public class RasendeRoboter {

	private JFrame mainFrame;

	private JButton rewindButton;
	private JButton resetButton;
	private JButton passTurnButton;
	private JButton startButton;

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
	private GamePartyModel  partyModel;
	private GameOptionDialog optionDialog;
	private Timer gameTimer;


	public RasendeRoboter() {

		//gameManager = new GameData();
		//gameTimer = new Timer(1000, null);

		partyModel = new GamePartyModel();
		createView();
		placeComponent();
		createController();
	}

	private void createView() {
		mainFrame = new JFrame("RasendRoboter");

		rewindButton = new JButton("Rewind");
		resetButton = new JButton("Reset Game");
		passTurnButton = new JButton("Passer le tour");
		startButton = new JButton("Start Party");

		board = new BoardComponent(BoardGenerator.generateImages());
		resetButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		resetButton.setBackground(Color.BLUE.darker());
		resetButton.setForeground(Color.WHITE);

		startButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		startButton.setBackground(Color.GREEN);
		startButton.setForeground(Color.GREEN.darker());

		rewindButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
		rewindButton.setBackground(Color.YELLOW);
		rewindButton.setForeground(Color.YELLOW.darker().darker());		

		passTurnButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
		passTurnButton.setBackground(Color.RED);
		passTurnButton.setForeground(Color.RED.darker().darker());

		for(int i = 0; i < 4; i++) {
			playerNameTextArea[i] = new JTextArea("Joureur " + (i+1));
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

		optionDialog = new GameOptionDialog(mainFrame);
	}

	private void placeComponent() {

		JPanel p = new JPanel(new BorderLayout()); {

			JPanel s = new JPanel(new GridLayout(2, 0)); {
				s.add(startButton);
				s.add(resetButton);
			}
			p.add(s, BorderLayout.NORTH);

			s = new JPanel(new GridLayout(3, 0)); {				
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
								playerNameTextArea[i].setText("Player "+(i+1));
								playerScoreLabels[i].setText("0");
								r.add(playerNameTextArea[i]);
								r.add(playerScoreLabels[i]);
								r.add(playersButton[i]);							
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

		mainFrame.add(p, BorderLayout.EAST);
		p = new JPanel(new GridLayout(0, 1)); {			
			p.add(board);
		}
		mainFrame.add(p, BorderLayout.CENTER);
	}

	/*private void createController() {

		disactiveButton(rewindButton);
		disactiveButton(passTurnButton);

		partyModel.addPropertyChangeListener(PROP_IS_READY, lst);

		board.addPropertyChangeListener(BoardComponent.PROP_READY, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				//gameManager.setTargetsList((List<Target>)evt.getNewValue());
				showTarget(partyModel.getNextTarget());
			}
		});

		board.addPropertyChangeListener(BoardComponent.PROP_HAVE_MATCH, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {

				Target currentTarget = partyModel.playedTargets.get(partyModel.getPlayersNb()-1);
				Case matchedCase = (Case)evt.getNewValue();

				if (currentTarget.getType() == matchedCase.getType() && currentTarget.getColor() == matchedCase.getColor()) {

					//gameManager.currentPlayer.addPoint();
					//gameManager.setCurrentPlayer(GameData.NO_USER_INDEX);

					board.updatePionPosition();
					board.releasePionFocus();

					disactiveButton(rewindButton);
					disactiveButton(passTurnButton);

					//gameTimer.stop();

					int nextId = partyModel.getNextTarget();

					if (nextId != -1) {
						showTarget(nextId);
						for(int i = 0, size = partyModel.getPlayersNb(); i < size; i++) {							
							activeButton(playersButton[i]);
						}
						gameTimer.start();
					} else {

						showWinner();
					}
					//gameManager.refreshSolutionTime();
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
				//gameManager.refreshSolutionTime();
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

//		mainFrame.addKeyListener(new KeyAdapter() {
//			
//			public void keyTyped(KeyEvent e) {
//				// TODO Auto-generated method stub
//			// gettOut pour recuperer la varible qui decroit avec les seconde de la main du joueur il es declarer et mis a chaque fois a jour dans la calss Board.java
//				if(gameManager.getTime() ==59) {
//					//no time gére le temps qui est attribuer au jouer la fonction est diponible dans la calss Board.java
//				//bord.noTime();
//				//bord.timer.start();
//				}
//				if (e.getKeyChar() =='˛') {
//					//System.out.println(bord.gettOut());
//					if(gameManager.getTime() !=59) {
//						JOptionPane.showMessageDialog(null, enCour + "en cour de jeu, veuillez attendre la fin des 60 sec");
//					}else {		
//						JOptionPane.showMessageDialog(null, gameManager.playerList.get(0).getPseudo() + "en cour de jeu");
//						this.keyPressed = e.getKeyChar();
//						enCour = gameManager.playerList.get(0).getPseudo();
//					}
//				}
//				
//				if (e.getKeyChar() =='0') {
//					if(gameManager.getTime() !=59) {
//						JOptionPane.showMessageDialog(null, enCour + "en cour de jeu, veuillez attendre la fin des 60 sec");
//						
//					}else {
//						
//						this.keyPressed = e.getKeyChar();
//						JOptionPane.showMessageDialog(null,gameManager.playerList.get(1).getPseudo() + "en cour de jeu");
//						enCour = gameManager.playerList.get(1).getPseudo();
//					}
//				}
//				if (e.getKeyChar() =='-') {
//					if(gameManager.getTime() !=59) {
//						JOptionPane.showMessageDialog(null, enCour + "en cour de jeu, veuillez attendre la fin des 60 sec");
//					}else {
//						
//						this.keyPressed = e.getKeyChar();
//						JOptionPane.showMessageDialog(null, gameManager.playerList.get(2).getPseudo() + "en cour de jeu");
//						enCour = gameManager.playerList.get(2).getPseudo();
//
//					}
//				}
//				if (e.getKeyChar() =='c') {
//					if(gameManager.getTime() != 59) {
//						JOptionPane.showMessageDialog(null, enCour + "en cour de jeu, veuillez attendre la fin des 60 sec");
//					}else {
//						 
//						this.keyPressed = e.getKeyChar();
//						JOptionPane.showMessageDialog(null, gameManager.playerList.get().getPseudo() + "en cour de jeu");
//						enCour = gameManager.playerList.get(3).getPseudo();
//					}
//				}
//				
//				}
//		});
	}
	 */

	private void createController() {
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				partyModel.pauseGame();

				if (JOptionPane.showConfirmDialog(null,
						"     La partie n'est pas terminée.\nVoulez-vous quand même la quitter ?",
						"Partie en cours",
						JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					System.exit(0);
				} 

				partyModel.resumeGame();
			}
		});


		optionDialog.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				setPartyOptions(optionDialog.getOptions());
			}
		});
		
		

		partyModel.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				if (! partyModel.isReady()) {

					gameTimeLabel.setText((partyModel.getTime()/60 == -1) ? "∞" : Integer.toString(partyModel.getTime()/60)+":00");
					solutionTimeLabel.setText((partyModel.getSolutionTime()/60 == -1) ? "∞" : Integer.toString(partyModel.getSolutionTime()/60)+":00");

					List<Player> playerList = ((GamePartyModel) e.getSource()).getPlayers();

					for (int i = 0; i < playerList.size(); i++) {

						int index = i; 
						playerNameTextArea[i].setText(playerList.get(i).getPseudo());
						playerScoreLabels[i].setText(playerList.get(i).getPoints()+"");

						playersButton[i].setVisible(true);
						playerNameTextArea[i].setVisible(true);
						playerScoreLabels[i].setVisible(true);

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


					activeButton(startButton);					
				} else if (partyModel.isStarted()) {

					int gameTime = partyModel.getTime();
					int solutionTime = partyModel.getSolutionTime();


					gameTimeLabel.setText(gameTime/60 + ":"+gameTime%60);
					solutionTimeLabel.setText(solutionTime/60 + ":"+solutionTime%60);



					if (gameTime == 0) {
						for (int i = 0; i < 4; i++) {						
							disactiveButton(playersButton[i]);
						}
						disactiveButton(passTurnButton);
						disactiveButton(rewindButton);
						disactiveButton(startButton);

						showWinner();

					} else if (solutionTime == 60) {
						showTarget(partyModel.getNextTarget());
					}
				}
			}
		});

		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JButton btn = (JButton) e.getSource();				

				disactiveButton(btn);
				activeButton(resetButton);
				//activeButton(passTurnButton);

				board.setVisible(true);

				for (int i = 0; i < partyModel.getPlayersNb(); i++) {
					activeButton(playersButton[i]);
				}

				partyModel.startParty();
			}
		});

		passTurnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (! board.acceptInteractions()) {
					if (rewindButton.isEnabled()) {
						disactiveButton(rewindButton);					
					}
					showTarget(partyModel.getNextTarget());					
				} else {
					partyModel.refreshSolutionTime();
					board.rewindPions();					
					board.setInteractions(false);
				}
				
				for (int i = 0, size = partyModel.getPlayersNb(); i < size; i++) {
					activeButton(playersButton[i]);
				}
			}
		});

		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RasendeRoboter game = new RasendeRoboter();
				mainFrame.dispose();
				game.display();
			}
		});

		rewindButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				board.rewindPions();
			}
		});

		board.addPropertyChangeListener(BoardComponent.PROP_READY, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				partyModel.setTargetsList((List<Target>)evt.getNewValue());
				showTarget(partyModel.getNextTarget());
			}
		});
		
		board.addPropertyChangeListener(BoardComponent.PROP_HAVE_MATCH, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {

				//Target currentTarget = partyModel.playedTargets.get(partyModel.getPlayersNb()-1);
				
				Target currentTarget = partyModel.getCurrentTarget();
				
				Case matchedCase = (Case)evt.getNewValue();

				if (currentTarget.getType() == matchedCase.getType() && currentTarget.getColor() == matchedCase.getColor()) {

					partyModel.addPointToCurrentPlayer();
					board.updatePionPosition();
					board.releasePionFocus();

					disactiveButton(rewindButton);
					disactiveButton(passTurnButton);

					partyModel.pauseGame();
					
					int nextId = partyModel.getNextTarget();

					if (nextId != -1) {
						showTarget(nextId);
						for(int i = 0, size = partyModel.getPlayersNb(); i < size; i++) {							
							activeButton(playersButton[i]);
						}
						partyModel.resumeGame();
					} else {

						showWinner();
					}
					//gameManager.refreshSolutionTime();
				}
			}
		});
	}

	private void showWinner() {
		String text = "";

		for (Player winner : partyModel.getWinners()) {
			text += (winner.getPseudo() + "\n");
		}

		JOptionPane.showMessageDialog(null, "La partie est terminée ! Le(s) gagnant(s) avec "+partyModel.getWinners().get(0).getPoints()+": points \n"+text);

		for (JButton btn : playersButton) {
			disactiveButton(btn);
		}

		disactiveButton(passTurnButton);
		disactiveButton(rewindButton);
		
		partyModel.pauseGame();
	}


	private void switchToUser(int userIndex) {
		partyModel.setCurrentPlayer(userIndex);

		for (int i = 0, size = partyModel.getPlayersNb(); i < size; i++) {
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

	private void prepareParty() {
		for (int i = 0; i < 4; i++) {						
			playerNameTextArea[i].setVisible(false);
			playerScoreLabels[i].setVisible(false);
			playersButton[i].setVisible(false);
		}

		disactiveButton(resetButton);
		disactiveButton(rewindButton);
		disactiveButton(startButton);
		disactiveButton(passTurnButton);

		for (JButton jButton : playersButton) {
			disactiveButton(jButton);
		}
		board.setVisible(false);
	}

	public void display() {

		mainFrame.setResizable(false);
		mainFrame.pack();
		mainFrame.setVisible(true);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		prepareParty();


		//gameManager.init(options);
		//setPlayers(options.get(0));
		//board.setVisible(false);
		optionDialog.setVisible(true);
	}

	private void showTarget(int targetId) {

		if (targetId != -1) {
			ResourceLoarder resourceLoarder = new ResourceLoarder(ResourceLoarder.TARGETS_IMAGES_DIR);

			ImageIcon targetIcon = new ImageIcon(resourceLoarder.getResource("target"+targetId+".png").getAbsolutePath());
			targetIcon.getImage().flush();
			targetImage.setIcon(targetIcon);

			ImageIcon robotIcon = new ImageIcon(resourceLoarder.getResource(targetId/4+".png").getAbsolutePath());
			robotIcon.getImage().flush();
			robotImage.setIcon(robotIcon);
		} else {
			
			partyModel.pauseGame();
			showWinner();
		}
	}


	private void setPartyOptions(List<Integer> options) {

		ArrayList<Player> players = new ArrayList<Player>();

		int plNb = options.remove(0);

		for (int i = 0; i < plNb; i++) {

			String pseudo = null;

			do {				
				pseudo = JOptionPane.showInputDialog("Nom du joueur "+(i+1)+" :");


				if (pseudo == null) {
					optionDialog.setVisible(true);
					break;
				} else if (pseudo.isBlank()) {
					JOptionPane.showMessageDialog(null, "Vous devez choisir un pseudo");
				}
			} while (pseudo == null || pseudo.isBlank());

			if (pseudo == null) {
				break;
			}

			Player p = new Player(pseudo);
			players.add(p);
		}

		if (players.size() == plNb) {
			partyModel.init(options, players);
		}
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				RasendeRoboter game = new RasendeRoboter();
				game.display();
			}
		});
	}
}
