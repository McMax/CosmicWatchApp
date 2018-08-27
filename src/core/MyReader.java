package core;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.Vector;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class MyReader implements Runnable
{
	static Measurements measurements;
	CommPortIdentifier portIdentifier;
	CommPort commPort;
	SerialPort serialPort;
	InputStream in;
	SerialReader serialreader;
	boolean connected = false;
	Thread reader_thread;
	
	public MyReader(Measurements cw_meas)
	{
		measurements = cw_meas;
	}
	
	public void run()
	{
		try
		{
			Thread.sleep(5000);
		}
		catch (InterruptedException e) {e.printStackTrace();}
	}

	public boolean connect ( String portName ) throws Exception
    {
        portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Blad: Port jest w uzyciu");
            return (connected = false);
        }
        else
        {
            commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                               
                reader_thread = new Thread(this);
                reader_thread.start();
                
                serialPort.addEventListener(serialreader = new SerialReader(in));
                serialPort.notifyOnDataAvailable(true);
                return (connected = true);
            }
            
            return (connected = false);
        }     
    }
	
	public void disconnect()
	{
		if(commPort == null)
			return;
		
		serialPort.notifyOnDataAvailable(false);
		serialPort.removeEventListener();
		serialreader.clean();
		in = null;
		commPort.close();
		commPort = null;
		connected = false;
		try 
		{
			reader_thread.join();
			System.out.println("Reading stopped");
		} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	public static class SerialReader implements SerialPortEventListener 
    {
        private InputStream in;
        private byte[] buffer = new byte[1024];
        
        public SerialReader(InputStream in)
        {
            this.in = in;
        }
        
        public void serialEvent(SerialPortEvent arg0)
        {
        	int data;
        	long currentTimestamp = Calendar.getInstance().getTimeInMillis();

        	try
        	{
        		int len = 0;
        		while ( ( data = in.read()) > -1 )
        		{
        			if ( data == '\n' ) {
        				break;
        			}
        			buffer[len++] = (byte) data;
        		}

        		StringTokenizer tokenizer = new StringTokenizer(new String(buffer,0,len));
        		if(tokenizer.countTokens() != 6)
        		{
        			System.out.println("Bledne dane (" + tokenizer.countTokens() + " tokenow)");
        			return;
        		}
        		
        		parseData(currentTimestamp, tokenizer);
        	}
        	catch ( IOException e )
        	{
        		e.printStackTrace();
//        		System.exit(-1);
        	}
        }
        
        public void clean()
        {
        	buffer = new byte[1024];
        	in = null;
        }
    }
	
	private static void parseData(long currentTimestamp, StringTokenizer tokenizer)
	{
		long timestamp = currentTimestamp;
		long count = Integer.parseInt(tokenizer.nextToken());
		long local_timestamp = Integer.parseInt(tokenizer.nextToken());
		short adc = Short.parseShort(tokenizer.nextToken());
		double sipm_voltage = Double.parseDouble(tokenizer.nextToken());
		long dead_time = Integer.parseInt(tokenizer.nextToken());
		
//		System.out.println("Pomiar: " + timestamp + " " + count + " " + local_timestamp + " " + adc + " " + sipm_voltage + " " + dead_time);
//		System.out.println("\t\trozpoczecie pomiaru: " + (timestamp - local_timestamp));
		
		measurements.add(new Measurement(timestamp, count, local_timestamp, adc, sipm_voltage, dead_time));
	}
	
	public static Vector<String> getAvailableSerialPorts() 
	{
		//Taken from http://rxtx.qbang.org/wiki/index.php/Discovering_available_comm_ports
        Vector<String> portsList = new Vector<String>();
        @SuppressWarnings("rawtypes")
		java.util.Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
        while (thePorts.hasMoreElements()) {
            CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
            switch (com.getPortType()) {
            case CommPortIdentifier.PORT_SERIAL:
                try {
                    CommPort thePort = com.open("CommUtil", 50);
                    thePort.close();
                    portsList.add(com.getName());
                } catch (PortInUseException e) {
                    System.out.println("Port, "  + com.getName() + ", is in use.");
                } catch (Exception e) {
                    System.err.println("Failed to open port " +  com.getName());
                    e.printStackTrace();
                }
            }
        }
        return portsList;
    }
	
	public boolean isConnected()
	{
		return connected;
	}
}
