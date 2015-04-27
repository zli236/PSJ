public class DP {
    String X;
	String Y;
	int lenx;
	int leny;
	int[][] c;//matrix for computing edit distance;
	int[][] b;//matrix for tracking in DP;
	int ed;
	
	public DP( String x, String y)
	{
		X = x;
		Y = y;
		lenx = X.length();
		leny = Y.length();
		c = new int[lenx+1][leny+1];
		b = new int[lenx+1][leny+1];
		ed = 9999;
	}
	
	public int computeEd( )
	{
	  int i, j;
	  for( i = 0; i < lenx+1; i++)
		  c[i][0] = i;
	  for( j = 0; j < leny+1; j++)
		  c[0][j] = j;
	  
	  for( i = 1; i < lenx+1; i++)
		  for( j = 1; j < leny+1; j++)
		  {
		    if( X.charAt(i-1) == Y.charAt(j-1) )// ע���±�Ϊi-1,j-1 ��ΪX[0] Y[0] ���ŵ�һ��Ԫ��
			{
			  c[i][j] = c[i-1][j-1];
			  b[i][j] = 45;
			}
			else
			{
			  if( c[ i-1 ][ j ] <= c[ i ][ j-1 ] && c[ i-1 ][ j ] <= c[ i-1 ][ j-1 ] )
			  {
			    c[ i ][ j ] = c[ i-1 ][ j ] + 1 ;
				b[ i ][ j ] = 90;
			  }
			  else if( c[ i ][ j -1 ] <= c[ i-1 ][ j ] && c[ i ][ j-1 ] <= c[ i-1 ][ j-1 ] ) 
			  {
			    c[ i ][ j ] = c[ i ][ j -1 ] + 1;
				b[ i ][ j ] = 0;
			  }
			  else
			  {
				  c[i][j] = c[i-1][j-1] + 1;
				  b[i][j] = 45; 
			  }
			}//else
		  }
	  ed = c[lenx][leny];
	  return 1;
	}

	public int preed( boolean isreverse, int k )
	{//����x��y[leny-2k:leny]����С���룬 isreverse��ʾ�Ƿ񷴹���. leny-2k<0ʱȡy[1:leny]
		int min=9999;
		int pos;
		if( lenx == 0 && leny == 0 )
		{
			return 0;
		}
		else if( leny == 0 )
		{
			return lenx;
		}
		if ( isreverse )
		{
			if( lenx - 2*k > 0 )
			{
				pos = lenx - 2*k;
				for( int i = 0; i < 2*k + 1; i++ )
				{
					if( c[pos+i][leny] < min )
						min = c[pos+i][leny];	
				}
			}
			else
			{
				pos = 1;
				for( int i = 0; i < lenx; i++)
				{
					if( c[pos+i][leny] < min )
						min = c[pos+i][leny];
				}
			}
		}
		else
		{
			if( leny - 2*k > 0 )
			{
				pos = leny - 2*k;
				for( int i = 0; i < 2*k + 1; i++ )
				{
					if( c[lenx][pos+i] < min )
						min = c[lenx][pos+i];	
				}
			}
			else
			{
				pos = 1;
				for( int i = 0; i < leny; i++)
				{
					if( c[lenx][pos+i] < min )
						min = c[lenx][pos+i];
				}
			}
		}
		return min;
	}
	/*
	public int print_LCS( int i; int j)
	{
//	  b��LCS_Length�м�¼�м��Ķ�ά����
//	    X�ǵ�һ���ַ���
//	    i,j �ֱ��������X,Y�ĵ�i,j���ַ�  
	{
	  if( i == 0 || j == 0 )
	    return 0;
	  if( b[ i ][ j ] == 45 )
	  {
	    print_LCS( i-1, j-1 );
		System.out.print( X.charAt( i-1 ) ); //b[i][j] = 45 �� ��Ӧ�ڴ�ӡX�ĵ� i ��Ԫ�أ��±�Ϊi-1
	  }
	  else
	  {
	    if( b[ i ][ j ] == 90 ) 
			print_LCS( b, X, i-1, j );
		else
			print_LCS( b, X, i, j-1 );
	  }
	  return 0;
	}
*/
	
}
