import java.util.Vector;


public class uncertainChar {
	//Vector<String> chars;
	//Vector prob;
	char[] chars;
	double[] prob;
	int size;
	
	public uncertainChar()
	{
		//chars = new Vector();
		//prob = new Vector();
		chars = new char[0];
		prob = new double[0];
		int size = 0;
	}
	
	public uncertainChar( Vector<Character> chs, Vector<Double> p )
	{
		size = chs.size();
		chars = new char[size];
		prob = new double[size];
		for( int i =0; i<size; i++)
		{
			chars[i] = chs.get(i);
			prob[i] = p.get(i);
		}
	}
	
	public uncertainChar( char ch )
	{//deterministic constructor
		chars = new char[1];
		prob = new double[1];
		chars[0] = ch;
		prob[0] = 1;
		size = 1;
	}
	
	public uncertainChar( uncertainChar ch )
	{
		size = ch.size();
		chars = new char[size];
		prob = new double[size];
		for( int i =0; i < size; i++)
		{
			chars[i] = ch.get(i);
			prob[i] = ch.prob[i];
		}
	}
	
	
	public uncertainChar( String chs, double[] p )
	{
		/*
		chars = new Vector();
		prob = new Vector();
		int len = chs.length();
		for( int i = 0; i < len; i++ )
		{
			chars.add( chs.charAt(i) );
			prob.add( p[i] );
		}
		*/
		int len = chs.length();
		chars = new char[len];
		prob = new double[len];
		for( int i = 0; i < len; i++)
		{
			chars[i] = chs.charAt(i);
			prob[i] = p[i];
		}
		size = len;
	}
	
	public void resetFromString( String strtext )
	{//construct from #a0.5b0.3c0.2#ab format
		
	}
	
	public char get( int index )
	{
		char c = chars[index];
		return c;
	}
	
	public int size()
	{
		return size;
	}
}
