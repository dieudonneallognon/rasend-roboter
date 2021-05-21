import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;


public class GameOptionDialog extends JDialog{

	public static final String [] PLAYERS_NUMBERS = new String [] {"2", "3", "4"};
	public static final String [] GAME_TIMES = new String[] { "5", "10", "30", "∞"};
	public static final String [] GAME_REFLEXION_TIMES = new String[] { "1", "2", "5", "∞"};
	public static final String [] GAME_RESPONSE_TIMES = new String[] { "1", "2", "3", "∞"};
	
	public static final int INFINITE_VAL = -1;
	
	
	public static final JComboBox<String> PLAYERS_NUMBER_COMBO = new JComboBox<String>(PLAYERS_NUMBERS);
	protected static final JComboBox<String> GAME_TIME_COMBO = new JComboBox<String>(GAME_TIMES);
	protected static final JComboBox<String> REFLEXION_TIME_COMBO = new JComboBox<String>(GAME_REFLEXION_TIMES);
	
	protected static final JComboBox<String> RESPONSE_TIME_COMBO = new JComboBox<String>(GAME_RESPONSE_TIMES);
	
	private  EventListenerList listenerList;
	private  ChangeEvent changeEvent;
	private ArrayList<Integer> options = new ArrayList<Integer>();
	
	public GameOptionDialog(JFrame owner) {
		// TODO Auto-generated constructor stub
		
		super(owner, "Paramètres de la partie", true);
		
		listenerList = new EventListenerList();
		
		JButton startButton = new JButton("Jouer");
		
		add(new JPanel(), BorderLayout.NORTH);		
		JPanel panel = new JPanel(new GridLayout(4, 2)); {
			panel.add(new JLabel("Nombre de joueurs: "));
			panel.add(PLAYERS_NUMBER_COMBO);
			panel.add(new JLabel("Temps de la partie (minutes): "));
			panel.add(GAME_TIME_COMBO);
			panel.add(new JLabel("Temps réflexion (minutes): "));
			panel.add(REFLEXION_TIME_COMBO);
			panel.add(new JLabel("Temps de réponse (minutes): "));
			panel.add(RESPONSE_TIME_COMBO);
		}
		add(panel, BorderLayout.CENTER);		
		add(startButton, BorderLayout.SOUTH);
		

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				
				options.clear();
				
				options.add(Integer.parseInt((String) PLAYERS_NUMBER_COMBO.getSelectedItem()));
								
				try {
					options.add(Integer.parseInt((String)GAME_TIME_COMBO.getSelectedItem()));
				} catch (NumberFormatException e) {
					options.add(INFINITE_VAL);
				}
				
				try {
					options.add(Integer.parseInt((String)REFLEXION_TIME_COMBO.getSelectedItem()));
				} catch (NumberFormatException e) {
					options.add(INFINITE_VAL);
				}
				
				try {
					options.add(Integer.parseInt((String)RESPONSE_TIME_COMBO.getSelectedItem()));
				} catch (NumberFormatException e) {
					options.add(INFINITE_VAL);
				}
				
				System.out.println(options);
				
				
				if (optionsAreValid()) {					
					setVisible(false);
					fireStateChanged();
				} else {
					JOptionPane.showMessageDialog(
							null,
							"Il y a des erreurs dans le choix des options !",
							"Options Incorrectes",
							JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (RasendeRoboter.exitConfirmed()) {
					System.exit(0);
				}
			}
		});
		
		
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		
	}
	
	public ArrayList<Integer> getOptions() {
		return new ArrayList<Integer>(options);
	}
	
	
	public boolean optionsAreValid() {
		return (options.get(1) > options.get(2));
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
	
	
}
