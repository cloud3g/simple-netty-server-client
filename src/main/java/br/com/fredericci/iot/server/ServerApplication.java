package br.com.fredericci.iot.server;

public class ServerApplication {

	public static void main(String[] args) throws Exception {
		Server server = new Server(7788);
		server.startAndWait();
	}
}
