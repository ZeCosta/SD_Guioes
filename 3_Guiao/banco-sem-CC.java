import java.util.*;

class Bank {

    private static class Account {
        private int balance;
  		ReentrantLock lock=new ReentrantLock();
        Account(int balance) { 
        	this.balance = balance; 
        }
        int balance() { 
        	return balance; 
        }
        boolean deposit(int value) {
            balance += value;
            return true;
        }
        boolean withdraw(int value) {
            if (value > balance)
                return false;
            balance -= value;
            return true;
        }
    }

    private Map<Integer, Account> map = new HashMap<Integer, Account>();
    private int nextId = 0;
  	ReentrantLock lock=new ReentrantLock();

    // create account and return account id
    public int createAccount(int balance) {
	    Account c = new Account(balance);
    	lock.lock();
    	try{
	        int id = nextId;
	        nextId += 1;
	        map.put(id, c);
	        return id;
    	}
    	finally{
    		lock.unlock();
    	}
    }

    // close account and return balance, or 0 if no such account
    public int closeAccount(int id) {
    	Account c;
    	lock.lock();
    	try{
        	c = map.remove(id);
        	if (c == null)
            	return 0;
            c.lock.lock();
    	}finally{
    		lock.unlock();
    	}

        try{
    		return c.balance();
        }finally{
        	c.lock.unlock();
        }
    }

    // account balance; 0 if no such account
    public int balance(int id) {
        Account c;
    	lock.lock();
    	try{
        	c = map.get(id);
	        if (c == null)
	            return 0;
	        c.lock.lock();
    	}finally{
			lock.unlock();
    	}

        try{
        	return c.balance();
        }finally{
        	c.lock.unlock();
        }
    }

    // deposit; fails if no such account
    public boolean deposit(int id, int value) {
        Account c;
        lock.lock();
        try{
        	c = map.get(id);
	        if (c == null)
	            return false;
	        c.lock.lock();
        }finally{
        	lock.unlock();
        }

        try{
        	return c.deposit(value);
        }
        finally{
        	c.lock.unlock();
        }
    }

    // withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        Account c;
        try{
        	c = map.get(id);
	        if (c == null)
	            return false;
	    	c.lock.lock();
        }finally{
        	lock.unlock();
        }

        try{
        	return c.withdraw(value);
        }
        finally{
        	c.lock.unlock();
        }
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    public boolean transfer(int from, int to, int value) {
        Account cfrom, cto;
        lock.lock();

        try{
	        cfrom = map.get(from);
	        cto = map.get(to);
	        if (cfrom == null || cto ==  null)
	            return false;

	        if(from<to){
	        	cfrom.lock.lock();
	        	cto.lock.lock();
	      	}
	      	else{
	        	cto.lock.lock();
	        	cfrom.lock.lock();        
	      	}
        }finally{
        	lock.lock();
        }


        try{
	        try{
	          if(!cfrom.withdraw(value))
	            return false;
	        }finally{
	          cfrom.lock.unlock();
	        }
	        return cto.deposit(value);
	    }finally{
	    	cto.lock.unlock();
	    }

    }

    // sum of balances in set of accounts; 0 if some does not exist
    public int totalBalance(int[] ids) {
        int total = 0;
        Account[] c = new Account[ids.length];

        lock.lock();
        try{
	        for (int i=0;i<ids.length; i++) {
	            c[i] = map.get(i);
	            if (c[i] == null){
	                return 0;
	            }

	        }

	        for (Account ci : c) {
	        	ci.lock.lock();
	        }
        }finally{
        	lock.unlock();
        }

        for(Account ci : c){
        	total += ci.balance();
        	ci.lock.unlock();
        }
	        
	    return total;
        
  }

}
