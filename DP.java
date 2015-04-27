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
		    if( X.charAt(i-1) == Y.charAt(j-1) )// 注意下标为i-1,j-1 因为X[0] Y[0] 存着第一个元素
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
	{//返回x与y[leny-2k:leny]的最小距离， isreverse表示是否反过来. leny-2k<0时取y[1:leny]
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
//	  b是LCS_Length中记录行迹的二维数组
//	    X是第一个字符串
//	    i,j 分别代表考察至X,Y的第i,j个字符  
	{
	  if( i == 0 || j == 0 )
	    return 0;
	  if( b[ i ][ j ] == 45 )
	  {
	    print_LCS( i-1, j-1 );
		System.out.print( X.charAt( i-1 ) ); //b[i][j] = 45 度 对应于打印X的第 i 个元素，下标为i-1
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
