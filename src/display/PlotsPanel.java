package display;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;

import core.AppFrame;
import core.Measurement;
import core.Measurements;

public class PlotsPanel extends JPanel implements Runnable
{
	private static final long serialVersionUID = -2015775801560270667L;
	
	AppFrame parent_frame;
	Measurements measurements;
	Measurement new_measurements[];
	int new_measurements_count;
	long last_measurement_time = -1;
	boolean taking_data = false;
	Thread display_thread;
	boolean thread_running = false;
	
	JPanel four_charts;
	
	ChartPanel chartpanel_signals_in_time, chartpanel_signals_per_sample, chartpanel_signal_intervals, chartpanel_average, chartpanel_amplitude;
	JFreeChart chart_signals_in_time, chart_signals_per_sample, chart_signal_intervals, chart_average, chart_amplitude;
	
	SignalsInTime dataset_signals_in_time;
	SignalsPerSample dataset_signals_per_sample;
	SignalIntervals dataset_signal_intervals;
	AverageOverTime dataset_average_over_time;
	Amplitude dataset_amplitude;
	
	public PlotsPanel(Measurements cw_meas, AppFrame parentFrame)
	{
		setLayout(new BorderLayout());
		measurements = cw_meas;
		parent_frame = parentFrame;

		//Upper panel
		dataset_signals_in_time = new SignalsInTime();
		chart_signals_in_time = ChartFactory.createTimeSeriesChart("Liczba zliczeń rejestrowanych na każdą sekundę", "Czas", "Zliczenia", dataset_signals_in_time, true, true, false);
		chartpanel_signals_in_time = new ChartPanel(chart_signals_in_time);
		chartpanel_signals_in_time.setPreferredSize(new Dimension(800, 300));
		chartpanel_signals_in_time.setBorder(BorderFactory.createEmptyBorder(10,10,5,10));
		
		this.add(chartpanel_signals_in_time,BorderLayout.NORTH);
		
		//Lower panel
		//Upper left sub-panel
		dataset_signals_per_sample = new SignalsPerSample("Liczba zliczeń na interwał");
		chart_signals_per_sample = ChartFactory.createHistogram("Liczba zliczeń na interwał", "Zliczenia na interwał", "Liczba wystąpień", dataset_signals_per_sample, PlotOrientation.VERTICAL, true, true, false);
		modifyChartLook(chart_signals_per_sample);
        
        //Upper right sub-panel
        dataset_signal_intervals = new SignalIntervals("Czas przerw pomiędzy zliczeniami");
        chart_signal_intervals = ChartFactory.createHistogram("Czas przerw pomiędzy zliczeniami", "Czas przerw pomiędzy zliczeniami (s)", "Liczba wystąpień", dataset_signal_intervals, PlotOrientation.VERTICAL, true, true, false);
        modifyChartLook(chart_signal_intervals);
        
        //Lower left sub-panel
        dataset_average_over_time = new AverageOverTime();
        chart_average = ChartFactory.createTimeSeriesChart("Średnia liczba zliczeń w czasie", "Czas", "Średnia", dataset_average_over_time, true, true, false);
        
        //Lower right sub-panel
        dataset_amplitude = new Amplitude("Amplituda sygnału");
		chart_amplitude = ChartFactory.createHistogram("Amplituda sygnału", "Amplituda (mV)", "Liczba zliczeń", dataset_amplitude, PlotOrientation.VERTICAL, true, true, false);
		
		//Creation of panel with four smaller charts
		four_charts = new JPanel(new GridLayout(2, 2));
		four_charts.setBorder(new EmptyBorder(10, 10, 10, 10));
		chartpanel_signals_per_sample = new ChartPanel(chart_signals_per_sample);
		chartpanel_signal_intervals = new ChartPanel(chart_signal_intervals);
		chartpanel_average = new ChartPanel(chart_average);
		chartpanel_amplitude = new ChartPanel(chart_amplitude);

		chartpanel_signals_per_sample.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		chartpanel_signal_intervals.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		chartpanel_average.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		chartpanel_amplitude.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		four_charts.add(chartpanel_signals_per_sample);
		four_charts.add(chartpanel_signal_intervals);
		four_charts.add(chartpanel_average);
		four_charts.add(chartpanel_amplitude);
		
		this.add(four_charts, BorderLayout.CENTER);
		
		setPanelColors(0x6495ED);
	}

	public void runThread()
	{
		dataset_signals_in_time.clean();
		dataset_signals_per_sample.clearObservations();
		dataset_signal_intervals.clearObservations();
		dataset_amplitude.clearObservations();
		dataset_average_over_time.clean();
		display_thread = new Thread(this);
		thread_running = true;
		display_thread.start();
	}
	
	public void stopDisplaying()
	{
		if(!thread_running)
			return;
		
		thread_running = false;
		try
		{
			display_thread.join();
		} 
		catch (InterruptedException e) {e.printStackTrace();}
		
		System.out.println("Display stopped");
		taking_data = false;
		last_measurement_time = -1;
		new_measurements_count = 0;
	}
	
