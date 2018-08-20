package tests;
import java.io.*;
import java.util.*;

import gnu.io.*;



public class SimpleRead2 implements Runnable{
    static CommPortIdentifier portId;
    static Enumeration portList;

    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;
    
    Calendar calendar = Calendar.getInstance();
    boolean processing;

    public static void main(String[] args) {
        portList = CommPortIdentifier.getPortIdentifiers();
        

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                 if (portId.getName().startsWith("COM")) {
			//                if (portId.getName().equals("/dev/term/a")) {
                    SimpleRead2 reader = new SimpleRead2();
                }
            }
        }
    }

    public SimpleRead2()
    {
    	try 
    	{
    		serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
    	} 
    	catch (PortInUseException e) {System.out.println(e);}

    	try
    	{
    		inputStream = serialPort.getInputStream();
    	} 
    	catch (IOException e) {System.out.println(e);}

    	serialPort.notifyOnDataAvailable(true);
    	
    	try
    	{
    		serialPort.setSerialPortParams(9600,
    				SerialPort.DATABITS_8,
    				SerialPort.STOPBITS_1,
    				SerialPort.PARITY_NONE);
    	}
    	catch (UnsupportedCommOperationException e) {System.out.println(e);}

    	readThread = new Thread(this);
    	readThread.start();
    }

    public void run()
    {
    	try
    	{
//    		long currentTime = Calendar.getInstance().getTimeInMillis();
    		byte[] readBuffer = new byte[128];
    		int numBytes = -1;

//    		System.out.print(currentTime + " ");
    		while((numBytes = inputStream.read(readBuffer)) > -1)
    		{
    			System.out.print(new String(readBuffer,0,numBytes));
    		}


    	} 
    	catch (IOException e) {System.out.println(e);}
    }
}
