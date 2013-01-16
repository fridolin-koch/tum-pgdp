package sheet9;

/**
 * @author Frido Koch
 */
public class Student implements Comparable<Student>
{
    
    private final String name;
    
    private final String enrolmentNumber;

    public Student(String name, String enrolmentNumber)
    {
        this.enrolmentNumber = enrolmentNumber;
        this.name = name;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public String getEnrolmentNumber()
    {
        return this.enrolmentNumber;
    }
    
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Student) {
			//cast obj
			Student that = (Student) obj;
	        return this.name.equals( that.getName() ) & this.enrolmentNumber.equals( that.getEnrolmentNumber() );
	    }
		return false;
	} 
    
    @Override
    public int compareTo(Student t) 
    {
        int cmp = this.name.compareTo(t.getName());
        if( cmp == 0 )    {
            
            //names are equal so we compare the enrolment Number
            return this.enrolmentNumber.compareTo(t.getEnrolmentNumber());
            
        } else  {
            return cmp;
        }
    }
    
    /**
     * Sort some array whose elements implement the Comparable interface. 
     * 
     * @param someArray
     */
    public static <E extends Comparable<E>> void sort(E[] someArray)
    {
    	quicksort(someArray, 0, someArray.length-1);
    }
    
    /**
     * Simple generic quicksort implementation for elements implementing the Comparable interface
     * 
     * @param list The list to sort
     * @param leftLimit lower limit
     * @param rightLimit upper limit
     */
    private static <E extends Comparable<E>> void quicksort(E[] list, int leftLimit, int rightLimit)
    {
    	//only sort if the list has at least 2 elements
    	if(list.length > 1)	{
    		//we can only sort if the list elements support the Comparable interface
    		if( list[0] instanceof Comparable )	{
    			
    			if( leftLimit < rightLimit)	{
    				
        			int left = leftLimit;
        			int right = rightLimit;
        			//get pivot element
        			E pivot = list[(left+right)/2];
        			
        			while(left < right)	{
        				
        				//search for an element which is bigger than the pivot element
        				while( list[left].compareTo(pivot) < 0 && left < rightLimit)	{
        					left++;
        				}
        				
        				//search for an element which is smaller than the pivot element
        				while( list[right].compareTo(pivot) > 0 && right > leftLimit)	{
        					right--;
        				}
        				
        				//swap elements
        				if(left < right)	{
        					E tmp = list[left];
        					list[left] = list[right];
        					list[right] = tmp;
        				}
        			}
        			
        			//swap pivot with final position
        			if( list[left].compareTo(pivot) > 0 )	{
        				E tmp = list[left];
        				list[left] = list[(left+right)/2];
        				list[(left+right)/2] = tmp;
        			}
        			
        			//go on
        			quicksort(list,leftLimit, left-1);
        			quicksort(list,left+1, rightLimit);
    				
    			}
    		}
    	}
    }
    
    @Override
    public String toString()
    {
        return String.format("%s - %s", this.name,this.enrolmentNumber);
    }
}
