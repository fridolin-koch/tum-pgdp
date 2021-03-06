/*
 * Author: Frido Koch
 * Email: frido@vresh.net
 * License: CC BY-NC-SA 3.0 DE
 * http://creativecommons.org/licenses/by-nc-sa/3.0/de/
 */
package sheet11;

/**
 * @author Frido Koch
 */
public class Erathostenes extends Thread {

	final private boolean[] numbers;

	final private int factor;

	public Erathostenes(boolean[] numbers,int factor)	{
		if(factor < 2)	{
			throw new IllegalArgumentException("The factor argument has to be larger than 2.");
		}
		this.numbers = numbers;
		this.factor = factor;
	}

	public void run() {
	    System.out.println("Factor: " + this.factor);
		int maxNumber = this.numbers.length;
		int i = this.factor;
		while( i <= maxNumber )	{
			i = i + this.factor;
			if(i > 1 && i <= maxNumber)	{
				this.numbers[i-1] = true;
			}
		}
    }

	public static void main(String args[]) {

		//init numbers array
		boolean[] nbrs = new boolean[5000];
		//set 1(index:0) to true
		nbrs[0] = true;
		//init thread array and factor
		Erathostenes[] threads = new Erathostenes[2500];
		int f = 2;
		for(int i = 0; i < threads.length; i++)	{
			threads[i] = new Erathostenes(nbrs, f);
			//increase factor
			f++;
		}
		//start each thread
		for(int i = 0; i < threads.length; i++)	{
		    threads[i].start();
		}
		//and then join each thread
		for(int i = 0; i < threads.length; i++)   {
            try {
              threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
		//output prime numbers
		for(int i = 0; i < nbrs.length; i++)	{
			if(!nbrs[i])	{
				System.out.println(i+1);
			}
		}

	}
}
