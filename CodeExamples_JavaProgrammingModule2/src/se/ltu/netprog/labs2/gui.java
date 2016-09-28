package se.ltu.netprog.labs2;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.omg.CORBA.portable.InputStream;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.awt.event.ActionEvent;

public class gui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldInput;
	private JTextArea textAreaOutput;
	/**
	 * Launch the application.
	 */
	static DataOutputStream outToServer;
	static DataInputStream inFromServer;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// a jframe here isn't strictly necessary, but it makes the
					// example a little more real
					JFrame frameUrl = new JFrame("InputDialog Server URL #1");
					JFrame frameSocket = new JFrame("InputDialog Server SOcket");
					// prompt the user to enter their name
					String serverUrl = JOptionPane.showInputDialog(frameUrl, "Enter server url");
					String socketurl = JOptionPane.showInputDialog(frameSocket, "Enter socket");
					int x =Integer.parseInt(socketurl);
					Socket clientSocket = new Socket(serverUrl, x);
					outToServer = new DataOutputStream(clientSocket.getOutputStream());
					inFromServer = new DataInputStream(clientSocket.getInputStream());
					gui frame = new gui(clientSocket);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public gui(Socket clientSocket) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textFieldInput = new JTextField();
		textFieldInput.setBounds(12, 22, 424, 19);
		contentPane.add(textFieldInput);
		textFieldInput.setColumns(10);

		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String command = textFieldInput.getText();
				try {
					// command+= "\n";
					//outToServer.print(command+ "\r\n");
					outToServer.writeUTF(command);
					outToServer.flush();
					String res;

					res = inFromServer.readUTF();
					textAreaOutput.setText(res);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		btnOk.setBounds(9, 234, 117, 25);
		contentPane.add(btnOk);

		textAreaOutput = new JTextArea();
		textAreaOutput.setBounds(12, 53, 424, 170);
		contentPane.add(textAreaOutput);
	}

	private String executeCommand(String command) {

		String s = "";
		ProcessBuilder b = new ProcessBuilder("/bin/sh", "-c", command);
		Map<String, String> environ = b.environment();

		Process process = null;
		try {
			process = b.start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		java.io.InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
		try {
			while ((line = br.readLine()) != null) {
				s += line + "\n";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Program terminated!");
		/*
		 * try {
		 * 
		 * String [] cmd= command.split("&&"); System.out.println(cmd[0]); for
		 * (String cmdLine: cmd) { Process p =
		 * Runtime.getRuntime().exec(cmdLine); BufferedReader stdInput = new
		 * BufferedReader(new InputStreamReader(p.getInputStream()));
		 * 
		 * BufferedReader stdError = new BufferedReader(new
		 * InputStreamReader(p.getErrorStream()));
		 * 
		 * // read the output from the command String temp = null;
		 * //System.out.println("Here is the standard output of the command:\n"
		 * ); while ((temp = stdInput.readLine()) != null) { s += temp + "\n"; }
		 * 
		 * // read any errors from the attempted command //System.out.
		 * println("Here is the standard error of the command (if any):\n");
		 * while ((temp = stdError.readLine()) != null) { s += temp + "\n"; } }
		 * 
		 * 
		 * } catch (IOException e) {
		 * System.out.println("exception happened - here's what I know: ");
		 * e.printStackTrace(); s = e.toString(); }
		 */
		return s;
	}
}