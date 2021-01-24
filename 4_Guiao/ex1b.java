import java.util.concurrent.locks.*;

class Barrierb {
	private final int N;
	int counter=0;
	boolean open=false;
  	
  	private Lock l=new ReentrantLock();
  	private Condition cond= l.newCondition();

	
	Barrierb (int N) {
		this.N=N;
	}

	void await() throws InterruptedException {
		l.lock();
		try{
			while(open)
				cond.await();


			counter+=1;
			if(counter<N){
				open=false;
				while(!open)
					cond.await();
			}
			else{
				open=true;
				cond.signalAll();
			}
			
			counter-=1;
			if(counter==0){
				open=false; //a ultima fecha a porta
				cond.signalAll();
			}

		}finally{
			l.unlock();
		}
	}
}


