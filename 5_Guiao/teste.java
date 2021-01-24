//Falta colocar variaveis de sincronização

class Agreement{

	static class Epoch{
		int max=0;
		int c=0
	}	

	final int N;

	Epoch e= new Epoch;

	Agreement(int N){
		this.N=N;
	}

	int propose(int v){
		Epoch e = this.e;
		e.max=Math.max(e.max,v);
		e.c++;

		if(e.c<N){
			while(e==this.e)
				cond.await();
		}
		else{
			this.e=new Epoch();
			cond.signalAll();
		}
	}
}