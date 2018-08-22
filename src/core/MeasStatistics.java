package core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import display.StatisticsPanel;

public class MeasStatistics 
{
	StatisticsPanel statistics_panel;
	ArrayList<Long> signal_timestamps;
	ArrayDeque<Long> signals_5min;
	ArrayDeque<Long> signals_30min;
	Date time_start;
	int total_signal_count;
	long uptime;
	long timestamp_now;
	double average_total;
	double average_5min;
	double average_30min;
	
	public MeasStatistics(StatisticsPanel statisticsPanel)
	{
		signal_timestamps = new ArrayList<Long>();
		signals_5min = new ArrayDeque<Long>();
		signals_30min = new ArrayDeque<Long>();
		total_signal_count = 0;
		uptime = -1;
		time_start = null;
		statistics_panel = statisticsPanel;
	}
	
	public void setTimeStart(long timestamp_start, long local_timestamp)
	{
		time_start = new Date(timestamp_start-local_timestamp);
	}
	
	public void addCounts(Measurement[] new_measurements, int new_measurements_count)
	{
		long temp_timestamp;
		for(int i=0; i<new_measurements_count; ++i)
		{
			temp_timestamp = new_measurements[i].getTimestamp();
			signal_timestamps.add(temp_timestamp);
			signals_5min.addLast(temp_timestamp);
			signals_30min.add(temp_timestamp);
		}
		total_signal_count += new_measurements_count;
	}

	public void update() 
	{
		if(time_start == null)
			return;
		
		timestamp_now = Calendar.getInstance().getTimeInMillis();
		uptime = timestamp_now - time_start.getTime();
		validate5min();
		validate30min();
		calculateAverages();
		statistics_panel.updateStatistics(uptime, total_signal_count, average_total, average_5min, average_30min);
	}

	private void calculateAverages() 
	{
		//Total average
		average_total = (double)signal_timestamps.size()/uptime;
//		System.out.println("Średnia liczba zliczeń (całkowita):\t\t" + total*1000);
		
		//5 minutes average
		if(uptime < 300000)
			average_5min = (double)signals_5min.size()/uptime;
		else
			average_5min = (double)signals_5min.size()/300000;
//		System.out.println(("Średnia liczba zliczeń (ostatnie 5 minut):\t" + av_5min*1000));
		
		//30 minutes average
		if(uptime < 1800000)
			average_30min = (double)signals_30min.size()/uptime;
		else
			average_30min = (double)signals_30min.size()/1800000;
	}

	private void validate5min() 
	{
		long edge = timestamp_now - 300000;
		Iterator<Long> it = signals_5min.iterator();
		
		while(it.hasNext())
			if(it.next() < edge)
				it.remove();
			else
				break;
	}

	private void validate30min() 
	{
		long edge = timestamp_now - 1800000;
		Iterator<Long> it = signals_30min.iterator();
		
		while(it.hasNext())
			if(it.next() < edge)
				it.remove();
			else
				break;
	}
}
