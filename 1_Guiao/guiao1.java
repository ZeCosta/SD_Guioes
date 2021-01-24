import java.lang.Thread;

class Incrementer implements Runnable {
	public void run() {
		final long I=100;

		for (long i = 0; i < I; i++)
			System.out.println(i);
	}

	public static void main(String[] args){
		final int numThreads = 10;
		Incrementer worker = new Incrementer();


		Thread[] t = new Thread[numThreads];
		int i;
		for (i=0; i<numThreads; i++) {
			t[i]=(new Thread(worker));
			t[i].start();
		}

		try{
			for (i=0; i<numThreads; i++) {
				t[i].join();
			}
		}catch(InterruptedException e){
			System.out.println("fuck");
		}

		System.out.println("The end");
	}
}
