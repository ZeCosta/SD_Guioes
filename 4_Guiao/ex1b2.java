//usando o conceito de fase (epoch)!!!

import java.util.concurrent.locks.*;

class Barrierb2 {
	private final int N;
	int counter=0;
	int epoch=0;
  	
  	private Lock l=new ReentrantLock();
  	private Condition cond= l.newCondition();

	
	Barrierb2 (int N) {
		this.N=N;
	}

	void await() throws InterruptedException {
		l.lock();
		try{
			int epoch=this.epoch;
			counter+=1;
			if(counter<N){
				while(epoch==this.epoch)
					cond.await();
			}
			else{
				this.epoch+=1;
				counter=0;
				cond.signalAll();
			}
			
		}finally{
			l.unlock();
		}
	}
}


