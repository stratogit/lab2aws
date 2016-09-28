package se.ltu.netprog.labs2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class CMDExecuter {
	public static String runCommand(String command) {
		Process process;

		ProcessBuilder b = new ProcessBuilder("/bin/sh", "-c", command);

		 process = null;
		try {
			process = b.start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String s = "";
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
		return s;
	}
}

