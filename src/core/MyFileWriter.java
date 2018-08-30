package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MyFileWriter 
{
	File file_to_save;
	BufferedWriter writer;
	
	public MyFileWriter()
	{
		file_to_save = null;
		writer = null;
	}
	
	public MyFileWriter(File fileToSave)
	{
		file_to_save = fileToSave;
	}
	
	public void Open()
	{
		if(file_to_save==null)
		{
			SuggestFileName();
		}
		try
		{
			file_to_save.createNewFile();
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_to_save),"utf-8"));
			System.out.println("Otwarto plik do zapisu\t" + file_to_save.getAbsolutePath());
		} 
		catch (IOException e) {e.printStackTrace();}
	}
	
	public void Close()
	{
		if(writer == null)
			return;
		
		try 
		{
			writer.close();
			System.out.println("Plik do zapisu zamknięty");
		}
		catch (IOException e) {e.printStackTrace();}
	}
	
	public void Write(Measurement[] measurements, int count)
	{
		try 
		{
			for(int i=0; i<count; i++)
				writer.write(measurements[i].convertToString(false));
		}
		catch (IOException e){e.printStackTrace();}
	}

	public File getFile_to_save() {
		return file_to_save;
	}

	public void setFile_to_save(File file_to_save) {
		this.file_to_save = file_to_save;
	}

	public String SuggestFileName() 
	{
		int i = 0;
		String suggested_file_name;
		do
		{
			suggested_file_name = String.format("Pomiary_%03d.txt", i++);
			file_to_save = new File(suggested_file_name);
		}
		while(file_to_save.exists());
		
		return suggested_file_name;
	}
}
