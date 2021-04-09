import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class GameOptionDialog extends JDialog{

	public static final String [] PLAYERS_NUMBERS = new String [] {"2", "3", "4"};
	public static final String [] GAME_TIMES = new String[] { "7", "10", "20", "---"};
	public static final String [] GAME_RESPONSE_TIMES = new String[] { "1", "2", "3", "---"};
	public static final JComboBox<String> PLAYERS_NUMBER_COMBO = new JComboBox<String>(PLAYERS_NUMBERS);
	protected static final JComboBox<String> GAME_TIME_COMBO = new JComboBox<String>(GAME_TIMES);;
	protected static final JComboBox<String> RESPONSE_TIME_COMBO = new JComboBox<String>(GAME_RESPONSE_TIMES);;


	public static ArrayList<Integer> showOptionDialog(JFrame owner) {

		JDialog dialog = new JDialog(owner, "Paramères de la partie", true);

		JButton startButton = new JButton("Commencer");

		dialog.add(new JPanel(), BorderLayout.NORTH);		
		JPanel panel = new JPanel(new GridLayout(4, 2)); {
			panel.add(new JLabel("Nombre de joueurs: "));
			panel.add(PLAYERS_NUMBER_COMBO);
			panel.add(new JLabel("Temps de la partie (minutes): "));
			panel.add(GAME_TIME_COMBO);
			panel.add(new JLabel("Temps de réponse (minutes): "));
			panel.add(RESPONSE_TIME_COMBO);
		}
		dialog.add(panel, BorderLayout.CENTER);		
		dialog.add(startButton, BorderLayout.SOUTH);
		//			add(
		//					new JLabel(
		//							"<html><h1><i>Core Java</i></h1><hr>By Cay Horstmann and Gary Cornell</html>"),
		//					BorderLayout.CENTER);



		PLAYERS_NUMBER_COMBO.setSelectedIndex(0);
		GAME_TIME_COMBO.setSelectedIndex(0);
		RESPONSE_TIME_COMBO.setSelectedIndex(0);

		ArrayList<Integer> list = new ArrayList<Integer>();

		list.add(Integer.parseInt((String) PLAYERS_NUMBER_COMBO.getSelectedItem()));
		list.add(Integer.parseInt((String) GAME_TIME_COMBO.getSelectedItem()));
		list.add(Integer.parseInt((String) RESPONSE_TIME_COMBO.getSelectedItem()));

		PLAYERS_NUMBER_COMBO.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				list.set(Integer.parseInt((String) PLAYERS_NUMBER_COMBO.getSelectedItem()), 0);
			}
		});

		RESPONSE_TIME_COMBO.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				try {
					list.set(Integer.parseInt((String)RESPONSE_TIME_COMBO.getSelectedItem()), 1);
				} catch (NumberFormatException ex) {
					list.add(-1);
				}
			}
		});

		GAME_TIME_COMBO.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				try {
					list.set(Integer.parseInt((String)GAME_TIME_COMBO.getSelectedItem()), 2);
				} catch (NumberFormatException ex) {
					list.add(-1);
				}	
			}
		});


		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event)
			{
				dialog.setVisible(false);
			}
		});

		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
		
		System.out.println(list.get(0));

		System.out.println(list.get(1));

		System.out.println(list.get(2));

		return list;		
	}
}
