package core;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class PortChooser extends JDialog
{
	private static final long serialVersionUID = -7365521050458319711L;

	JList<String> port_list;
	JButton OK_button, Cancel_button;
	JPanel button_panel;
	JLabel label;
	JScrollPane scroll_pane;
	
	public PortChooser(AppFrame parentFrame) throws HeadlessException 
	{
		super(parentFrame, "Wybierz port");
		setBounds(parentFrame.getLocation().x + 5, parentFrame.getLocation().y + 10, 200, 300);
		setResizable(false);
		setLayout(new BorderLayout());
		
		label = new JLabel("Lista dostępnych portów");
		label.setBorder(new EmptyBorder(10, 10, 5, 10));
		getContentPane().add(label,BorderLayout.NORTH);
		
		port_list = new JList<String>(MyReader.getAvailableSerialPorts());
		scroll_pane = new JScrollPane(port_list);
		scroll_pane.setBorder(new EmptyBorder(5, 0, 5, 0));
		getContentPane().add(scroll_pane,BorderLayout.CENTER);
		
		OK_button = new JButton("Wybierz");
		OK_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(!port_list.isSelectionEmpty())
				parentFrame.choosePort(port_list.getSelectedValue());
				dispose();
			}
		});
		Cancel_button = new JButton("Anuluj");
		Cancel_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				dispose();
			}
		});
		button_panel = new JPanel(new GridLayout(1,2));
		button_panel.setBorder(new EmptyBorder(5, 0, 10, 0));
		button_panel.add(OK_button);
		button_panel.add(Cancel_button);
		getContentPane().add(button_panel,BorderLayout.SOUTH);
		
		setVisible(true);
	}
}
