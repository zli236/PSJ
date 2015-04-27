import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

public class demonstrate {

	public static void main(String[] argv) throws IOException
	{
		int thresh = 3;    
		double probthresh = 0.5;
		int qv = 3;
		int sigLen = 2;
		int maxpos = 3;
		int maxsiglevel = 3;

		pattern p = new pattern("pattern.txt", thresh);
		uncertainText t = new uncertainText( "text.txt");
		
		t.setValues(qv, sigLen, maxpos, maxsiglevel);
		t.buildIndex();
		Vector<Long> result = new Vector<Long>();;
		result = t.qgramlist.search(p, thresh, probthresh);
		System.out.print( result.toString() );
	}
}
