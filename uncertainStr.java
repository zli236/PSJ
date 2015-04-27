import java.util.Vector;


public class uncertainStr {
	//Vector< uncertainChar > ustring;
	//Object[] ustring;
	uncertainChar[] ustring;
	int size;
	
	public uncertainStr ( int maxsize )
	{
		//ustring = new Vector();
		//ustring = new Object[maxsize];
		ustring = new uncertainChar[maxsize];
		size = 0;
	}
	
	public uncertainStr ( uncertainStr ustr )
	{
		size = ustr.size;
		ustring = new uncertainChar[ size ];
		for( int i =0; i < size; i++ )
			ustring[i] = new uncertainChar( ustr.ustring[i] );
	}
	
	public void add( uncertainChar ch )
	{//add to the end of the array(Vector) ustring
		//ustring.add(o);
		uncertainChar temp = new uncertainChar( ch );
		ustring[ size ] = temp;
		size++;
	}
	
	public boolean isCertainCharAt( int index )
	{
		return( ustring[index].size() == 1 );
	}
	
	public uncertainStr subUString( int fromIndex, int toIndex )
	{//截取sub Uncertain String
		/*
		uncertainStr str = new uncertainStr();
		str.ustring = (Vector) ustring.subList(fromIndex, toIndex);
		return str;
		*/
		uncertainStr ustr = new uncertainStr( toIndex-fromIndex );
		for( int i = fromIndex; i < toIndex ; i++)
			ustr.add( ustring[i] );
		return ustr;	
	}
	
	public void DFTAt( int fromIndex, int toIndex, int wsize, char[][] strs, double[] p, int level )
	{
		int currentpos = fromIndex + level;
		int ucharsize = ustring[currentpos].size();
		int iter = wsize/ucharsize;
		for( int i = 0; i < ucharsize; i++ )//遍历第一个uncertain char的possible world
		{
			for( int j = 0; j < iter; j++)
			{
				strs[i+ucharsize*j][level] = ustring[currentpos].get(i);
				p[i+ucharsize*j] = p[i+ucharsize*j] * ustring[currentpos].prob[i];
				if( level + 1 < toIndex )
					DFTAt( fromIndex, toIndex, wsize, strs, p, level+1 );
			}
		}
	}
	
	public void possibleWorldAt( int fromIndex, int toIndex, int wsize, char[][] strs, double[] p )
	{//用strs传出ustring 从fromIndex到toIndex的所有可能certain string;
		int temp = toIndex - fromIndex;
		for( int i = 0; i<wsize; i++ )
		{
			strs[i] = new char[ temp ]; 
		}
		if( size > 0 )
		DFTAt( fromIndex, toIndex, wsize, strs, p, 0 );
	}
	
	public uncertainChar get( int index )
	{
		return ustring[index];
	}
	
	public int size()
	{
		return size;
	}
}
