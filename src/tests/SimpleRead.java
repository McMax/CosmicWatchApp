package tests;
import java.io.*;
import java.util.*;

import gnu.io.*;



public class SimpleRead implements Runnable, SerialPortEventListener {
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
                    SimpleRead reader = new SimpleRead();
                }
            }
        }
    }

    public SimpleRead() {
        try {
            serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
        } catch (PortInUseException e) {System.out.println(e);}
        try {
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {System.out.println(e);}
	try {
            serialPort.addEventListener(this);
	} catch (TooManyListenersException e) {System.out.println(e);}
        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.setSerialPortParams(9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {System.out.println(e);}
        readThread = new Thread(this);
        readThread.start();
    }

    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {System.out.println(e);}
    }

    public void serialEvent(SerialPortEvent event)
    {
    	if(!processing)
    	{
    		processing = true;
    		if(event.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        	{
        		long currentTime = Calendar.getInstance().getTimeInMillis();
        		byte[] readBuffer = new byte[128];
        		int numBytes = -1;

                try
                {
                	System.out.print(currentTime + " ");
//                    while (inputStream.available() > 0)
                	while((numBytes = inputStream.read(readBuffer)) > 0)
                    {
//                        int numBytes = inputStream.read(readBuffer);
                		System.out.print(new String(readBuffer,0,numBytes));
                    }
//                    String message = new String(readBuffer);
//                    System.out.println(currentTime + " " + message.trim());
                    
                } catch (IOException e) {System.out.println(e);}
        	}
    		processing = false;
    	}
    	
    }
}
