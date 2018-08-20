package display;

import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;

public class SignalIntervals extends SimpleHistogramDataset
{
	private static final long serialVersionUID = -5712954058485693666L;

	int bins = 0;
	
	public SignalIntervals(@SuppressWarnings("rawtypes") Comparable key)
	{
		super(key);
		extendBinAxis(4);
	}

	protected void extendBinAxis(double max_val_bin)
	{
		while(bins < Math.ceil(max_val_bin))	//New bins will be added to have the bin including 'max_val_bin' number
		{
			this.addBin(new SimpleHistogramBin(bins, bins+1, false, true));
			bins++;			
		}
	}
	
	public void addNewObservation(double value)
	{
		if(value > bins-1)
			extendBinAxis(value);
		
		this.addObservation(value);
	}

	public void addNewObservations(double[] intervals, int count)
	{
		for(int i=0; i<count; ++i)
			addNewObservation(intervals[i]);
	}
}
