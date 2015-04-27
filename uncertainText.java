import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Vector;


public class uncertainText {
	static String textFileName;
	qGramList qgramlist;
	
	static int qValue;
	static int sigLength;
	//important
	static int maxPos;
	static int maxSigLevel;
	static int textLength;
	
	public uncertainText()
	{}
	
	public uncertainText( String str)
	{
		textFileName = str;
		qgramlist = new qGramList();
	//	sigtable = new sigTable();
		
	//	RandomAccessFile randomFile = null;  
	//	randomFile = new RandomAccessFile(textFileName, "r");  
	}
	
	public void setValues( int qv, int sigLen, int maxpos, int maxsiglevel )
	{
		qValue = qv;
		sigLength = sigLen;
		maxPos = maxpos;
		maxSigLevel = maxsiglevel;
	}
	
	public void buildIndex( ) throws IOException
	{	
		//qgramlist.setvalues(qValue, sigLength, maxPos, maxSigLevel, textFileName );
		uncertainStr qgram;
		uncertainStr ustrl;
		uncertainStr ustrr;
		uncertainStr ustrqgram;
		long qgrampos = 0;
		qgram = getUString( true , qgrampos, qValue );
		do
		{
			ustrr = getUString( true, qgrampos, qValue+sigLength*maxSigLevel );
			if( ustrr.size >= qValue )
				ustrr = ustrr.subUString( qValue, ustrr.size() );
			ustrl = getUString( false, qgrampos, sigLength*maxSigLevel);
			qgramlist.buildIndex(qgram, ustrl, ustrr, qgrampos);
			
			if( qgram.isCertainCharAt(0) )
				qgrampos += 1;
			else
			{
				qgrampos += qgram.size() * 3 + 2;
			}
			qgram = getUString( true , qgrampos, qValue );
		}while( qgram.size() == qValue );
	}
	
	
	
	public uncertainStr getUString( boolean isRightSide, long fromIndex, int number) throws IOException
	{
		uncertainStr ustr;
		uncertainChar uch;
		long fsize;
		char[] chs = new char[2];
		float f;
		Vector<Double> prob = new Vector();
		Vector<Character> chars = new Vector();
		int vsize;
		
		RandomAccessFile randomFile = null;  
		randomFile = new RandomAccessFile(textFileName, "r");  
		int byteread;
		byte[] bytes = new byte[1];
		byte[] bytesu = new byte[3];
		
		fsize = randomFile.length();

		randomFile.seek( fromIndex );
		ustr = new uncertainStr( number );
		int i;
		
		if( isRightSide ){
		for ( i = 0; i < number && (byteread = randomFile.read( bytes ) ) != -1; i++)
		{//
			if( (char)bytes[0] == '$' )
			{//uncertain char occurs
				byteread = randomFile.read( bytesu );
				do
				{
					chars.add( (char)bytesu[0] );
					
					chs[0] = (char) bytesu[1];
					chs[1] = (char) bytesu[2];
					f = Float.parseFloat( String.valueOf( chs ) )/100;//e.g. 33 in the file should be 0.33
					prob.add( (double)f );
					
					byteread = randomFile.read( bytesu );
				}
				while( (char)bytesu[0] != '$');
				uch = new uncertainChar( chars, prob);
				//pushback 2 
				randomFile.seek( randomFile.getFilePointer() - 2 );
			}
			else
			{
				uch = new uncertainChar( (char)bytes[0] );
			}
			ustr.add(uch);
		}
		}
		
		else
		{//read from fromindex to the left 'number' uncertainChar
			if( fromIndex == 0 )
			{	
				ustr.size = 0;
				return ustr;
			}
			randomFile.seek( fromIndex - 1 );
			for ( i = 0; i < number && randomFile.getFilePointer() != -1; i++)
			{//not reach the start of the file 
				randomFile.read( bytes );
				if( randomFile.getFilePointer() - 2 < 0)//no char ahead. this is the first char. just break;
				{
					if( (char)bytes[0] != '$' )
					{
						uch = new uncertainChar( (char)bytes[0] );
						ustr.add(uch);
						i++;
					}
					break;
				}
				else
				randomFile.seek( randomFile.getFilePointer() - 2 );
				
				if( (char)bytes[0] == '$' )
				{//uncertain char occurs
					randomFile.seek( randomFile.getFilePointer() - 2 );
					byteread = randomFile.read( bytesu );
					do
					{
						chars.add( (char)bytesu[0] );
						
						chs[0] = (char) bytesu[1];
						chs[1] = (char) bytesu[2];
						f = Float.parseFloat( String.valueOf( chs ) )/100;//e.g. 33 in the file should be 0.33
						prob.add( (double)f );
						
						if( randomFile.getFilePointer() - 6 < 0 )
						{//eg a$a12... just jump to a 
							randomFile.seek(randomFile.getFilePointer() - 3 );
							break;
						}
						else
						randomFile.seek( randomFile.getFilePointer() - 6 );
						
						byteread = randomFile.read( bytesu );
					}
					while( (char)bytesu[2] != '$');
					uch = new uncertainChar( chars, prob);
					//pushback 2 
					randomFile.seek( (randomFile.getFilePointer() - 2 > 0 ?
										randomFile.getFilePointer() - 2: 0 ) );
				}
				else
				{
					uch = new uncertainChar( (char)bytes[0] );
				}
				ustr.add(uch);
			}
		}
		
		randomFile.close();
	
		if( i < number )//got less than 'number' of uncertainChar, change ustr's size
			ustr.size = i;
		return ustr;
	}
}
