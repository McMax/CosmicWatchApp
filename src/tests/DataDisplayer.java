package tests;
import core.Measurement;
import core.Measurements;

public class DataDisplayer implements Runnable
{
	Measurements measurements;
	
	public DataDisplayer(Measurements cw_meas)
	{
		measurements = cw_meas;
		
		new Thread(this).start();
	}
	
	public void run()
	{
		try
		{
			while(true)
			{
				Thread.sleep(250);
				
				if(measurements.hasData)
				{
					Measurement meas = measurements.poll();
					System.out.println(meas.getTimestamp() + " " + meas.getCount() + " " + meas.getLocal_timestamp() + " "
										+ meas.getAdc() + " " + meas.getSipm_voltage() + " " + meas.getDead_time());
				}
			}
		} 
		catch (InterruptedException e) {e.printStackTrace();}
	}
}
