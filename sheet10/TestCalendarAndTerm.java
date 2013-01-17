package sheet10;

import sheet10.Calendar.OverlappingTermException;
import sheet10.Term.InvalidDateFormatException;
import sheet10.Term.InvalidDurationException;

public class TestCalendarAndTerm {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		try {
			Term t = new Term("24.01.2013 14:45", 300, "blubb" , "TUM");
			Term t1 = new Term("24.01.2013 14:46", 600, "blubb" , "TUM");
			
			Calendar c = new Calendar();
			
			
			
			c.insertTerm(t);
			c.insertTerm(t1);
			
			System.out.println(c);
			
			
		} catch (InvalidDurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDateFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OverlappingTermException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
