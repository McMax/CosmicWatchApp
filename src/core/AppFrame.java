package core;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import display.MeasTablePanel;
import display.PlotsPanel;
import display.StatisticsPanel;

public class AppFrame extends JFrame implements WindowListener
{
	private static final long serialVersionUID = 146571341364742862L;

	static PlotsPanel plots_panel;
	static MeasTablePanel meas_table_panel;
	static MyFileWriter file_writer;
	ControlPanel control_panel;
	SavePanel save_panel;
	StatisticsPanel statistics_panel;
	AboutPanel about_panel;
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
		file_writer = new MyFileWriter();
		
		left_panel = new JPanel(new GridLayout(4,1));
		control_panel = new ControlPanel();
		control_panel.setBorder(BorderFactory.createTitledBorder("POŁĄCZENIE"));
		left_panel.add(control_panel);
		save_panel = new SavePanel();
		save_panel.setBorder(BorderFactory.createTitledBorder("ZAPIS DO PLIKU"));
		left_panel.add(save_panel);
		statistics_panel = new StatisticsPanel();
		statistics_panel.setBorder(BorderFactory.createTitledBorder("STATYSTYKI"));
		left_panel.add(statistics_panel);
		about_panel = new AboutPanel();
		about_panel.setBorder(BorderFactory.createTitledBorder("INFORMACJE"));
		left_panel.add(about_panel);
		
		this.getContentPane().add(left_panel,BorderLayout.WEST);

		tabbed_pane = new JTabbedPane();
		meas_table_panel = new MeasTablePanel(measurements);
		plots_panel = new PlotsPanel(measurements, this, StatisticsPanel.getMeas_statistics(), file_writer);
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
			chooseFileButton.addActionListener(new SaveAction());
			
			save_checkbox = new JCheckBox("Zapis", false);
			save_checkbox.addActionListener(new SaveAction());
			
			filename_label = new JLabel("Nazwa pliku");
			
			gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.insets = new java.awt.Insets(5, 2, 5, 2); gbc.anchor = GridBagConstraints.LINE_START;
			add(chooseFileButton,gbc);
			gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_END;
			add(save_checkbox, gbc);
			gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
			add(filename_label,gbc);
		}
	}
	
	class AboutPanel extends JPanel
	{
		private static final long serialVersionUID = 7169615757193973199L;
		
		JButton about_button;
		
		public AboutPanel()
		{
			super(new FlowLayout());
			
			about_button = new JButton("O aplikacji");
			about_button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					new AboutDialog(AppFrame.this);
				}
			});
			
			add(about_button);
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
					
					//Making changes in file saving section impossible
					chooseFileButton.setEnabled(false);
					save_checkbox.setEnabled(false);
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
				
				//Making changes in file saving section possible again
				chooseFileButton.setEnabled(true);
				save_checkbox.setEnabled(true);
			}
		}
	}
	
	class SaveAction extends AbstractAction
	{
		private static final long serialVersionUID = -5244765271097285014L;

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==chooseFileButton)
			{
				JFileChooser file_chooser = new JFileChooser();
				File file_to_save;

				while(true)		//Stupid solution, but works when user clicks "No" in confirm dialog
				{
					if(file_chooser.showSaveDialog(AppFrame.this) == JFileChooser.APPROVE_OPTION)
					{
						file_to_save = file_chooser.getSelectedFile();

						if(file_to_save.exists())
						{
							int choosen_option = JOptionPane.showConfirmDialog(AppFrame.this, "Wybrany plik istnieje. Czy nadpisać?");

							if(choosen_option == JOptionPane.YES_OPTION) {}
							else if(choosen_option == JOptionPane.NO_OPTION)
								continue;
							else if(choosen_option == JOptionPane.CANCEL_OPTION)
								return;
							else
								return;
						}

						file_writer.setFile_to_save(file_to_save);
						filename_label.setText(file_to_save.getName());
						save_checkbox.setSelected(true);
						break;
					}
				}
			}
			else if(e.getSource()==save_checkbox)
			{
				if(filename_label.getText().equals("Nazwa pliku"))
					filename_label.setText(file_writer.SuggestFileName());
			}
		}
	}
	
	void closeApp()
	{
		serialreader.disconnect();
		file_writer.Close();
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
	
	public boolean isSavingToFile()
	{
		return save_checkbox.isSelected();
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
