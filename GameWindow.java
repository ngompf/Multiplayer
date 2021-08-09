package game;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.InetSocketAddress;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameWindow implements ActionListener {
	private GameManager gameManager;
	
	private Dimension screenSize;
	
	public JFrame frame;
	private JPanel mainPanel;
	private LinkedList<JPanel> previousPanels = new LinkedList<JPanel>();
	
	public JPanel mainMenuPanel;
	private JButton mainMenuConnectPanelButton;
	private JButton mainMenuControlsPanelButton;
	private JButton mainMenuSettingsPanelButton;
	private JButton mainMenuQuitButton;
	
	public JPanel connectionPanel;
	private JTextField nameTextField;
	private JTextField ipTextField;
	private JTextField portTextField;
	private JButton connectionConnectButton;
	private JButton connectionBackButton;
	private JLabel isConnectedLabel;

	public JPanel gamePanel;
	public GameCanvas gameCanvas;
	
	public JPanel controlsPanel;
	private JButton controlsBackButton;
	
	public JPanel settingsPanel;
	private JButton settingsBackButton;
	
	public JPanel pauseMenuPanel;
	private JButton pauseMenuControlsPanelButton;
	private JButton pauseMenuSettingsPanelButton;
	private JButton pauseMenuMainMenuButton;
	private JButton pauseMenuBackButton;
	
	public GameWindow(GameManager gameManager){
		this.gameManager = gameManager;
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		frame = new JFrame("TankGame");
		
		mainPanel = (JPanel) frame.getContentPane();
		initConnectionPanel();
		initMainMenuPanel();
		initControlsPanel();
		initSettingsPanel();
		initPauseMenuPanel();
		initGamePanel();
		
		
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel.setInputMap(0,gameManager.keyBoardHandler.inputMap);
        mainPanel.setActionMap(gameManager.keyBoardHandler.actionMap);
        
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            	gameManager.disconnectClient();
            	System.exit(0);
            }
        });
        
        changePanel(mainMenuPanel);
        
        //frame.setUndecorated(true);
        frame.setPreferredSize(screenSize);
        frame.pack();
        frame.setVisible(true);
	}
	
	public void update() {
		frame.repaint();
    }
	
	private void initMainMenuPanel() {
		mainMenuPanel = new JPanel(new GridBagLayout());
		mainMenuPanel.setName("mainMenuPanel");
		GridBagConstraints c = new GridBagConstraints();
		
		mainMenuConnectPanelButton = initJButton("Connect To Server",250,40,20);
		mainMenuControlsPanelButton = initJButton("Controls",250,40,20);
		mainMenuSettingsPanelButton = initJButton("Settings",250,40,20);
		mainMenuQuitButton = initJButton("Quit",250,40,20);
		
		c.gridx = 0;
 		c.gridy = 0;
 		mainMenuPanel.add(mainMenuConnectPanelButton,c);
 		c.gridy = 1;
 		mainMenuPanel.add(mainMenuControlsPanelButton,c);
 		c.gridy = 2;
 		mainMenuPanel.add(mainMenuSettingsPanelButton,c);
 		c.gridy = 3;
 		mainMenuPanel.add(mainMenuQuitButton,c);
	}
	
	private void initConnectionPanel() {
		connectionPanel = new JPanel(new GridBagLayout());
		connectionPanel.setName("connectionPanel");
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel nameLabel = initJLabel("Name",60,40,20);
        nameTextField = initJTextField("",200,40,20);
        
        JLabel ipLabel = initJLabel("IP",60,40,20);
        ipTextField = initJTextField("127.0.0.1",200,40,20);
        
        JLabel portLabel = initJLabel("Port",60,40,20);
        portTextField = initJTextField("55555",200,40,20);
        
        connectionConnectButton = initJButton("Connect",250,40,20);
        connectionBackButton = initJButton("Back",250,40,20);
        
        isConnectedLabel = initJLabel("Failed To Connect",200,40,20);
        isConnectedLabel.setVisible(false);
        
		c.gridx = 0;
 		c.gridy = 0;
 		connectionPanel.add(nameLabel,c);
 		c.gridx = 1;
 		connectionPanel.add(nameTextField,c);
 		c.gridx = 0;
		c.gridy = 1;
 		connectionPanel.add(ipLabel,c);
 		c.gridx = 1;
 		connectionPanel.add(ipTextField,c);
 		c.gridx = 0;
		c.gridy = 2;
		connectionPanel.add(portLabel,c);
 		c.gridx = 1;
 		connectionPanel.add(portTextField,c);
 		c.gridy = 3;
 		connectionPanel.add(connectionConnectButton,c);
 		c.gridy = 4;
 		connectionPanel.add(connectionBackButton,c);
 		c.gridy = 5;
 		connectionPanel.add(isConnectedLabel,c);
	}
	
	private void initControlsPanel() {
		controlsPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		controlsBackButton = initJButton("Back",250,40,20);
		
		c.gridx = 0;
 		c.gridy = 0;
 		controlsPanel.add(controlsBackButton,c);
	}
	
	private void initSettingsPanel() {
		settingsPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		settingsBackButton = initJButton("Back",250,40,20);
		 
		c.gridx = 0;
 		c.gridy = 0;
 		settingsPanel.add(settingsBackButton,c);
	}
	
	private void initGamePanel() {
		gamePanel = new GameCanvas(gameManager);
	}

	private void initPauseMenuPanel() {
		pauseMenuPanel = new JPanel(new GridBagLayout());
		pauseMenuPanel.setName("pauseMenuPanel");
		GridBagConstraints c = new GridBagConstraints();
		
		pauseMenuMainMenuButton = initJButton("Main Menu",250,40,20);
		pauseMenuControlsPanelButton = initJButton("Controls",250,40,20);
		pauseMenuSettingsPanelButton = initJButton("Settings",250,40,20);
		pauseMenuBackButton = initJButton("Back",250,40,20);
		
		c.gridx = 0;
		c.gridy = 1;
		pauseMenuPanel.add(pauseMenuMainMenuButton,c);
 		c.gridy = 2;
 		pauseMenuPanel.add(pauseMenuControlsPanelButton,c);
 		c.gridy = 3;
 		pauseMenuPanel.add(pauseMenuSettingsPanelButton,c);
 		c.gridy = 4;
 		pauseMenuPanel.add(pauseMenuBackButton,c);
	}
	
	private JLabel initJLabel(String label, int width, int height, int size) {
		JLabel l = new JLabel(label);
		l.setPreferredSize(new Dimension(width, height));
		l.setFont(new Font("Dialog", Font.BOLD, size));
		return l;
	}
	
	private JTextField initJTextField(String text, int width, int height, int size) {
		JTextField t = new JTextField(text);
		t.setPreferredSize(new Dimension(width, height));
		t.setFont(new Font("Dialog", Font.BOLD, size));
		return t;
	}
	
	private JButton initJButton(String label, int width, int height, int size) {
		JButton b = new JButton(label);
		b.setName(label);
		b.addActionListener(this);
		b.setPreferredSize(new Dimension(width, height));
		b.setFont(new Font("Dialog", Font.BOLD, size));
		return b;
	}
	
	public void changePanel(JPanel p) {
		if(mainPanel.getComponentCount() == 0)
			previousPanels.addFirst(mainMenuPanel);
		else
			previousPanels.addFirst(currentPanel());
		mainPanel.removeAll();
		mainPanel.add(p);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	public void changePanelWithoutMemory(JPanel p) {
		mainPanel.removeAll();
		mainPanel.add(p);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	public JPanel currentPanel() {
		return (JPanel) mainPanel.getComponent(0);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == connectionConnectButton) {
			gameManager.connectClient(new InetSocketAddress(ipTextField.getText(), Integer.parseInt(portTextField.getText())), nameTextField.getText());
			if(gameManager.client.connected) {
				isConnectedLabel.setVisible(false);
				changePanel(gamePanel);
			}
			else
				isConnectedLabel.setVisible(true);
		}
		if(e.getSource() == mainMenuConnectPanelButton) {
			changePanel(connectionPanel);
		}
		if(e.getSource() == mainMenuControlsPanelButton || e.getSource() == pauseMenuControlsPanelButton) {
			changePanel(controlsPanel);
		}
		if(e.getSource() == mainMenuSettingsPanelButton || e.getSource() == pauseMenuSettingsPanelButton) {
			changePanel(settingsPanel);
		}
        if(e.getSource() == mainMenuQuitButton) {
        	frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
        if(e.getSource() == pauseMenuMainMenuButton) {
        	gameManager.disconnectClient();
        	changePanel(mainMenuPanel);
        }
        if(e.getSource() == connectionBackButton || e.getSource() == controlsBackButton 
        		|| e.getSource() == settingsBackButton || e.getSource() == pauseMenuBackButton) {
        	changePanelWithoutMemory(previousPanels.pollFirst());
		}
    }
}

