import java.util.Vector;


public class sigNode {
	signature leftSig;
	signature rightSig;
	sigTable nexttable;
	Vector<Long> positions;
	
	public sigNode ()
	{
		leftSig = new signature();
		rightSig = new signature();
		positions = new Vector();
		nexttable = new sigTable();
	}
	
	public boolean noNextLevel()
	{
		return ( nexttable.size() == 0 );
	}
	
	public signature getLeftSig()
	{
		return leftSig;
	}
	
	public signature getRightSig()
	{
		return rightSig;
	}
	
	public long getPositionAt(int index)
	{
		return positions.get(index);
	}
	
	public int size()
	{
		return positions.size();
	}
	
	public void setSigs( signature sigl, signature sigr, long posqgram )
	{
		leftSig = sigl;
		rightSig = sigr;
		positions.add( posqgram );
	}
	
	public boolean equals( signature sigl, signature sigr )
	{
		return( leftSig.equals(sigl) && rightSig.equals(sigr) );
	}
}
