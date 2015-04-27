
public class qGramNode {
	String qgram;
	int asc2;
	qGramNode next;
	sigTable sigtable;
	
	public qGramNode(String str)
	{
		qgram = str;
		char ch = qgram.charAt(0);
		asc2 = (int)ch;
		
		next = null;
		sigtable = new sigTable();
	}
	
	public qGramNode( qGramNode node )
	{
		qgram = node.qgram;
		asc2 = node.asc2;
		next = node.next;
		sigtable = node.sigtable;
	}
}
