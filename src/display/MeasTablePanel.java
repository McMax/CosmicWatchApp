package display;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import core.Measurement;
import core.Measurements;

public class MeasTablePanel extends JPanel 
{
	private static final long serialVersionUID = 2303648777908239861L;

	JTable meas_table;
	DefaultTableModel dtm = new DefaultTableModel(0, 6);
	String tableHeader[] = new String[] {"Czas Unix", "Numer pomiaru", "Czas lokalny", "ADC", "SiPM (V)", "Czas martwy"};
	Measurements measurements;
	
	public MeasTablePanel(Measurements meas)
	{
		super(new java.awt.GridLayout(1,1));
		
		dtm.setColumnIdentifiers(tableHeader);
		meas_table = new JTable(dtm);
		measurements = meas;
		
		add(new JScrollPane(meas_table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	}
	
	protected void addMeasurement(Measurement meas_in)
	{
		Object objects_row[] = new Object[] {meas_in.getTimestamp(), meas_in.getCount(), meas_in.getLocal_timestamp(), meas_in.getAdc(), meas_in.getSipm_voltage(), meas_in.getDead_time()};
//		dtm.addRow(objects_row);
		dtm.insertRow(0, objects_row);
	}
	
	protected void addMeasurements(Measurement[] meas_array, int size)
	{
		if(size==0)
			return;
		
		for(int i=0; i<size; ++i)
			addMeasurement(meas_array[i]);
	}
	
	public void cleanTable()
	{
		dtm.setRowCount(0);
	}
}
