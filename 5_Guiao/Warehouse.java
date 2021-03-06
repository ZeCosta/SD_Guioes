import java.util.*;
import java.util.concurrent.locks.*;

class Warehouse {
	private Map<String, Product> m =  new HashMap<String, Product>();

  	Lock l=new ReentrantLock();


	private class Product {
		int q = 0;
  		
  		Condition cond= l.newCondition();
	
		//Product(Condition cond){this.cond=cond;} //versao static class Product
	}

	private Product get(String s) {
		Product p = m.get(s);
		if (p != null) return p;
	
		//p = new Product(l.newCondition()); //versao static class Product
		m.put(s, p);
		return p;
	}

	public void supply(String s, int q) {
		l.lock();
		try{
			Product p = m.get(s);
			p.q += q;
			p.cond.signalAll();
		}
		finally{
			l.unlock();
		}
	}

	// Errado se faltar algum produto...
	public void consume(String[] a) throws InterruptedException {
		l.lock();
		try{
			Product[] ap = new Product[a.length];
			for(i=0;i<a.length;i++){
				ap[i] = get(a[i]);
			}

			Product p;								//while(true){
			while((p=missing(ap)!=null){			//	Product p = missing(ap);
				p.cond.await();				    	//	if(p==null)break;
			}										//	p.cond.await();}
		
			for (String s : a)get(s).q--;

		finally{
			l.unlock();
		}	
	}

	public Produto missing(Product[] a){
		for (Product p : a)
			if(p.q==0)return p;
		return null;
	}

}
