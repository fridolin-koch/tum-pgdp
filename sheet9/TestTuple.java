package sheet9;

/**
 * @author Frido Koch and the guy who wrote the exercise
 */
public class TestTuple 
{
    public static void main(String[] args)  
    {
    	//list one
    	List <Integer> lstOne = new List<Integer>(new Integer(1));
    	lstOne = lstOne.append(new Integer(2));
    	lstOne = lstOne.append(new Integer(3));
    	lstOne = lstOne.append(new Integer(4));
    	lstOne = lstOne.append(new Integer(5));
    	
    	//list two
    	List <String> lstTwo = new List<String>("A");
    	lstTwo = lstTwo.append("B");
    	lstTwo = lstTwo.append("C");
    	lstTwo = lstTwo.append("D");
    	lstTwo = lstTwo.append("E");
    	
    	//just output the lists
    	System.out.println("List one:");
    	System.out.println(lstOne);
    	
    	System.out.println("List two:");
    	System.out.println(lstTwo);
    	
    	List <Tuple<Integer, String>> zipped = Tuple.zip ( new Tuple <List<Integer>, List<String>>(lstOne, lstTwo) );
    	
    	if ( zipped.equals ( Tuple.zip ( Tuple.unzip ( zipped ) )))	{
    		System.out.println ("Part  1/2  correct !" );
    	}
    	
    	Tuple<List<Integer>, List<String>> unzipped = Tuple.unzip( zipped );
    	
    	if ( unzipped.equals( Tuple.unzip( Tuple.zip ( unzipped )) )) {
    		System.out.println ("Part  2/2  correct !" );
    	}
    	
    }
}
