package sheet10;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Term {

	private int start;
	
	private int end;
	
	private String description;
	
	private String location;
	
	static class InvalidDateFormatException extends Exception	{

		public String dateString;

        public InvalidDateFormatException(String dateString) {
            this.dateString = dateString;
        }

        public String toString() {
            return this.getClass().getName() + " : " + this.dateString;
        }
		
	}
	
	static class InvalidDurationException extends Exception	{

		public int duration;

        public InvalidDurationException(int duration) {
            this.duration = duration;
        }

        public String toString() {
            return this.getClass().getName() + " : " + this.duration;
        }
		
	}
	
	static class EndBeforeStartDateException extends Exception	{

		public String start;
		
		public String end;

        public EndBeforeStartDateException(String start, String end) {
        	this.start = start;
        	this.end = end;
        }

        public String toString() {
            return this.getClass().getName() + " : startdate (" + this.end + ") is before enddate(" + this.start;
        }
	}
	
	public Term(int start, int duration, String description, String location) throws InvalidDurationException	{
		
		//make start absolute
		this.start = Math.abs(start);
		//check if duration is valid
		if( duration < 1 )	{
			throw new InvalidDurationException(duration);
		}
		//set end date
		this.end = this.start + duration;
		
		//set description and location
		this.description = description;
		this.location = location;
		
	}
	
	public Term(String start, int duration, String description, String location) throws InvalidDurationException, InvalidDateFormatException	{
		this(Term.convertDateStringToTimestamp(start), duration, description, location);
	}

	public Term(String start, String end, String description, String location) throws InvalidDateFormatException, EndBeforeStartDateException	{
		//get timestamps
		int tsStart = Term.convertDateStringToTimestamp(start);
		int tsEnd = Term.convertDateStringToTimestamp(end);
		
		if(tsEnd < tsStart)	{
			throw new EndBeforeStartDateException(start,end);
		}
		//set description and location
		this.description = description;
		this.location = location;
	}
	
	
	private static int convertDateStringToTimestamp(String date) throws InvalidDateFormatException	{
		
		try	{
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			Date d = df.parse(date);
			return (int)(d.getTime()/1000);
		} catch(ParseException e)	{
			throw new InvalidDateFormatException(date);
		}
		
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
}
