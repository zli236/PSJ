import java.io.IOException;
import java.util.Vector;


public class sigTable extends qGramList{
	Vector<sigNode> sigtable;

	public sigTable ()
	{
		sigtable = new Vector();
	}
	
	public int size()
	{
		return sigtable.size();
	}
	
	public sigNode getSigNodeAt( int index )
	{
		return sigtable.get(index);
	}

	public void insertSigsAtEnd( signature sigl, signature sigr, long posqgram )
	{
		sigNode temp = new sigNode( );
		temp.setSigs(sigl, sigr, posqgram);
		sigtable.add( temp );
	}
	
	public void insertSigs( uncertainStr ustrl, uncertainStr ustrr, long posintext, int level  ) throws IOException
	{//level starts from 0
		//get left and right sigs at level
		int posl;
		int posr;
		int posl0;
		int posr0;
		int i, j;
		signature sigl;
		signature sigr;
		int sizel;
		int sizer;
		sizel = ustrl.size();
		sizer = ustrr.size();
		
		posl0 = sigLength*(level);
		posr0 = sigLength*(level);
		posl = sigLength*(level+1);
		posr = sigLength*(level+1);
		
		if( posl  > sizel && posr > sizer )
		{
			sigl = new signature( ustrl.subUString( posl0, sizel ));
			sigr = new signature( ustrr.subUString( posr0, sizer) );
		}
		else if( posl > sizel )
		{
			sigl = new signature( ustrl.subUString( posl0, sizel));
			sigr = new signature( ustrr.subUString( posr0, posr));
		}
		else if( posr > sizer )
		{
			sigr = new signature( ustrr.subUString( posr0, sizer));
			sigl = new signature( ustrl.subUString( posl0, posl ));
		}
		else
		{
			sigl = new signature( ustrl.subUString( posl0, posl ));
			sigr = new signature( ustrr.subUString( posr0, posr));
		}
		
		//start insert the sigs and posintext
		int sizetable = size();
		int sizenodes;
		sigNode tempnode = new sigNode();
		if ( sizetable == 0 )
		{
			insertSigsAtEnd( sigl, sigr, posintext);
		}
		
		else
		{
			for( i = 0; 
				 i < sizetable && !sigtable.get(i).equals(sigl, sigr);  
				 i++ )
			{
				;
			}
			if( i == sizetable )
			{
				insertSigsAtEnd( sigl, sigr, posintext);
			}
			
			else
			{//met same sigs, insert 
				tempnode = sigtable.get(i);
				if( !tempnode.noNextLevel() )//has next level on node[i]
					tempnode.nexttable.insertSigs(ustrl, ustrr, posintext, level + 1 );
				else//no next level, direct insert posintext on this node
				{
					if( tempnode.size() < maxPos )//just insert the posintext
						tempnode.setSigs(sigl, sigr, posintext);//add this pos
					else
					{//exceed maxpos, need to expand the level;
						if( level == maxSigLevel )//if current level is 3, no need to expand
						{
							insertSigsAtEnd( sigl, sigr, posintext);
						}
						else//expand the sigs of all the positions
						{
							posl0 = posl;
							posr0 = posr;
							posl = posl + sigLength;
							posr = posr + sigLength;
							
							if( posl  > sizel && posr > sizer )
							{
								sigl = new signature( ustrl.subUString( posl0, sizel ));
								sigr = new signature( ustrr.subUString( posr0, sizer) );
							}
							else if( posl > sizel )
							{
								sigl = new signature( ustrl.subUString( posl0, sizel));
								sigr = new signature( ustrr.subUString( posr0, posr));
							}
							else if( posr > sizer )
							{
								sigr = new signature( ustrr.subUString( posr0, sizer));
								sigl = new signature( ustrl.subUString( posl0, posl ));
							}
							else
							{
								sigl = new signature( ustrl.subUString( posl0, posl ));
								sigr = new signature( ustrr.subUString( posr0, posr));
							}
						
							//insert current sig to the next level of table first
							tempnode.nexttable.insertSigsAtEnd(sigl, sigr, posintext);
							
							int len = tempnode.size();
							uncertainStr ustrtempl;
							uncertainStr ustrtempr;
							uncertainText utext = new uncertainText( textFileName );// for get expaned sigs in the uncertain text
							for( j = 0; j < len; j++)
							{//for all the positions that already exits in the tempnode
								long posq = tempnode.getPositionAt(j);
								//posl = posq;
							//	posr = posq + qValue + sigLength*(level+1);
								
								//get expand sigs from utext
								ustrtempl = utext.getUString( false, posq, sigLength*3 );
								//sigl.uStrToSig(ustrtemp);
								ustrtempr = utext.getUString( true, posq, sigLength*3 );
								//sigr.uStrToSig(ustrtemp);
								
								tempnode.nexttable.insertSigs(ustrtempl, ustrtempr, posintext, level+1);
							}
						}
					}
				}
			}
		}
	}
	
	public Vector<Long> search ( pattern patt, int thresh, int splitsindex, int level )
	{//sig compare with pattern,  level starts from 1
		Vector<Long> result = new Vector();
		String sigl = null;
		String sigr = null;
		String plsig = null;
		String prsig = null;
		int pos = patt.pos[splitsindex];
		int posl;
		int posr;
		DP DPL;
		DP DPR;
		int preed;
		StringBuffer sb;
		int sizetable = size();
		int i, j;
		sigNode tempnode;
		for( i = 0; i<sizetable; i++ )//for all the signode 
		{
			tempnode = sigtable.get(i);
			sigl = tempnode.leftSig.sig;
			sigr = tempnode.rightSig.sig;
			
			posl = pos - thresh - level*sigLength;
			posr = pos + patt.split[splitsindex].length();
			if( posl < 0 || (posr + thresh + level*sigLength) > patt.length )
			{// pattern exhausted
				for(  j = 0; j < sizetable; j++)
				{
					result.addAll( sigtable.get(j).positions );
				}
				break;
			}
			else
			{
				plsig = patt.patt.substring(posl, pos );
				prsig = patt.patt.substring(posr, posr + thresh + level*sigLength );
			}
			plsig = signature.hash( plsig );
			prsig = signature.hash( prsig );
			
			//Compute ED Between pattern's two-end sig and qgram's two-end sig
			//reverse the string on the left side
			sb = new StringBuffer(plsig);
			plsig = sb.reverse().toString();
			sb = new StringBuffer(sigl);
			sigl = sb.reverse().toString();
			
			DPL = new DP( plsig, sigl);
			DPR = new DP( prsig, sigr);
			
			DPL.computeEd();
			DPR.computeEd();
			
			preed = DPL.preed( true, thresh) + DPR.preed( true, thresh);
			if( preed  > thresh )
				continue;
			else//
			{
				if( tempnode.noNextLevel() )//if there's no further level
					result.addAll( tempnode.positions );
				else//continue to check the next level sigtable
				{
						result.addAll( tempnode.nexttable.search(patt, thresh, splitsindex, level+1) );
				}
			}
		}
		return result;
	}

}//end of class
