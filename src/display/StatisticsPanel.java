package display;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import core.MeasStatistics;

public class StatisticsPanel extends JPanel
{
	private static final long serialVersionUID = 8553913718118843142L;
	
	static MeasStatistics meas_statistics;
	
	GridBagConstraints gbc = new GridBagConstraints();
	JLabel detector_name_label, uptime_label, counts_label;
	JLabel average_counts_label, five_min_average_counts_label, thirty_min_average_counts;
	
	public StatisticsPanel()
	{
		super(new GridBagLayout());
		
		/*gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Nazwa detektora"),gbc);
		
		detector_name_label = new JLabel("<NAZWA>");
		gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_END;
		add(detector_name_label,gbc);*/
		
		gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.LINE_START; gbc.insets = new Insets(5, 10, 5, 10);
		add(new JLabel("Czas pomiaru"),gbc);
		
		uptime_label = new JLabel("0:00:00:00");
		gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_END;
		add(uptime_label,gbc);
		
		gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Liczba zliczeń "), gbc);
		
		counts_label = new JLabel("0");
		gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_END;
		add(counts_label, gbc);
		
		gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.CENTER;
		add(new JLabel("ŚREDNIA LICZBA ZLICZEŃ"), gbc);

		gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Całkowita"), gbc);
		
		average_counts_label = new JLabel("0");
		gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_END;
		add(average_counts_label, gbc);
		
		gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Ostatnie 5 minut"), gbc);
		
		five_min_average_counts_label = new JLabel("0");
		gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_END;
		add(five_min_average_counts_label, gbc);
		
		gbc.gridx = 0; gbc.gridy = 6; gbc.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Ostatnie 30 minut"), gbc);
		
		thirty_min_average_counts = new JLabel("0");
		gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_END;
		add(thirty_min_average_counts, gbc);
		
		meas_statistics = new MeasStatistics(this);
	}
	
	public static MeasStatistics getMeas_statistics()
	{
		return meas_statistics;
	}

	public void updateStatistics(long uptime, int total_signal_count, double average_total, double average_5min,
			double average_30min)
	{
		uptime_label.setText(convertUptimeDisplay(uptime));
		counts_label.setText(String.valueOf(total_signal_count));
		average_counts_label.setText(String.format("%4.3f", average_total*1000));
		five_min_average_counts_label.setText(String.format("%4.3f", average_5min*1000));
		thirty_min_average_counts.setText(String.format("%4.3f", average_30min*1000));
	}
	
	String convertUptimeDisplay(long uptime)
	{
		int days = (int) (uptime/86400000);
		int hours = (int)((uptime%86400000)/3600000);
		int minutes = (int)((uptime%3600000)/60000);
		int seconds = (int)((uptime%60000)/1000);
		
		return (String.format("%d:%02d:%02d:%02d", days, hours, minutes, seconds));
	}
}