package display;

import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;

public class Amplitude extends SimpleHistogramDataset 
{
	private static final long serialVersionUID = -7551048907413980038L;

	public Amplitude(@SuppressWarnings("rawtypes") Comparable key) 
	{
		super(key);
		createBinAxis(300, 60);
		setAdjustForBinSize(false);
	}

	protected void createBinAxis(double upper_limit, int nbins)
	{
		double one_bin_range = upper_limit/nbins;
		for(int i=0; i<nbins; i++)
			this.addBin(new SimpleHistogramBin(i*one_bin_range, (i+1)*one_bin_range, true, false));
	}

	public void addNewObservation(double value)
	{
		if(value > 300)
			System.out.println("Wykryto silny sygnał! " + value + " mV\nNie zostanie on dodany ze względu na ograniczenia histogramu.");
		else
			addObservation(value);
	}
	
	public void addNewObservations(double[] values, int count)
	{
		for(int i=0; i<count; ++i)
		{
			if(values[i] > 300)
				System.out.println("Wykryto silny sygnał! " + values[i] + " mV\nNie zostanie on dodany ze względu na ograniczenia histogramu.");
			else
				addObservation(values[i]);
		}
			
	}
}
