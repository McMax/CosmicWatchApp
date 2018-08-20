package core;
import java.util.concurrent.ArrayBlockingQueue;

public class Measurements
{
	ArrayBlockingQueue<Measurement> measurements = new ArrayBlockingQueue<Measurement>(100);
	Measurement last_measurement = null;
	public boolean hasData;
	
	public Measurements()
	{
		hasData = false;
	}
	
	protected void add(Measurement meas_in)
	{
		try {
			if(last_measurement != null)
			{
				if(!sanityCheck(meas_in))
				{
					System.out.println("Pomiar nie przeszedl testu sensownosci");
					return;
				}
				last_measurement = meas_in;
				measurements.put(meas_in);
			}
			else
			{
				last_measurement = meas_in;
				measurements.put(meas_in);
			}
			hasData = true;
		}
		catch (InterruptedException e) {e.printStackTrace();}
	}
	
	protected void add(long ts_in, long count_in, long local_ts_in, short adc_in, double sipm_v_in, long dt_in)
	{
		Measurement temp_meas = new Measurement(ts_in, count_in, local_ts_in, adc_in, sipm_v_in, dt_in); 
		try {
			if(last_measurement != null)
			{
				if(!sanityCheck(temp_meas))
				{
					System.out.println("Pomiar nie przeszedl testu sensownosci");
					return;
				}
				last_measurement = temp_meas;
				measurements.put(temp_meas);
			}
			else
			{
				last_measurement = temp_meas;
				measurements.put(temp_meas);
			}
			hasData = true;
		}
		catch (InterruptedException e){e.printStackTrace();}
	}
	
	public int countNewMeasurements()
	{
		return measurements.size();
	}
	
	public Measurement poll()
	{
		Measurement meas_out = measurements.poll();
		if(measurements.isEmpty())
			hasData = false;
		
		return meas_out;
	}
	
	private boolean sanityCheck(Measurement meas_in)
	{
		//Sprawdzenie licznika
		if(meas_in.getCount() < last_measurement.getCount())
			return false;
		if(meas_in.getCount() > (last_measurement.getCount() + 3))
			return false;
		
		return true;
	}
	
	public void clean()
	{
		measurements.clear();
		hasData = false;
		last_measurement = null;
	}
}
