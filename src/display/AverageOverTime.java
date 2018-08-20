package display;

import java.util.Calendar;
import java.util.TimeZone;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class AverageOverTime extends TimeSeriesCollection
{
	private static final long serialVersionUID = -330760056597482041L;

	TimeSeries series_average;
	int measurements_count;
	long timestamp_start;
	
	public AverageOverTime()
	{
		super(TimeZone.getDefault());
		
		series_average = new TimeSeries("Średnia liczba zliczeń w czasie");
		clean();
		this.addSeries(series_average);
	}
	
	protected void setTimeStampStart(long timestamp)
	{
		timestamp_start = timestamp;
	}
	
	protected void addNewMeasurement(long last_timestamp, int count)
	{
		measurements_count+=count;
		if(last_timestamp > timestamp_start)
			series_average.add(new Second(Calendar.getInstance().getTime()), (double)measurements_count/(last_timestamp - timestamp_start)*1000);
	}

	public void addNewZeroMeasurement()
	{
		Second currentSecond = new Second(Calendar.getInstance().getTime());
		series_average.add(currentSecond, (double)measurements_count/(currentSecond.getMiddleMillisecond() - timestamp_start)*1000);
	}
	
	public void clean()
	{
		series_average.clear();
		measurements_count = 0;
		timestamp_start = 0;
	}
}
