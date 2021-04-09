import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.Timer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	
	private String joueur;
	private Timer timer;
	private int second;
	private String ddsecond;
	private JOptionPane and;
	
	private Timer timerGame;
	private int minGame;
	private int secGame;
	private String ddsecGame;
	private String ddminGame;
	
	private JPanel panel1;
	private JPanel buttPanel;
	private JPanel timerPanel;
	private JPanel IconLoding;
	private JPanel RobotLoding;
	private JPanel Players;
	private JLabel label;
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	public JButton Exit;
	public JButton resetGame;
	private JButton gotIt;

	private JLabel gameTime;
	private JLabel TimeOut;
	private JLabel iconLabel;
	private JLabel rebotLabel;
	private JLabel iconSet;
	private JLabel robotSet;
	private JLabel player1;
	private JLabel player2;
	private JLabel player3;
	private JLabel player4;
	private JLabel gTime;
	private JLabel tOut;
	
	private ImageIcon icon;
	private ImageIcon Robot;
	//private ImageIcon game;
	
	private DecimalFormat dFormat;
	
	public BoardPanel() {
		dFormat = new DecimalFormat("00");
		
		this.second = 59;
		this.secGame = 59;
		this.minGame = 4;
		
		label = new JLabel("");
		label1 = new JLabel("");
		label2 = new JLabel("");
		label3 = new JLabel("");
		player1 = new JLabel("Player 1: ");
		player2 = new JLabel("Player 2: ");
		player3 = new JLabel("Player 3: ");
		player4 = new JLabel("Player 4: ");
		 
		
		panel1 = new JPanel();
		buttPanel = new JPanel();
		timerPanel = new JPanel();
		IconLoding = new JPanel();
		RobotLoding = new JPanel();
		Players = new JPanel();
		
		Exit = new JButton("EXIT");
		Exit.setForeground(Color.RED);
		Exit.setBorder(BorderFactory.createLineBorder(Color.red));
		Exit.setPreferredSize(new Dimension(10,10));
		Exit.setBorder(new RoundBtn(15));
		/*Exit.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 testBord.Frm.dispose();
	         }
	      });*/
		
		resetGame = new JButton("Reset Game");
		resetGame.setForeground(Color.green);
		resetGame.setBorder(new RoundBtn(15));
		/*resetGame.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	testBord.Frm.repaint();
	         }
	      });*/
		
		gotIt = new JButton("Got it !");
		gotIt.setForeground(Color.BLUE);
		gotIt.setBorder(BorderFactory.createLineBorder(Color.blue));
		gotIt.setBorder(new RoundBtn(15));
		//resetGame.setBorder(BorderFactory.createLineBorder(Color.green));
		gotIt.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	joueur = JOptionPane.showInputDialog("entrez votre non de joueur");
	        	second = 60;
	        	noTime();
	        	timer.start();
	        	
	         }
	      });
		
		gameTime = new JLabel("Game Time :");
		TimeOut = new JLabel("Time Remining :");
		gTime = new JLabel();
		tOut = new JLabel(""+second);
		iconLabel = new JLabel("Target: ");
		rebotLabel = new JLabel("rebot: ");
		gTime.setText(""+ minGame+":"+ secGame);
		noTimeGame();
		timerGame.start();
		
		
		icon = new ImageIcon("C:\\Users\\ghiles\\Desktop\\target-yellow-square.png");
		iconSet = new JLabel(icon, JLabel.CENTER);
		iconSet.setPreferredSize(new Dimension(30,30));
		Robot = new ImageIcon("C:\\Users\\ghiles\\Desktop\\robot-blue.png");
		robotSet = new JLabel(Robot, JLabel.CENTER);
		robotSet.setPreferredSize(new Dimension(30,45));

		this.setSize(60, 400);
		
		buttPanel.setBackground(Color.DARK_GRAY);
		buttPanel.setLayout(new GridLayout(1,2));
		buttPanel.add(Exit);
		buttPanel.add(resetGame);
		
		timerPanel.setLayout(new GridLayout(2,2));
		timerPanel.add(gameTime);
		timerPanel.add(gTime);
		timerPanel.add(TimeOut);
		timerPanel.add(tOut);
		
		IconLoding.setLayout(new GridLayout(1,2));
		IconLoding.add(iconLabel);
		IconLoding.add(iconSet);
		RobotLoding.setLayout(new GridLayout(1,2));
		RobotLoding.add(rebotLabel);
		RobotLoding.add(robotSet);
				
		Players.setLayout(new GridLayout(4,1));
		Players.add(player1);
		Players.add(player2);
		Players.add(player3);
		Players.add(player4);
		
		
		panel1.setBackground(Color.LIGHT_GRAY);
		panel1.setLayout(new GridLayout(10,1));
		panel1.add(buttPanel);
		panel1.add(label);
		panel1.add(timerPanel);
		panel1.add(label1);
		panel1.add(IconLoding);
		panel1.add(RobotLoding);
		panel1.add(label2);
		panel1.add(Players);
		panel1.add(label3);
		panel1.add(gotIt);
		
		
		this.add(panel1);
		
		
	}
	public void noTime() {
	timer = new Timer(1000, new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			second--;
			ddsecond = dFormat.format(second);
			tOut.setText(""+ ddsecond);
			if(second == 0) {
				and = new JOptionPane() ;
				and.showMessageDialog(null, "timeaout", "Information", JOptionPane.INFORMATION_MESSAGE);
				second = 60;
				ddsecond = dFormat.format(second);
				tOut.setText(""+ ddsecond+" sec");
				timer.stop();
			}
		  }		
		});	
     }
	public void noTimeGame() {
		timerGame = new Timer(1000, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				secGame--;
				ddsecGame = dFormat.format(secGame);
				ddminGame = dFormat.format(minGame);
				gTime.setText(""+ ddminGame+":"+ ddsecGame);
				if(minGame == 0 && secGame == 0) {
					and = new JOptionPane() ;
					and.showMessageDialog(null, "the time of the game is out", "Information", JOptionPane.INFORMATION_MESSAGE);
					secGame = 59;
					minGame = 4;
					ddsecGame = dFormat.format(secGame);
					ddminGame = dFormat.format(minGame);
					gTime.setText(""+ ddminGame+":"+ ddsecGame);
					timerGame.stop();
				}
				if(secGame == 0) {
					minGame--;
					secGame = 59;
					ddsecGame = dFormat.format(secGame);
					ddminGame = dFormat.format(minGame);
					gTime.setText(""+ ddminGame+":"+ ddsecGame);
				}
				
				
			}
			
				});
		
	}
}