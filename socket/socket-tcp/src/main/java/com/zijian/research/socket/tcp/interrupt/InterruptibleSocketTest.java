package com.zijian.research.socket.tcp.interrupt;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class InterruptibleSocketTest {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new InterruptibleSocketFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

}

class InterruptibleSocketFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;

	private Scanner in;
	private JButton interruptibleButton;
	private JButton blockingButton;
	private JButton cancelButton;
	private JTextArea messages;
	private TestServer server;
	private Thread connectThread;

	public InterruptibleSocketFrame() {
		setSize(WIDTH, HEIGHT);
		setTitle("InterruptibleSocketTest");

		JPanel northPanel = new JPanel();
		add(northPanel, BorderLayout.NORTH);

		messages = new JTextArea();
		add(new JScrollPane(messages));

		interruptibleButton = new JButton("Interruptible");
		blockingButton = new JButton("Blocking");

		northPanel.add(interruptibleButton);
		northPanel.add(blockingButton);

		interruptibleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				interruptibleButton.setEnabled(false);
				blockingButton.setEnabled(false);
				cancelButton.setEnabled(true);
				connectThread = new Thread(new Runnable() {
					public void run() {
						messages.setText("");
						try {
							connectInterruptibly();
						} catch (IOException e) {
							messages.append("\nInterruptibleSocketTest.connectInterruptibly: "
									+ e);
						}
					}
				});
				connectThread.start();
			}
		});

		blockingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				interruptibleButton.setEnabled(false);
				blockingButton.setEnabled(false);
				cancelButton.setEnabled(true);
				connectThread = new Thread(new Runnable() {
					public void run() {
						messages.setText("");
						try {
							connectBlocking();
						} catch (IOException e) {
							messages.append("\nInterruptibleSocketTest.connectBlocking: "
									+ e);
						}
					}
				});
				connectThread.start();
			}
		});

		cancelButton = new JButton("Cancel");
		cancelButton.setEnabled(false);
		northPanel.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				connectThread.interrupt();
				cancelButton.setEnabled(false);
			}
		});
		server = new TestServer();
		new Thread(server).start();
	}

	/**
	 * Connects to the test server, using interruptible I/O
	 */
	public void connectInterruptibly() throws IOException {
		messages.append("Interruptible:\n");
		SocketChannel channel = SocketChannel.open(new InetSocketAddress(
				"localhost", 8189));
		try {
			in = new Scanner(channel);
			while (!Thread.currentThread().isInterrupted()) {
				messages.append("Reading ");
				if (in.hasNextLine()) {
					String line = in.nextLine();
					messages.append(line);
					messages.append("\n");
				}
			}
		} finally {
			channel.close();
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					messages.append("\nChannel closed\n");
					interruptibleButton.setEnabled(true);
					blockingButton.setEnabled(true);
				}
			});
		}
	}

	/**
	 * Connects to the test server, using blocking I/O
	 */
	public void connectBlocking() throws IOException {
		messages.append("Blocking:\n");
		Socket sock = new Socket("localhost", 8189);
		try {
			in = new Scanner(sock.getInputStream());
			while (!Thread.currentThread().isInterrupted()) {
				messages.append("Reading ");
				if (in.hasNextLine()) {
					String line = in.nextLine();
					messages.append(line);
					messages.append("\n");
				}
			}
		} finally {
			sock.close();
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					messages.append("\nSocket closed\n");
					interruptibleButton.setEnabled(true);
					blockingButton.setEnabled(true);
				}
			});
		}
	}

	/**
	 * A multithreaded server that listens to port 8189 and sends numbers to the
	 * client, simulating a hanging server after 10 numbers.
	 */
	class TestServer implements Runnable {
		public void run() {
			try {
				ServerSocket s = new ServerSocket(8189);

				while (true) {
					Socket incoming = s.accept();
					Runnable r = new TestServerHandler(incoming);
					Thread t = new Thread(r);
					t.start();
				}
			} catch (IOException e) {
				messages.append("\nTestServer.run: " + e);
			}
		}
	}

	/**
	 * This class handles the client input for one server socket connection.
	 */
	class TestServerHandler implements Runnable {

		private Socket incoming;
		private int counter;

		/**
		 * Constructs a handler.
		 * 
		 * @param i
		 *            the incoming socket
		 */
		public TestServerHandler(Socket i) {
			incoming = i;
		}

		public void run() {
			try {
				OutputStream outStream = incoming.getOutputStream();
				PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
				while (counter < 100) {
					counter++;
					if (counter <= 10)
						out.println(counter);
					Thread.sleep(100);
				}
				incoming.close();
				messages.append("\nClosing server\n");
			} catch (Exception e) {
				messages.append("\nTestServerHandler.run: " + e);
			}
		}

	}
}
