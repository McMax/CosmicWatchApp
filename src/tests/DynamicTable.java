package tests;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import core.Measurement;
import core.Measurements;

public class DynamicTable extends JTable implements Runnable
{
	private static final long serialVersionUID = 5228271939569044146L;
	
	DefaultTableModel dtm = new DefaultTableModel(0, 0);
	String tableHeader[] = new String[] {"Czas Unix", "Numer pomiaru", "Czas lokalny", "ADC", "SiPM (V)", "Czas martwy"};
	Measurements measurements;
	Measurement meas_temp;
	
	public DynamicTable(Measurements cw_meas)
	{
		dtm.setColumnIdentifiers(tableHeader);
		setModel(dtm);
		measurements = cw_meas;
	}
	
	protected void runThread()
	{
		new Thread(this).start();
	}
	
	protected void addRow(Object[] objects)
	{
		dtm.addRow(objects);
	}
	
	protected void addMeasurement(Measurement meas_in)
	{
		Object objects_row[] = new Object[] {meas_in.getTimestamp(), meas_in.getCount(), meas_in.getLocal_timestamp(), meas_in.getAdc(), meas_in.getSipm_voltage(), meas_in.getDead_time()};
		dtm.addRow(objects_row);
	}
	
	public void run() 
	{
		try
		{
			while(true)
			{
				Thread.sleep(250);
				
				if(measurements.hasData)
					addMeasurement(measurements.poll());
			}
		} 
		catch (InterruptedException e) {e.printStackTrace();}
	}

}