	public void run()
	{
		while(thread_running)
		{
			try
			{
				Thread.sleep(1000);
				new_measurements = getNewMeasurements();
				
				if((last_measurement_time == -1) && (new_measurements_count != 0))	//first measurement
				{
					taking_data = true;
					dataset_average_over_time.setTimeStampStart(new_measurements[0].getTimestamp());
				}
				
				if(new_measurements_count != 0)
					AppFrame.getMeas_table_panel().addMeasurements(new_measurements, new_measurements_count);
				
				updateSignalsInTimeChart();
				updateSignalsPerSample();
				updateSignalIntervals();
				updateAverageOverTime();
				updateAmplitude();
		
				this.validate();
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
				thread_running = false;
				stopDisplaying();
			}
		}
	}

	private Measurement[] getNewMeasurements()
	{
		new_measurements_count = measurements.countNewMeasurements();
		
		if(new_measurements_count == 0)
			return null;
		
		new_measurements = new Measurement[new_measurements_count];
		
		for(int i=0; i<new_measurements_count; ++i)
			new_measurements[i] = measurements.poll();
		
		return new_measurements;
	}

	private void updateSignalsInTimeChart()
	{
		if(taking_data)
			dataset_signals_in_time.addNewMeasurement(new Second(Calendar.getInstance().getTime()), new_measurements_count);
	}
	
	private void updateSignalsPerSample()
	{
		if(taking_data)
			dataset_signals_per_sample.addNewObservation(new_measurements_count);
	}
	
	private void updateSignalIntervals()
	{
		double intervals[];
		
		if(new_measurements_count == 0)
			return;
		
		if(last_measurement_time != -1)
		{
			intervals = new double[new_measurements_count];
			long tmp_timestamp;
			
			for(int i=0; i<new_measurements_count; ++i)
			{
				tmp_timestamp = new_measurements[i].getTimestamp();
				intervals[i] = ((double)tmp_timestamp - last_measurement_time)/1000;
				last_measurement_time = tmp_timestamp;
			}	
			
			dataset_signal_intervals.addNewObservations(intervals, new_measurements_count);			//Adding to histogram here
		}
		else //The case with first measurement
		{
			last_measurement_time = new_measurements[0].getTimestamp();	//Initiate last_measurement_time value
			
			if(new_measurements_count == 1) //If there was only one measurement, do nothing more
				return;
			else //If there were more measurements, create array without the first measurement  
			{
				intervals = new double[new_measurements_count-1];
				long tmp_timestamp;
				
				for(int i=1; i<new_measurements_count; ++i)
				{
					tmp_timestamp = new_measurements[i].getTimestamp();
					intervals[i-1] = ((double)tmp_timestamp - last_measurement_time)/1000;
					last_measurement_time = tmp_timestamp;
				}	
				
				dataset_signal_intervals.addNewObservations(intervals, new_measurements_count-1);	//Adding to histogram here
			}
		}
	}
	
	private void updateAverageOverTime()
	{
		if(!taking_data)
			return;
		
		if(new_measurements_count == 0)
			dataset_average_over_time.addNewZeroMeasurement();
		else
			dataset_average_over_time.addNewMeasurement(new_measurements[new_measurements_count-1].getTimestamp(),new_measurements_count);
	}
	
	private void updateAmplitude()
	{
		if((!taking_data) || new_measurements_count == 0)
			return;
		
		for(int i=0; i<new_measurements_count; ++i)
			dataset_amplitude.addNewObservation(new_measurements[i].getSipm_voltage());
	}
	
	private void modifyChartLook(JFreeChart chart)
	{
		XYPlot plot = (XYPlot) chart_signals_per_sample.getPlot();
        plot.setDomainZeroBaselineVisible(false);
        plot.getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	}
	
	private void setPanelColors(int rgb)
	{
		java.awt.Color panel_color = new java.awt.Color(rgb);
		chartpanel_signals_in_time.setBackground(panel_color);
		four_charts.setBackground(panel_color);
		chartpanel_signals_per_sample.setBackground(panel_color);
		chartpanel_signal_intervals.setBackground(panel_color);
		chartpanel_average.setBackground(panel_color);
		chartpanel_amplitude.setBackground(panel_color);
		
		chart_signals_in_time.setBackgroundPaint(panel_color);
		chart_signals_per_sample.setBackgroundPaint(panel_color);
		chart_signal_intervals.setBackgroundPaint(panel_color);
		chart_average.setBackgroundPaint(panel_color);
		chart_amplitude.setBackgroundPaint(panel_color);
		
		chart_signals_in_time.getPlot().setBackgroundPaint(Color.WHITE);
		chart_signals_per_sample.getPlot().setBackgroundPaint(Color.WHITE);
		chart_signal_intervals.getPlot().setBackgroundPaint(Color.WHITE);
		chart_average.getPlot().setBackgroundPaint(Color.WHITE);
		chart_amplitude.getPlot().setBackgroundPaint(Color.WHITE);
	}
	
	public boolean takingData()
	{
		return taking_data;
	}
}
