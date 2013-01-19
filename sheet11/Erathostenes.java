package sheet11;

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
		
		boolean[] nbrs = new boolean[5000];
		nbrs[0] = true;
		
		Erathostenes[] threads = new Erathostenes[2500];
		int f = 2;
		for(int i = 0; i < threads.length; i++)	{
			threads[i] = new Erathostenes(nbrs, f);
			//increase factor
			f++;
		}
		
		for(int i = 0; i < threads.length; i++)	{
			threads[i].start();
		}
		
		for(int i = 0; i < threads.length; i++)	{
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for(int i = 0; i < nbrs.length; i++)	{
			if(!nbrs[i])	{
				System.out.println(i+1);
			}
		}
		
	}
}