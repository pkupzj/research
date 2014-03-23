package com.zijian.research.socket.tcp.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {
	public static void main(String[] args) {
		try {
			ServerSocket s = new ServerSocket(8189);
			boolean exit = false;
			while (!exit) {
				Socket incoming = s.accept();
				try {
					InputStream inStream = incoming.getInputStream();
					OutputStream outStream = incoming.getOutputStream();

					Scanner in = new Scanner(inStream);
					PrintWriter out = new PrintWriter(outStream, true);

					out.println("Hello! Enter BYE to disconnect.");
					out.println("Hello! Enter EXIT to close server.");
					
					boolean done = false;

					while (!done && in.hasNextLine()) {
						String line = in.nextLine();
						out.println("Echo: " + line);
						if (line.trim().equals("BYE")) {
							done = true;
						}
						else if(line.trim().equals("EXIT")){
							done = true;
							exit = true;
						}
					}
				} finally {
					incoming.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
