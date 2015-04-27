import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class pattern {
	String patt;
	int k;//the E.D. threshhold
	String[] split;
	int pos[];
	int length;
	
	public pattern ( String filename, int thresh )throws IOException
	{//get pattern and splits from a file name filename; t is the E.D. threshold
		k = thresh;
		FileInputStream stream = new FileInputStream( filename );
		InputStreamReader reader = new InputStreamReader( stream );
		BufferedReader buffer = new BufferedReader( reader );
		String line;
		//first line should be the pattern
		//other lines are the splits
		line = buffer.readLine();
		patt = line.trim();
		length = patt.length();
		//line = line.trim();
		//int nextSpace = line.indexOf(" ");
		//int t = Integer.parseInt(line.substring(0,nextSpace));
		split = new String[k+1];
		pos = new int[k+1];
		int i = 0;
		while( (line = buffer.readLine()) != null && !line.equals("") )
		{
			pos[i] = Integer.parseInt(line.substring(0,1));
			split[i] = line.substring(1);
			i++;
		}
	}
	
	public void splitPattern()
	{
		System.out.println("Input the k+1 splits");
		
	}
	
}
