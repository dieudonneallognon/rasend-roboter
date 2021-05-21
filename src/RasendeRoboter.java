import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RasendeRoboter {

	private JFrame mainFrame;

	private JButton rewindButton;
	private JButton resetButton;
	private JButton passTurnButton;
	private JButton startButton;

	private BoardComponent board;

	private final JTextArea[] playerNameTextArea = new JTextArea[4];
	private final JLabel[] playerScoreLabels = new JLabel[4];
	private final JButton[] playersButton = new JButton [4];

	private JLabel targetImage;
	private JLabel robotImage;

	private JLabel gameTimeLabel;
	private JLabel reflexionTimeLabel;
	private JLabel currentPlayerNameLabel;
	private JLabel responseTimeLabel;
	private GamePartyModel  party;
	private GameOptionDialog optionDialog;

	private static final String NO_INFO = "--";
	private static final int NO_USER = -1;
	private static final int MAX_PLAYER_NUMBER = 4;

	public RasendeRoboter() {

		//gameManager = new GameData();
		//gameTimer = new Timer(1000, null);

		party = new GamePartyModel();
		createView();
		placeComponent();
		createController();
	}

	private void createView() {
		mainFrame = new JFrame("RasendRoboter");

		board = new BoardComponent(BoardGenerator.generateImages());

		resetButton = new JButton("Nouvelle partie");
		resetButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		resetButton.setBackground(Color.BLUE.darker());
		resetButton.setForeground(Color.WHITE);

		startButton = new JButton("Démarrer la partie");
		startButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		startButton.setBackground(Color.GREEN);
		startButton.setForeground(Color.GREEN.darker());

		rewindButton = new JButton("Rétablir les postions");
		rewindButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		rewindButton.setBackground(Color.YELLOW);
		rewindButton.setForeground(Color.YELLOW.darker().darker());		

		passTurnButton = new JButton("Passer le tour");
		passTurnButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		passTurnButton.setBackground(Color.RED);
		passTurnButton.setForeground(Color.RED.darker().darker());

		for(int i = 0; i < 4; i++) {
			playerNameTextArea[i] = new JTextArea("Joureur " + (i+1));
			playerNameTextArea[i].setEditable(false);
			playerNameTextArea[i].setLineWrap(true);
			playerNameTextArea[i].setWrapStyleWord(true);
			playerNameTextArea[i].setBackground(mainFrame.getBackground());

			playerScoreLabels[i] = new JLabel("0");

			playersButton[i] = new JButton("Trouvé");
			playersButton[i].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
			playersButton[i].setBackground(Color.GREEN);
			playersButton[i].setForeground(Color.GREEN.darker());
		}

		targetImage = new JLabel();
		robotImage = new JLabel();

		gameTimeLabel = new JLabel("00:00");
		reflexionTimeLabel = new JLabel("00:00");
		currentPlayerNameLabel = new JLabel(NO_INFO);
		responseTimeLabel = new JLabel("00:00");

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
				JPanel q = new JPanel(new GridLayout(4, 0)); {

					JPanel r = new  JPanel(new FlowLayout(FlowLayout.CENTER)); {
						r.add(new JLabel("Temps de jeu (min): "));
						r.add(gameTimeLabel);
						r.setOpaque(false);
					}
					q.add(r);					

					r = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
						r.add(new JLabel("Temps de Réflexion (min): "));
						r.add(reflexionTimeLabel);
						r.setOpaque(false);
					}
					q.add(r);

					r = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
						r.add(new JLabel("Temps de Solution (min): "));
						r.add(responseTimeLabel);
						r.setOpaque(false);
					}
					q.add(r);

					r = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
						r.add(new JLabel("Main à : "));
						r.add(currentPlayerNameLabel);
						r.setOpaque(false);
					}
					q.add(r);

					q.setBackground(Color.YELLOW);
					q.setBorder( BorderFactory.createTitledBorder(
							BorderFactory.createLineBorder(Color.BLACK, 2),
							"Infos de la partie",
							TitledBorder.CENTER, TitledBorder.TOP
							));
				}
				s.add(q);

				q = new JPanel(new GridLayout(2, 0)); {
					JPanel r = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
						r.add(new JLabel("Objectif : "));
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
					q.setBackground(Color.GRAY.brighter());
					//q.setBorder(BorderFactory.createTitledBorder(border, title, titleJustification, titlePosition, titleFont, titleColor));
					q.setBorder( BorderFactory.createTitledBorder(
							BorderFactory.createBevelBorder(BevelBorder.RAISED),
							"Prochain point",
							TitledBorder.CENTER, TitledBorder.TOP
							));
				}
				s.add(q);

				q = new JPanel(new BorderLayout()); {

					JPanel t = new JPanel(new GridLayout(4, 0)); {
						for (int i = 0; i < 4; i++) {	
							JPanel r = new JPanel(new FlowLayout(FlowLayout.RIGHT)); {							
								r.add(playerNameTextArea[i]);
								r.add(playerScoreLabels[i]);
								r.add(playersButton[i]);

								playersButton[i].setVisible(true);
								playerNameTextArea[i].setVisible(true);
								playerScoreLabels[i].setVisible(true);
							}
							t.add(r);
						}
					}
					q.setBorder( BorderFactory.createTitledBorder(
							BorderFactory.createEtchedBorder(),
							"Scores des joueurs",
							TitledBorder.CENTER, TitledBorder.TOP
							));
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

		p.setBorder(BorderFactory.createEtchedBorder());

		mainFrame.add(p, BorderLayout.EAST);

		mainFrame.add(board, BorderLayout.CENTER);
	}

	public static boolean exitConfirmed() {

		Object[] options = { "Oui", "Non" };

		return (JOptionPane.showOptionDialog(null, 
				"    La partie n'est pas terminée\nVoulez-vous quand même la quitter ?",
				"Partie en cours",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, options, options[0]) == 0);

	}

	private void createController() {
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				party.stopChrono();

				if (exitConfirmed()) {
					System.exit(0);
				}

				party.startChrono();
			}
		});

		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

			@Override
			public void eventDispatched(AWTEvent event) {

				if (event.getID() == KeyEvent.KEY_TYPED) {

					try {
						int playerNum = Integer.parseInt(((KeyEvent) event).getKeyChar()+"");

						if (party.hasStarted() && playerNum >= 1 && playerNum <= party.getPlayersNb()) {

							if (party.getCurrentPlayerNum() == playerNum-1) {

								switchToUser(NO_USER);

							} else if (party.getCurrentPlayerNum() >= 0) {
								party.stopChrono();
								JOptionPane.showMessageDialog(null, 
										party.getPlayers().get(party.getCurrentPlayerNum()).getPseudo() + " est en cour de jeu, veuillez attendre la fin du temps");
								party.startChrono();
							} else {
								switchToUser(playerNum-1);	
							}
						}
					} catch (NumberFormatException e) {
						// TODO: handle exception
					}
				}
			}
		}, AWTEvent.KEY_EVENT_MASK);


		optionDialog.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				setPartyOptions(optionDialog.getOptions());
			}
		});



		party.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				int gameTime = party.getGameTime();
				int reflexionTime = party.getReflexionTime();
				int responseTime = party.getResponseTime();

				gameTimeLabel.setText((gameTime < 0)
						? "∞" : String.format("%02d", gameTime/60)+":"+String.format("%02d", gameTime%60));

				reflexionTimeLabel.setText((reflexionTime < 0)
						? "∞" : String.format("%02d", reflexionTime/60)+":"+String.format("%02d", reflexionTime%60));

				responseTimeLabel.setText((responseTime < 0)
						? "∞" : String.format("%02d", responseTime/60)+":"+String.format("%02d", responseTime%60));

				List<Player> playerList = ((GamePartyModel) e.getSource()).getPlayers();


				if (! party.isReady()) {

					enableButton(startButton);

					for (int i = 0; i < playerList.size(); i++) {

						int index = i; 
						playerNameTextArea[i].setText(playerList.get(i).getPseudo());
						playerScoreLabels[i].setText(playerList.get(i).getPoints()+"");

						playersButton[i].addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {

								disableButton((JButton) e.getSource());
								enableButton(rewindButton);
								enableButton(passTurnButton);
								switchToUser(index);					
								board.setInteractions(true);
							}
						});
					}

					for (int i = party.getPlayersNb(); i < MAX_PLAYER_NUMBER; i++) {
						playersButton[i].setVisible(false);
						playerNameTextArea[i].setVisible(false);
						playerScoreLabels[i].setVisible(false);
					}

				} else {

					try {
						playerScoreLabels[party.getCurrentPlayerNum()]
								.setText(Integer.toString(playerList.get(party.getCurrentPlayerNum()).getPoints()));
					} catch (ArrayIndexOutOfBoundsException ex) {
						// There is no current player we just started the party
					}

					if (party.chronoIsActive()) {

						if (gameTime == 0) {

							for (int i = 0; i < 4; i++) {						
								disableButton(playersButton[i]);
							}

							disableButtonList(new JButton [] {passTurnButton, rewindButton, startButton});

							showWinner();

						} else if (reflexionTime == 0) {
							party.resetReflexionTime();
							showNextTarget();
						} else if (responseTime == 0) {
							JOptionPane.showMessageDialog( 
									null,
									"Temps de réponse épuisé",
									"Temps de réponse",
									JOptionPane.INFORMATION_MESSAGE
									);

							switchToUser(NO_USER);
						}
					}
				}
			}
		});

		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JButton btn = (JButton) e.getSource();				

				disableButton(btn);
				enableButton(resetButton);
				enableButton(passTurnButton);

				board.setVisible(true);

				for (int i = 0; i < party.getPlayersNb(); i++) {
					enableButton(playersButton[i]);
				}

				party.startParty();
			}
		});

		passTurnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (party.getCurrentPlayerNum() == NO_USER) {

					showNextTarget();

				} else {
					party.resetReflexionTime();
					disableButton(rewindButton);					
					board.rewindPions();
				}

				switchToUser(NO_USER);
			}
		});

		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (exitConfirmed()) {
					mainFrame.dispose();
					party.stopChrono();
					new RasendeRoboter().display();
				} 
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
				party.setTargetsList((List<Target>)evt.getNewValue());
				showNextTarget();
			}
		});

		board.addPropertyChangeListener(BoardComponent.PROP_HAVE_MATCH, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {

				//Target currentTarget = partyModel.playedTargets.get(partyModel.getPlayersNb()-1);

				Target currentTarget = party.getCurrentTarget();

				Case matchedCase = (Case)evt.getNewValue();

				if (currentTarget.getType() == matchedCase.getType()
						&& currentTarget.getColor() == matchedCase.getColor()) {

					party.addPointToCurrentPlayer();
					
					board.updatePionPosition();
					board.releasePionFocus();

					party.stopChrono();

					try {
						showTarget(party.getNextTarget());

						party.resetReflexionTime();

						switchToUser(NO_USER);

						party.startChrono();

					} catch (GamePartyModel.EmptyTargetListException e) {
						showWinner();
					}
				}
			}
		});
	}

	private void showWinner() {
		String text = "";

		board.setInteractions(false);

		party.stopChrono();

		disableButtonList(new JButton []{passTurnButton, rewindButton});

		for (JButton btn : playersButton) {
			disableButton(btn);
		}

		for (Player winner : party.getWinners()) {
			text += "- "+(winner.getPseudo() + "\n");
		}

		JOptionPane.showMessageDialog(
				null,
				"La partie est terminée ! Le(s) gagnant(s) avec "+party.getWinners().get(0).getPoints()+": points \n"+text);
	}


	private void switchToUser(int userIndex) {

		Player currentPlayer = null;

		try {
			board.setInteractions(true);

			currentPlayer = party.getPlayers().get(userIndex);			
			currentPlayerNameLabel.setText(currentPlayer.getPseudo());

			enableButton(rewindButton);


			for (int i = 0, size = party.getPlayersNb(); i < size; i++) {
				if (playersButton[i].isEnabled()) {					
					disableButton(playersButton[i]);
				}
			}

			party.resetResponseTime();
		} catch (IndexOutOfBoundsException e) {
			board.setInteractions(false);

			currentPlayerNameLabel.setText(NO_INFO);

			disableButton(rewindButton);

			for (int i = 0, size = party.getPlayersNb(); i < size; i++) {
				if (! playersButton[i].isEnabled()) {					
					enableButton(playersButton[i]);
				}
			}
		}

		board.rewindPions();

		party.setCurrentPlayer(currentPlayer);
	}

	private void enableButton(JButton button) {

		if (! button.isEnabled()) {			
			button.setEnabled(true);
			button.setBackground(button.getBackground().brighter());
			button.repaint();
		}
	}

	private void disableButton(JButton button) {

		if (button.isEnabled()) {			
			button.setEnabled(false);
			button.setBackground(button.getBackground().darker());
			button.repaint();
		}
	}

	private void initGameWindow() {

		/*for (int i = 0; i < 4; i++) {						
			playerNameTextArea[i].setVisible(false);
			playerScoreLabels[i].setVisible(false);
			playersButton[i].setVisible(false);
		}*/

		disableButtonList(new JButton []{passTurnButton, rewindButton, resetButton, startButton});

		for (JButton jButton : playersButton) {
			disableButton(jButton);
		}

		board.setVisible(false);
	}

	public void display() {

		mainFrame.setResizable(false);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		initGameWindow();

		mainFrame.setVisible(true);
		optionDialog.setVisible(true);
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

	private void showNextTarget() {

		try {
			showTarget(party.getNextTarget());

			for(int i = 0, size = party.getPlayersNb(); i < size; i++) {							
				enableButton(playersButton[i]);
			}

			party.resetReflexionTime();
			party.startChrono();

		} catch (GamePartyModel.EmptyTargetListException e) {

			party.stopChrono();
			showWinner();
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
	}


	private void setPartyOptions(List<Integer> options) {

		ArrayList<Player> players = new ArrayList<Player>();

		TreeSet<String> pseudoList = new TreeSet<String>();

		int plNb = options.remove(0);

		for (int i = 0; i < plNb; i++) {

			String pseudo = null;

			do {				
				pseudo = JOptionPane.showInputDialog(null,
						"              Pseudo du joueur "+(i+1)+"",
						"Joueur "+(i+1),
						JOptionPane.PLAIN_MESSAGE);


				if (pseudo == null) {
					optionDialog.setVisible(true);
					break;

				} else if (pseudo.isBlank()) {
					JOptionPane.showMessageDialog(null, "Vous devez choisir un pseudo");

				} else if (pseudoList.contains(pseudo)) {
					JOptionPane.showMessageDialog(null, "Ce pseudo a déjà été pris !");
				}
			} while (pseudo == null || pseudo.isBlank() || pseudoList.contains(pseudo));

			if (pseudo == null) {
				break;
			}

			players.add(new Player(pseudo));
			pseudoList.add(pseudo);
		}

		if (players.size() == plNb) {
			party.init(options, players);
		}
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new RasendeRoboter().display();
			}
		});
	}

	private void disableButtonList(JButton [] btnList) {

		for (JButton button : btnList) {
			disableButton(button);
		}	
	}
}
