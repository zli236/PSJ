import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

	
public class qGramList extends uncertainText
{

	/* �ñ�����ʵ�ֱ�ͷ */

	private qGramNode Head=null;

	private qGramNode Tail=null;

	private qGramNode Pointer=null;

	private int Length = 0;
	
	//static int qValue;
	//static int sigLength;
	//important
	//static int maxPos;
	//static int maxSigLevel;
	//static String filename;
	//static int textLength;
	/*
	public void setvalues( int qv, int sig, int maxpost, int maxsiglevel, String filen )
	{
		qValue = qv;
		sigLength = sig;
		maxPos = maxpost;
		maxSigLevel = maxsiglevel;
		filename = filen;
	}
	*/
	public void pruneByDP( Vector<Long> result, pattern pat, int splitindex, int thresh, double probthresh ) throws IOException
	{
		int pos;
		long posqgram;
		int posr;
		int numl;
		int numr;
		String strl;
		String strr;
		String patl;
		String patr;
		DP DPL;
		DP DPR; 
		int preedl;
		int preedr;
		StringBuffer sb;
		uncertainText utext = new uncertainText( textFileName );
		uncertainStr ustrl;
		uncertainStr ustrr;
		int i;
		double tempprob = 0;
		
		//int tempsize = result.size();
		
		pos = pat.pos[ splitindex ];// pos of the split
		String split = pat.split[ splitindex ];
		for( int j = 0; j < result.size(); j++ ) 
		{// for all the positions in the result
			posqgram = result.get(j);
			
			//compute split's two-end
			
			posr = pos+split.length();
			patl = pat.patt.substring( 0, pos );
			patr = pat.patt.substring( posr );
			
			//compute qgram's two-end which is uncertainStr
			numl = patl.length() + thresh;
			numr =  qValue + patr.length() + thresh;
			
			//get ustrl and ustrr
				ustrl = utext.getUString(false, posqgram, numl );
				ustrr = utext.getUString(true,  posqgram, numr);//this includs the qgram
				ustrr = ustrr.subUString(qValue, ustrr.size());//cut the qgram out, just leave the right part
			
			//for all possible worlds of ustrl and ustrr
			int wsizel = 1;	
			for( i = 0; i < ustrl.size(); i++ )
			{//compute size of all possible cases of the left ustr
				if( ustrl.isCertainCharAt(i) )
					;
				else
					wsizel = ustrl.ustring[i].size() * wsizel;
			}
		//	if( i==0 )//ustrl.size == 0 no possible world
		//		wsizel = 0;
			
			char[][] strsl = new char[wsizel][];
			double[] probl = new double[wsizel];
			for( i =0; i<wsizel; i++ )
				probl[i] = 1;
			ustrl.possibleWorldAt(0, ustrl.size(), wsizel, strsl, probl);
			
			int wsizer = 1;	
			for( i = 0; i < ustrr.size(); i++ )
			{//compute size of all possible cases of the right part
				if( ustrr.isCertainCharAt(i) )
					;
				else
					wsizer = ustrr.ustring[i].size() * wsizer;
			}
		//	if( i==0 )//no possible world
			//	wsizer = 0;
			
			char[][] strsr = new char[wsizer][];
			double[] probr = new double[wsizer];
			for( i =0; i<wsizer; i++ )
				probr[i] = 1;
			ustrr.possibleWorldAt(0, ustrr.size(), wsizer, strsr, probr);
			
			for( i = 0; i < wsizel; i++ )
				for( int k = 0; k < wsizer; k++ )
				{
					strl = String.valueOf( strsl[i] );
					strr = String.valueOf( strsr[k] );
			
					//strl is reversed, no need to reverse now
					//reverse the left side part of pattern
					sb = new StringBuffer(patl);
					patl = sb.reverse().toString();
			
					DPL = new DP( patl, strl);
					DPL.computeEd(); //Compute ED of the left part for one possible case 
					preedl = DPL.preed( false, thresh);
					if( preedl > thresh )//already larger than thresh, just discard this possible world
					{
						continue;
					}				
			
					DPR = new DP( patr, strr);//for the right part
					DPR.computeEd();
					preedr = DPR.preed( false , thresh );
					if( preedl + preedr > thresh)//
					{
						continue;
					}
					else//satisfy the thresh
					{
						tempprob += probl[i] * probr[k];
					}
				}
			if( tempprob < probthresh )
			{// do not meet the probthresh, discard this position
				result.remove(j);//this will decrease the size of result, which will incerease the indicies of the following elemets
				j--;//from the above reason, j--
			}
			
			}

	} 
	
	
	public Vector<Long> search( pattern pat, int thresh, double probthresh) throws IOException
	{
		//search pattern (all splits) on the qgramlist, also using sigList to further check if find an exact same qgram 
		Vector<Long> finalResult = new Vector<Long>();
		Vector<Long> result = new Vector<Long>();
		
		qGramNode temp;
		String split;
		int i;
		for( i = 0 ; i < thresh+1; i++ )//for all the splits of pattern
		{
			split = pat.split[i];
			Pointer = null;
			temp = cursor();
			while( temp != null )
			{
				if( temp.qgram.equals(split) )
				{
					//do sig Compare on sigList
					result = temp.sigtable.search(  pat, thresh, i, 1  );
					
					//further check on the result using prob. bound DP
					;
					
					//further check on the result using complete DP
					pruneByDP( result, pat, i, thresh, probthresh );
					
					finalResult.addAll(result);
				}
					 temp = temp.next;
			}
		}
		return finalResult;
	}
	
	
	public void buildIndex( uncertainStr uqgram, uncertainStr ustrl, uncertainStr ustrr, long posintext ) throws IOException
	{
		int i;
		
		//get all possible worlds of the uncertain qgram
		//uncertainStr uqgram = new uncertainStr( ustr.subUString( posqgram, posqgram + qValue ) );
		
		int wsize = 1;	
		for( i = 0; i < uqgram.size(); i++ )
		{//compute size of all possible cases
			if( uqgram.isCertainCharAt(i) )
				;
			else
				wsize = uqgram.ustring[i].size() * wsize;
		}
		char[][] strs = new char[wsize][];
		double[] prob = new double[wsize];// useless here no need to initializiont with value 1
		uqgram.possibleWorldAt(0, qValue, wsize, strs, prob);
		//
		String str;
		qGramNode tempnode;
			
		for( i = 0; i < wsize; i++)
		{
			str = String.valueOf( strs[i] );
			tempnode = orderedInsert( str );
			tempnode.sigtable.insertSigs( ustrl, ustrr, posintext, 0 );
		}
	}
	
	
	public qGramNode orderedInsert( String strqgram )
	{//asc2 descending order insert a qgram
		qGramNode e = new qGramNode( strqgram );
		if ( Length == 0 )
		{ 
			Tail = e;
			Head = e;
		}
		else
		{	
			Pointer = null;
			qGramNode temp = cursor();
			while( temp != null )
			{
				if( e.asc2 < temp.asc2 )//should insert infront of temp
				{
					e.next = temp;
					if(Pointer == null)//if temp is head
					{
						e.next = Head;
						Head = e;
					}
					else//temp is not head
					{
						e.next = temp;
						Pointer.next = e;	
					}
					Length++;
					return e;
				}
				else if( e.asc2 > temp.asc2 )//continue the search
				{
					Pointer = temp;
					temp = temp.next;
				}
				else//asc2 equals
				{
					if( e.qgram.equals(temp.qgram) )//qgram equals, noting to do here
					{ 
						return temp;
					}
					else
					{
						Pointer = temp;
						temp = temp.next;
					}
				}
			}//while
			//still not insert, insert at the end
			Tail.next = e;
			Tail = e;
			e.next = null;
		}
			Length++;
			return e;
	}
	

