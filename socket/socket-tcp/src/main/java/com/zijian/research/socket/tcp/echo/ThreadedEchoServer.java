package com.zijian.research.socket.tcp.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ThreadedEchoServer {

	public static void main(String[] args) {
		try {
			int i = 1;
			ServerSocket s = new ServerSocket(8189);
			while (true) {
				Socket incoming = s.accept();
				System.out.println("Spawning " + i);
				Runnable r = new ThreadedEchoHandler(incoming);
				Thread t = new Thread(r);
				t.start();
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}
}

class ThreadedEchoHandler implements Runnable {
	private final Socket incoming;

	public ThreadedEchoHandler(Socket i) {
		incoming = i;
	}

	public void run() {
		try {
			try {
				InputStream inStream = incoming.getInputStream();
				OutputStream outStream = incoming.getOutputStream();

				Scanner in = new Scanner(inStream);
				PrintWriter out = new PrintWriter(outStream, true);

				out.println("Hello! Enter BYE to disconnect.");

				boolean done = false;

				while (!done && in.hasNextLine()) {
					String line = in.nextLine();
					out.println("Echo: " + line);
					if (line.trim().equals("BYE")) {
						done = true;
					} else if (line.trim().equals("EXIT")) {
						done = true;
					}
				}
			} finally {
				incoming.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
