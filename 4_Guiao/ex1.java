import java.util.concurrent.locks.*;

class Barrier {
	private final int N;
	int counter=0;
  	
  	private Lock l=new ReentrantLock();
  	private Condition cond= l.newCondition();

	
	Barrier (int N) {
		this.N=N;
	}

	void await() throws InterruptedException {
		l.lock();
		try{
			counter+=1;
			if(counter<this.N)
				while(counter<this.N)
					cond.await();
			else
				cond.signalAll();

		}finally{
			l.unlock();
		}
	}
}


