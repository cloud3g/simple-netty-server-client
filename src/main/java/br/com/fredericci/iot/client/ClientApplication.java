package br.com.fredericci.iot.client;

import br.com.fredericci.iot.common.Message;

public class ClientApplication {

	public static void main(String[] args) throws Exception {
		Client client  = new Client();
		client.connect("localhost", 1988);
		
//		client.send(new Message("Hi"));
////		client.send(new Message("This is a simple socket implementation!"));
////		client.send(new Message("This is a simple socket implementation!"));
////		client.send(new Message("This is a simple socket implementation!"));
////		client.send(new Message("This is a simple socket implementation!"));
////		client.send(new Message("This is a simple socket implementation!"));
////		client.send(new Message("This is a simple socket implementation!"));
////		client.send(new Message("This is a simple socket implementation!"));

		while(true) {
			client.send("hello"); // 00 00 00 09 05 74 00 05 68 65 6C 6C 6F // 68 65 6C 6C 6F // hello
			Thread.sleep(1000);
		}
		//client.send(new Message("Bye")); //shutown the server
		//client.close();
	}
	
}
