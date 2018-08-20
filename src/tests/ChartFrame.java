package tests;

import java.awt.GridLayout;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class ChartFrame extends JFrame implements Runnable {

	ChartPanel panel;
	JFreeChart chart;
	TimeSeriesCollection dataset;
	TimeSeries series;
	RegularTimePeriod tick;
	
	public ChartFrame()
	{
		setSize(800,800);
		setLayout(new GridLayout(1,1));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		tick = RegularTimePeriod.createInstance(Second.class, Calendar.getInstance().getTime(), TimeZone.getDefault());
		
		series = new TimeSeries("Randomy");
		dataset = new TimeSeriesCollection(series, TimeZone.getDefault());
		
		chart = ChartFactory.createTimeSeriesChart("Randomy", "Czas", "Random", dataset);
		panel = new ChartPanel(chart);
		
		this.getContentPane().add(panel);
		this.setVisible(true);
		new Thread(this).start();
	}
	
	public static void main(String[] args) 
	{
		new ChartFrame();
		
	}

	public void run() 
	{
		while(true)
		{
			System.out.println(tick.next());
			series.add(new Second(Calendar.getInstance().getTime()),Math.random());
			this.validate();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
