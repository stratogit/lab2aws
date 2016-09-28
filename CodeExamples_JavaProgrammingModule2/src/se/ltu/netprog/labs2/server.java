package se.ltu.netprog.labs2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class server extends Thread {
	private Socket s;
	static final int BUFSIZE = 1024;

	public server(Socket s) {
		this.s = s;
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("usage: java ThreadedServer port");
			System.exit(0);
		}
		try {
			int port = Integer.parseInt(args[0]);
			ServerSocket listener = new ServerSocket(port);
			for (;;) {
				Socket s = listener.accept();
				Thread server = new server(s);
				server.start();
			}
		} catch (IOException e) {
			return;
		}
	}

	public void run() {
		try {
			handleClient(s);
		} catch (IOException e) {
			return;
		}
	}

	static void handleClient(Socket s) throws IOException {

		// print out client's address
		System.out.println(
				"Connection from " + s.getInetAddress().getHostName() + " at :" + s.getInetAddress().getHostAddress());
		// Set up streams

		DataOutputStream out = new DataOutputStream(s.getOutputStream());
		DataInputStream in = new DataInputStream(s.getInputStream());

		String input;
		while ((input = in.readUTF()) != "exit") {
			System.out.println("Command : " + input);
			// input= input.substring(1, input.length());

			String outputString = CMDExecuter.runCommand(input);
			out.writeUTF(outputString);
		}

		// read/write loop

		// Modify your code here so that it sends back your name in addition to
		// the echoed symbols

		System.out.println("Client has left\n");

		s.close();
	}
}