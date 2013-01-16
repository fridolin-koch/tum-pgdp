package sheet9;

/**
 * @author Frido Koch
 */
public class Tuple<A, B> 
{

	private A first;
	
	private B second;
	
	public Tuple(A first, B second)	
	{
		//set properties
		this.first = first;
		this.second = second;
	}
	
	/**
	 * Returns the first element of the tuple
	 * @return The first element of the tuple
	 */
	public A getFirst()
	{
		return this.first;
	}
	/**
	 * Returns the second element of the tuple
	 * @return The second element of the tuple
	 */
	public B getSecond()
	{
		return this.second;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tuple) {
			//cast obj
			Tuple<?, ?> that = (Tuple<?, ?>) obj;
			//I always wanted to use a bit operator :)
            return this.first.equals( that.getFirst() ) & this.second.equals( that.getSecond() );
        }
		return false;
	}
	
	@Override
	public String toString()
	{
		return String.format("(First: %s - Second: %s)", this.first, this.second);
	}
	
	/**
	 * This function converts a tuple of lists into a list of tuples
	 * @param tuple
	 * @return
	 */
    public static <X,Y> List<Tuple<X, Y>> zip(Tuple<List<X>, List<Y>> tuple)
    {
    	//list which is returned
    	List<Tuple<X, Y>> list = null;

    	//get the lists from the tuple
    	List<X> listA = tuple.getFirst();
		List<Y> listB = tuple.getSecond();	
    	
		//first tuple in new list
    	list = new List<Tuple<X, Y>>( new Tuple<X, Y>(listA.head, listB.head) );
    	//loop over both lists
    	while(listA.tail != null && listB.tail != null)	{
    		
    		//move to next element
    		listA = listA.tail;
    		listB = listB.tail;	
    		//add to new list
    		if(listA != null && listB != null)	{
    			list = list.append( new Tuple<X, Y>(listA.head, listB.head) );
    		}	
    	}
    	
		return list;
    	
    }
    
    /**
     * This function converts a list of tuples into a tuple of lists
     * @param list
     * @return
     */
    public static <X,Y> Tuple<List<X>, List<Y>> unzip(List<Tuple<X, Y>> list)
    {
    	
    	//get the lists from the tuple
    	List<X> listA = new List<X>(list.head.getFirst());
		List<Y> listB = new List<Y>(list.head.getSecond());
    	
		//iterate over list 
    	while(list.tail != null)	{
    		//move to next element
    		list = list.tail;
    		//append tuple elements to lists
    		listA = listA.append( list.head.getFirst() );
    		listB = listB.append( list.head.getSecond() );
    	}
    	
    	//return the new tuple consisting of lists
		return new Tuple<List<X>, List<Y>>(listA, listB);
    	
    }
    
	
}
