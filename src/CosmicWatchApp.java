import core.AppFrame;
import core.Measurements;
import core.MyReader;

public class CosmicWatchApp
{
	volatile static Measurements CW_meas = new Measurements();
	MyReader serial_reader;
	AppFrame frame;

	public CosmicWatchApp()
	{
		serial_reader = new MyReader(CW_meas);
		
		frame = new AppFrame(CW_meas, serial_reader);		//Thread inside
		frame.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new CosmicWatchApp();
	}
}
