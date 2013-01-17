package sheet10;

import java.util.ArrayList;



public class Calendar {

	static class OverlappingTermException extends Exception	{
		
		Term a;
		
		Term b;
		
		public OverlappingTermException(Term a, Term b)	{
			this.a = a;
			this.b = b;
		}
		
		public String toString() {
			return this.getClass().getName() + " : Term "+ this.a.toString() + " overlapps with " + this.b.toString();
        }
		
	}
	
	static class NoTermFoundException extends Exception	{
		
		Term a;
		
		public NoTermFoundException(Term a)	{
			this.a = a;
		}
		
		public String toString() {
			return this.getClass().getName() + " : Term not found: "+ this.a.toString();
        }
		
	}
	
	private ArrayList<Term> terms;
	
	public Calendar() {
		
		this.terms = new ArrayList<Term>();
		
	}
	
	public void insertTerm(Term t) throws OverlappingTermException	{
		
		for (Term term : this.terms)	{
			if( 
					(t.getStart() <= term.getStart() && t.getEnd() <= term.getEnd()) || //case 1
					(t.getStart() <= term.getStart() && t.getEnd() >= term.getEnd()) || //case 4
					(t.getStart() >= term.getStart() && t.getEnd() <= term.getEnd()) || //case 2
					(t.getStart() <= term.getEnd()) //case 3
				)	
			{
				throw new OverlappingTermException(t, term);
			}
		}
		this.terms.add(t);
		
	}
	
	public Term next(Term t) throws NoTermFoundException	{
	
		boolean takeNext = false;
		for (Term term : this.terms)	{
			if(takeNext)	{
				return term;
			}
			if(t.equals(term))	{
				takeNext = true;
			}
		}
		if(!takeNext)	{
			throw new NoTermFoundException(t);
		}
		return null;
		
	}
	
	public String getTermsForDate(String date)	{
		return "";
	}
	
	public Term getTermForDate(String date, int i)	{
		return null;
	}
	
	@Override
	public String toString()	{
		StringBuilder s = new StringBuilder();
		for (Term term : this.terms)	{
			s.append( String.format("%s\n",term.toString()) );
		}
		return s.toString();
	}

}
