package display;

import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;

//TODO Histogram powinien sie adaptowac do czasu samplowania 
public class SignalsPerSample extends SimpleHistogramDataset
{
	private static final long serialVersionUID = 4315271193824321413L;
	
	int bins = 0;
	
	public SignalsPerSample(@SuppressWarnings("rawtypes") Comparable key)
	{
		super(key);
		extendBinAxis(5);
	}

	protected void extendBinAxis(int max_val_bin)
	{
		while(bins < max_val_bin+1)	//New bins will be added to have the bin including 'max_val_bin' number
		{
			this.addBin(new SimpleHistogramBin(bins-0.5, bins+0.5,false,true));
			bins++;			
		}
	}

	public void addNewObservation(int value)
	{
		if(value > bins-1)
			extendBinAxis(value);
		
		this.addObservation(value);
	}
}
