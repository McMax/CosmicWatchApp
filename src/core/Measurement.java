package core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Measurement 
{
	private long timestamp;
	private long count;
	private long local_timestamp;
	private short adc;
	private double sipm_voltage;
	private long dead_time;
//	private float tempC;	//Temperature is omitted due to parsing problems
	SimpleDateFormat hrdate = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
	
	public Measurement(long timestamp, long count, long local_timestamp, short adc, double sipm_voltage,
			long dead_time) 
	{
		super();
		this.timestamp = timestamp;
		this.count = count;
		this.local_timestamp = local_timestamp;
		this.adc = adc;
		this.sipm_voltage = sipm_voltage;
		this.dead_time = dead_time;
	}

	public long getTimestamp() 
	{
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) 
	{
		this.timestamp = timestamp;
	}
	
	public long getCount() 
	{
		return count;
	}
	
	public void setCount(long count) 
	{
		this.count = count;
	}
	
	public long getLocal_timestamp() 
	{
		return local_timestamp;
	}
	
	public void setLocal_timestamp(long local_timestamp) 
	{
		this.local_timestamp = local_timestamp;
	}
	
	public short getAdc() 
	{
		return adc;
	}
	
	public void setAdc(short adc) 
	{
		this.adc = adc;
	}
	
	public double getSipm_voltage() 
	{
		return sipm_voltage;
	}
	
	public void setSipm_voltage(double sipm_voltage) 
	{
		this.sipm_voltage = sipm_voltage;
	}
	
	public long getDead_time() 
	{
		return dead_time;
	}
	
	public void setDead_time(long dead_time) 
	{
		this.dead_time = dead_time;
	}
	
	public String convertToString(boolean human_readable_date, String chosen_endline)
	{
		if(human_readable_date)
		{
			return String.format("%s\t%d\t%d\t%d\t%f\t%d%s", hrdate.format(new Date(timestamp)), count, local_timestamp, adc, sipm_voltage, dead_time, chosen_endline);
		}
		else
			return String.format("%d\t%d\t%d\t%d\t%f\t%d%s", timestamp, count, local_timestamp, adc, sipm_voltage, dead_time, chosen_endline);
	}
	/*
	public float getTempC() {
		return tempC;
	}
	public void setTempC(float tempC) {
		this.tempC = tempC;
	}
	*/
}
