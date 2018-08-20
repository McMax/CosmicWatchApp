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
	
	public AverageOverTime()
	{
		super(TimeZone.getDefault());
		
		series_average = new TimeSeries("Średnia liczba zliczeń w czasie");
		clean();
		this.addSeries(series_average);
	}
	
	protected void addNewMeasurement(long last_timestamp, int count)
	{
		measurements_count+=count;
		series_average.add(new Second(Calendar.getInstance().getTime()), (double)measurements_count/last_timestamp*1000);
	}

	public void addNewZeroMeasurement()
	{
		Second currentSecond = new Second(Calendar.getInstance().getTime());
		long lastMeasurementSecond = series_average.getTimePeriod(getSeriesCount()-1).getMiddleMillisecond();
		
		series_average.add(currentSecond, (double)measurements_count/(currentSecond.getMiddleMillisecond() - lastMeasurementSecond)*1000);
	}
	
	public void clean()
	{
		series_average.clear();
		measurements_count = 0;
	}
}
