import java.util.concurrent.locks.ReentrantLock;

class Bank {
  private static class Account {

  private final ReentrantLock lock=new ReentrantLock();

    private int balance;
    Account(int balance) {
      this.balance = balance;
    }
    
    int balance() {
      lock.lock();
      try{
        return balance;
      }
      finally{
        lock.unlock();
      }

    }
    
    boolean deposit(int value) {
      lock.lock();
      try{
        balance += value;
      }
      finally{
        lock.unlock();
      }

      return true;
    }
    
    boolean withdraw(int value) {
      lock.lock();
      try{
        if (value > balance)
          return false;
        balance -= value;
        return true;
      }
      finally{
        lock.unlock();
      }
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
    if (id < 0 || id >= slots)
      return 0;
    return av[id].balance();
  }

  // Deposit
  boolean deposit(int id, int value) {
    if (id < 0 || id >= slots)
      return false;
    return av[id].deposit(value);
  }

  // Withdraw; fails if no such account or insufficient balance
  public boolean withdraw(int id, int value) {
    if (id < 0 || id >= slots)
      return false;
    return av[id].withdraw(value);
  }

  public boolean transfer(int from,int to, int value){
    
      if(from<0||from>=slots||to<0||to>=slots)
        return false;

      if(from<to){
        av[from].lock.lock();
        av[to].lock.lock();
      }
      else{
        av[to].lock.lock();
        av[from].lock.lock();        
      }

      try{
        try{
          if(!av[from].withdraw(value))
            return false;
        }finally{
          av[from].lock.unlock();
        }
        return av[to].deposit(value);
      }
      finally{
        av[to].lock.unlock();
      }
    
  }

  public int totalBalance(){
    int res=0;
    int i;
    for (i=0;i<slots;i++) {
      av[i].lock.lock();
    }
    for (i=0;i<slots;i++) {
      res+=balance(i);
    }
    for (i=0;i<slots;i++) {
      av[i].lock.unlock();
    }
    return res;
  }
}


class ex1{
  public static void main(String[] args){
    System.out.println("Bemvindo ao banco");
  }
}
