package core;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import display.MeasTablePanel;
import display.PlotsPanel;

public class AppFrame extends JFrame implements WindowListener
{
	private static final long serialVersionUID = 146571341364742862L;

	static PlotsPanel plots_panel;
	static MeasTablePanel meas_table_panel;
	ControlPanel control_panel;
	SavePanel save_panel;
	StatisticsPanel statistics_panel;
	JPanel left_panel;
	MyReader serialreader;
	Measurements measurements;
	static String chosen_port;
	
	JButton connectButton, discoverPortsButton, chooseFileButton;
	protected JTextField portTextField;
	JCheckBox save_checkbox;
	JLabel filename_label;
	JTabbedPane tabbed_pane;
	
	
	public AppFrame(Measurements meas, MyReader reader) throws HeadlessException
	{
		super();
		setTitle("CosmicWatch");
		setSize(1000,900);
		setLocation(400, 50);
		setLayout(new BorderLayout());
		addWindowListener(this);
		
		serialreader = reader;
		measurements = meas;
		
		left_panel = new JPanel(new GridLayout(6,1));
		control_panel = new ControlPanel();
		control_panel.setBorder(BorderFactory.createTitledBorder("POŁĄCZENIE"));
		left_panel.add(control_panel);
		save_panel = new SavePanel();
		save_panel.setBorder(BorderFactory.createTitledBorder("ZAPIS DO PLIKU"));
		left_panel.add(save_panel);
		statistics_panel = new StatisticsPanel();
		statistics_panel.setBorder(BorderFactory.createTitledBorder("STATYSTYKI"));
		left_panel.add(statistics_panel);
		
		this.getContentPane().add(left_panel,BorderLayout.WEST);

		tabbed_pane = new JTabbedPane();
		meas_table_panel = new MeasTablePanel(measurements);
		plots_panel = new PlotsPanel(measurements, this);
		tabbed_pane.addTab("Wykresy", plots_panel);
		tabbed_pane.addTab("Pomiary", meas_table_panel);
		this.getContentPane().add(tabbed_pane,BorderLayout.CENTER);
	}
	
	class ControlPanel extends JPanel
	{
		private static final long serialVersionUID = 1445821555546539232L;
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		public ControlPanel()
		{
			super(new GridBagLayout());
			setPreferredSize(new Dimension(220, AppFrame.this.getHeight()));
			
			portTextField = new JTextField("PORT", 15);
			portTextField.setEditable(false);
			portTextField.setMinimumSize(new Dimension(100,25));
			
			discoverPortsButton = new JButton("Wykryj");
			discoverPortsButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e)
				{
					new PortChooser(AppFrame.this);
				}
			});
			
			connectButton = new JButton("Połącz");
			connectButton.addActionListener(new ConnectAction());
			connectButton.setEnabled(false);
			
			gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 0;  gbc.insets = new java.awt.Insets(5, 2, 5, 2);
			add(discoverPortsButton,gbc);
			gbc.gridx = 1;
			add(portTextField,gbc);
			gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
			add(connectButton,gbc);
			
		}
	}
	
	class SavePanel extends JPanel
	{
		private static final long serialVersionUID = 7030193382686655912L;
		
		GridBagConstraints gbc = new GridBagConstraints();
		public SavePanel()
		{
			super(new GridBagLayout());
			chooseFileButton = new JButton("Wybierz plik");
			
			save_checkbox = new JCheckBox("Zapis", false);
			
			filename_label = new JLabel("Nazwa pliku");
			
			gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.insets = new java.awt.Insets(5, 2, 5, 2); gbc.anchor = GridBagConstraints.LINE_START;
			add(chooseFileButton,gbc);
			gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_END;
			add(save_checkbox, gbc);
			gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
			add(filename_label,gbc);
		}
	}
	
	class StatisticsPanel extends JPanel
	{
		private static final long serialVersionUID = 8553913718118843142L;
		
		GridBagConstraints gbc = new GridBagConstraints();
		JLabel counts_label;
		JLabel average_counts_label, five_min_average_counts_label, thirty_min_average_counts;
		
		public StatisticsPanel()
		{
			super(new GridBagLayout());
			
			gbc.gridx = 0; gbc.anchor = GridBagConstraints.LINE_START; gbc.insets = new Insets(5, 10, 5, 10);
			add(new JLabel("Liczba zliczeń "), gbc);
			
			counts_label = new JLabel("<LICZBA>");
			gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_END;
			add(counts_label, gbc);
			
			gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.CENTER;
			add(new JLabel("ŚREDNIA LICZBA ZLICZEŃ"), gbc);

			gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.LINE_START;
			add(new JLabel("Wszystkie zliczenia"), gbc);
			
			average_counts_label = new JLabel("<LICZBA>");
			gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_END;
			add(average_counts_label, gbc);
			
			gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.LINE_START;
			add(new JLabel("Ostatnie 5 minut"), gbc);
			
			five_min_average_counts_label = new JLabel("<LICZBA>");
			gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_END;
			add(five_min_average_counts_label, gbc);
			
			gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.LINE_START;
			add(new JLabel("Ostatnie 30 minut"), gbc);
			
			thirty_min_average_counts = new JLabel("<LICZBA>");
			gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_END;
			add(thirty_min_average_counts, gbc);
		}
	}
	
	class ConnectAction extends AbstractAction
	{
		private static final long serialVersionUID = 3910738984347391681L;

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(serialreader.isConnected())
				return;
			
			try 
			{
				if(serialreader.connect(chosen_port))	//Thread here
				{
					plots_panel.runThread();		//Thread here
					connectButton.setText("Rozłącz");
					connectButton.addActionListener(new DisconnectAction());
					connectButton.removeActionListener(ConnectAction.this);
				}
			} 
			catch (Exception e1) {e1.printStackTrace();}
		}
	}
	
	class DisconnectAction extends AbstractAction
	{
		private static final long serialVersionUID = -5760165226245089602L;

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			closeApp();
			
			if(!serialreader.isConnected())
			{
				connectButton.setText("Połącz");
				connectButton.addActionListener(new ConnectAction());
				connectButton.removeActionListener(DisconnectAction.this);
			}
		}
	}
	
	void closeApp()
	{
		serialreader.disconnect();
		plots_panel.stopDisplaying();
		measurements.clean();
	}
	
	protected void choosePort(String port)
	{
		chosen_port = port;
		portTextField.setText(chosen_port);
		connectButton.setEnabled(true);
	}
	
	public static MeasTablePanel getMeas_table_panel()
	{
		return meas_table_panel;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) 
	{
		closeApp();
		dispose();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}