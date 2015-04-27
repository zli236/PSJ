
public class signature {
	String sig;
	
	public signature( )
	{
		//sig = new Object[ maxsize ];
	}
	
	public signature( uncertainStr ustr )
	{
		uStrToSig( ustr );
	}
	
	public void uStrToSig( uncertainStr ustr )
	{
		//universalChar uni = new universalChar();
		int size = ustr.size();
		char[] chs = new char[size];
		//sig = new Object[ size ];
		for( int i =0; i<size; i++)
		{
			if( ustr.isCertainCharAt(i) )
				chs[i] = ustr.get(i).get(0);
				//sig[i] = ustr.get(i).get(0);
			else
				chs[i] = '$';
		}
		sig = String.valueOf(chs);
		sig = hash( sig );
	}
	
	public static String hash( String str)
	{
		return str;
	}
	
	public String getSig()
	{
		return sig;
	}
	
	public boolean equals( signature sig1)
	{
		return ( sig.equals(sig1) );
	}
}
