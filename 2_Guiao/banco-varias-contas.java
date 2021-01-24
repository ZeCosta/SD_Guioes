import java.util.concurrent.locks.ReentrantLock;

class Bank {
  private static final ReentrantLock lock=new ReentrantLock();

  private static class Account {
    private int balance;
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

  // Bank slots and vector of accounts
  private int slots;
  private Account[] av; 

  public Bank(int n)
  {
    slots=n;
    av=new Account[slots];
    for (int i=0; i<slots; i++) av[i]=new Account(0);
  }

  // Account balance
  public int balance(int id) {
    lock.lock();
      try{
    if (id < 0 || id >= slots)
      return 0;
    return av[id].balance();
    }
      finally{
        lock.unlock();
      }
  }

  // Deposit
  boolean deposit(int id, int value) {
    lock.lock();
      try{
    if (id < 0 || id >= slots)
      return false;
    return av[id].deposit(value);
  }
      finally{
        lock.unlock();
      }
  }

  // Withdraw; fails if no such account or insufficient balance
  public boolean withdraw(int id, int value) {
    lock.lock();
      try{
    if (id < 0 || id >= slots)
      return false;
    return av[id].withdraw(value);
    }
      finally{
        lock.unlock();
      }
  }

  public boolean transfer(int from,int to, int value){
    lock.lock();
    try{
      if(!withdraw(from,value))
        return false;
      return deposit(to,value);
    }
    finally{
      lock.unlock();
    }
  }

  public int totalBalance(){
    int res=0;
    int i;
    for (i=0;i<slots;i++) {
      res+=balance(i);
    }
    return res;
  }
}