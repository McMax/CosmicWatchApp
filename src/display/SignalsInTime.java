package display;

import java.util.TimeZone;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class SignalsInTime extends TimeSeriesCollection
{
	private static final long serialVersionUID = 3271234112596300922L;

	TimeSeries series_sig_in_time;
	
	public SignalsInTime()
	{
		super(TimeZone.getDefault());
		
		series_sig_in_time = new TimeSeries("Zliczenia");
		this.addSeries(series_sig_in_time);
	}
	
	public void clean()
	{
		series_sig_in_time.clear();
	}

	public void addNewMeasurement(Second second, int new_measurements_count)
	{
		series_sig_in_time.add(second, new_measurements_count);
	}
}
