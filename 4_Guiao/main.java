class Main{
	public static void main(String[] args){
		/*
			Barrier b = new Barrier(3);

			new Thread(() -> {
				try{
				Thread.sleep(1000);
				System.out.println("vou fazer await");
					b.await();
				System.out.println("await retornou");
				}catch(Exception e){}
				}).start();
		
			new Thread(() -> {
				try{
				Thread.sleep(3000);
				System.out.println("vou fazer await");
					b.await();
				System.out.println("await retornou");
				}catch(Exception e){}
				}).start();

			new Thread(() -> {
				try{
				Thread.sleep(2000);
				System.out.println("vou fazer await");
					b.await();
				System.out.println("await retornou");
				}catch(Exception e){}
				}).start();
		*/


		Barrierb2 bb = new Barrierb2(3);

		new Thread(() -> {
			try{
			//Thread.sleep(1000);
			System.out.println("1vou fazer await");
				bb.await();
			System.out.println("1await retornou");
			//Thread.sleep(1000);
			System.out.println("2vou fazer await");
				bb.await();
			System.out.println("2await retornou");
			}catch(Exception e){}
			}).start();
	
		new Thread(() -> {
			try{
			//Thread.sleep(3000);
			System.out.println("3vou fazer await");
				bb.await();
			System.out.println("3await retornou");
			//Thread.sleep(3000);
			System.out.println("4vou fazer await");
				bb.await();
			System.out.println("4await retornou");

			System.out.println("7vou fazer await");
				bb.await();
			System.out.println("7await retornou");
			}catch(Exception e){}
			}).start();

		new Thread(() -> {
			try{
			//Thread.sleep(2000);
			System.out.println("5vou fazer await");
				bb.await();
			System.out.println("5await retornou");
			//Thread.sleep(2000);
			System.out.println("6vou fazer await");
				bb.await();
			System.out.println("6await retornou");
			}catch(Exception e){}
			}).start();

	}

}