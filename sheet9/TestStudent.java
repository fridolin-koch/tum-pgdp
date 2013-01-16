package sheet9;

import java.util.Arrays;

/**
 * @author Frido Koch
 */
public class TestStudent 
{
    public static void main(String[] args)  
    {
    	
    	//the students input
        Student[] students = new Student[10];
        
        students[0] = new Student("Adam", "1003");
        students[1] = new Student("Werner", "1020");
        students[2] = new Student("Xaver", "1059");
        students[3] = new Student("GŸnther", "2031");
        students[4] = new Student("Ursula", "4510");
        students[5] = new Student("Frido", "1103");
        students[6] = new Student("Ursula", "1006");
        students[7] = new Student("Robert", "1035");
        students[8] = new Student("Laura", "1509");
        students[9] = new Student("Torsten", "1310");
        
        //define expected result
        Student[] studentsSorted = new Student[10];
        studentsSorted[0] = new Student("Adam", "1003");
        studentsSorted[1] = new Student("Frido", "1103");
        studentsSorted[2] = new Student("GŸnther", "2031");
        studentsSorted[3] = new Student("Laura", "1509");
        studentsSorted[4] = new Student("Robert", "1035");
       	studentsSorted[5] = new Student("Torsten", "1310");
       	studentsSorted[6] = new Student("Ursula", "1006");
       	studentsSorted[7] = new Student("Ursula", "4510");
       	studentsSorted[8] = new Student("Werner", "1020");
       	studentsSorted[9] = new Student("Xaver", "1059");
       
       	//sort students
       	Student.sort(students);
       	
       	if( students.length == studentsSorted.length )	{
       		
       		boolean testResult = true;
       		for(int i = 0; i < students.length; i++)	{
       			//check if the Student object are equal
       			if( !students[i].equals( studentsSorted[i]) )	{
       				testResult = false;
       			}
       		}
       		
       		if(testResult)	{
       			System.out.println("Test 1 passed, quicksort works!");
       		} else	{
           		System.out.println("Test 1 failed!");
           	}
       		
       	} else	{
       		System.out.println("Test 1 failed!");
       	}
       	
       	//just another test
        
        String[] letters 			= {"A","R","F","C","E","V","B","S","J","L","W","K","M","Q","T","P","H","X","I","G","D","Y","N","U","Z","O"};
        String[] lettersExpected	= {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        
        //sort letters
        Student.sort(letters);

        if( Arrays.equals(letters,lettersExpected) )	{
        	System.out.println("Test 2 passed, quicksort works!");
        } else	{
       		System.out.println("Test 2 failed!");
       	}

    }
    
}