	public void deleteAll()

	/* ����������� */

	{

	Head = null;

	Tail = null;

	Pointer = null;

	Length = 0;

	}

	public void reset()

	/* ����λ��ʹ��һ������Ϊ��ǰ��� */ 

	{

	Pointer = null;

	}

	public boolean isEmpty( )

	/* �ж������Ƿ�Ϊ�� */

	{

	return( Length == 0 );

	}

	public boolean isEnd()

	/* �жϵ�ǰ����Ƿ�Ϊ���һ����� */

	{

	if ( Length == 0 ) 

	throw new java.lang.NullPointerException();

	else if ( Length == 1 )

	return true;

	else

	return( cursor() == Tail );

	}

	public String nextNode()

	/* ���ص�ǰ������һ������ֵ����ʹ���Ϊ��ǰ��� */ 

	{

	if ( Length == 1 )

	throw new java.util.NoSuchElementException();

	else if ( Length == 0 )

	throw new java.lang.NullPointerException();

	else

	{

	qGramNode temp = cursor();

	Pointer = temp;

	if ( temp != Tail )

	return( temp.next.qgram );

	else 

	throw new java.util.NoSuchElementException();

	}

	}

	public String currentNode()

	/* ���ص�ǰ����ֵ */ 

	{

	qGramNode temp = cursor();

	return temp.qgram;

	}

	
	
	public void insert( String d )

	/* �ڵ�ǰ���ǰ����һ����㣬��ʹ���Ϊ��ǰ��� */ 

	{
	qGramNode e = new qGramNode( d );

	if ( Length == 0 )

	{ 

	Tail = e;

	Head = e;

	}

	else 

	{

	qGramNode temp = cursor();

	e.next = temp;

	if ( Pointer == null )

	Head = e;

	else

	Pointer.next = e;

	}

	Length++;

	}

	public int size()

	/* ��������Ĵ�С */

	{

	return ( Length );

	}

	public String remove()

	/* ����ǰ����Ƴ�������һ������Ϊ��ǰ��㣬 ����Ƴ�

	�Ľ�������һ����㣬���һ������Ϊ��ǰ��� */ 

	{

	String temp ;

	if ( Length == 0 )

	throw new java.util.NoSuchElementException();

	else if ( Length == 1 ) 

	{

	temp = Head.qgram;

	deleteAll();

	}

	else 

	{

	qGramNode cur = cursor();

	temp = cur.qgram;

	if ( cur == Head )

	Head = cur.next; 

	else if ( cur == Tail )

	{

	Pointer.next = null;

	Tail = Pointer;

	reset();

	}

	else 

	Pointer.next = cur.next;

	Length--;

	}

	return temp;

	}

	private qGramNode cursor()

	/* ���ص�ǰ����ָ�� */ 

	{

	if ( Head == null )

	throw new java.lang.NullPointerException();

	else if ( Pointer == null )

	return Head;

	else 

	return Pointer.next;

	}
}
