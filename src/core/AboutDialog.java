package core;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

public class AboutDialog extends JDialog 
{
	private static final long serialVersionUID = 6710228128301601675L;

	String about_text = "Aplikacja jest częścią projektu CosmicWatch.\n\n" +
			"CosmicWatch jest projektem edukacyjnym prowadzonym przez dwójkę doktorantów, Spencera Axani z\n" + 
			"Massachusetts Institute of Technology (MIT) oraz Katarzynę Frankiewicz z Narodowego Centrum\n" + 
			"Badań Jądrowych (NCBJ) pod kierunkiem prof. Janet Conrad (MIT). Dotyczy on prostych w użyciu i\n" + 
			"łatwych w budowie detektorów, opartych na plastikowych scyntylatorach i fotopowielaczach\n" + 
			"krzemowych (SiPM). Detektory te zasilane są przez zwykły kabel USB (5V), mają niski pobór mocy\n" + 
			"(~0.5 W), małe rozmiary (7x7x4 cm) i niewielką wagę. W projekcie dostępne jest również łatwe w\n" + 
			"obsłudze oprogramowanie dla detektorów (Python, Arduino), możliwość podłączenia do oscyloskopu,\n" + 
			"wbudowana pamięć (karta microSD) oraz przyjazny wyświetlacz OLED oraz aplikacja internetowa\n" + 
			"umożliwiająca zbieranie i wizualizację danych.\n\n" + 
			"Detektory CosmicWatch posiadają bardzo wiele różnych zastosowań edukacyjnych, od nauki budowy i\n" + 
			"projektowania detektorów, przez pomiary mionów kosmicznych, pomiary różnych źródeł\n" + 
			"promieniowania, naukę programowania mikrokontrolerów, analizy danych, statystyki, podstaw\n" + 
			"elektroniki, lutowania, projektowania obwodów, tworzenia oprogramowania i wizualizacji danych.\n" + 
			"Będą one wykorzystywane w zajęciach edukacyjnych dla szkół prowadzonych przez Dział Edukacji i\n" + 
			"Szkoleń Narodowego Centrum Badań Jądrowych.\n\n" + 
			"Lista komponentów użytych do budowy detektora:\n" + 
			"- fotopowielacz krzemowy (SensL C-series 6 mm×6 mm Micro-FC 60035)\n" + 
			"- scyntylator plastikowy (5x5x1 cm)\n" + 
			"- płytka drukowana (własnego projektu)\n" + 
			"- 16 MHz Arduino Nano ATmega328\n" + 
			"- 0.96” wyświetlacz OLED\n" + 
			"- aluminiowa obudowa\n" + 
			"- czytnik kart microSD\n" + 
			"- konektor BNC\n\n" + 
			"Więcej informacji dotyczących detektorów możne być znaleziona na stronie internetowej projektu\n" + 
			"(www.cosmicwatch.lns.mit.edu).\n\n-----------------------------------------------\n\n"
			+ "Autor aplikacji: Bartosz Maksiak (Bartosz.Maksiak@ncbj.gov.pl)\n"
			+ "Najnowsza wersja aplikacji zawsze dostępna pod adresem: https://github.com/McMax/CosmicWatchApp\n\n"
			+ "Aplikacja, do połączenia z detektorem, wykorzystuje bibliotekę RXTX\n"
			+ "RXTX binary builds provided as a courtesy of Fizzed, Inc. (http://fizzed.com/).\n" + 
			"Please see http://fizzed.com/oss/rxtx-for-java for more information.";
	
	DefaultStyledDocument about_document;
	JButton ok_button;
	JTextArea text_area;
	
	public AboutDialog(AppFrame parentFrame)
	{
		super(parentFrame, "Informacje");
		setBounds(parentFrame.getLocation().x + 50, parentFrame.getLocation().y + 50, 600, 800);
		setResizable(false);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel main_panel = new JPanel(new GridLayout(1,1));
		main_panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		
		about_document = new DefaultStyledDocument();
		
		try 
		{
			about_document.insertString(0, about_text, null);
		} 
		catch (BadLocationException e) {e.printStackTrace();}
		
		text_area = new JTextArea(about_document);
		text_area.setEditable(false);
		ok_button = new JButton("OK");
		ok_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				AboutDialog.this.dispose();
			}
		});
		
		main_panel.add(text_area);
		getContentPane().add(main_panel,BorderLayout.CENTER);
		getContentPane().add(ok_button,BorderLayout.SOUTH);
		
		setVisible(true);
	}
}
