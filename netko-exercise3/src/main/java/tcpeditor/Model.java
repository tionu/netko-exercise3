package tcpeditor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Model {
	
	public enum Modus {
		CLIENT, SERVER
	}
	
	public interface ModelListener {
		
		public void modelHasChanged();
		
	}
	
	private List<ModelListener> listeners;
	private Modus modus;
	private String url;
	private int port;
	private String textToSend;
	private String receivedText;
	private Socket socket; // Dieses Attribut benötigen Sie, wenn Sie den Modus SERVER programmieren, um einen Socket,
	// der in der Methode startServer() erstellt wurde auch in der Methode sendData() verwenden zu können
	
	public Model() {
		this.listeners = new LinkedList<>();
	}
	
	public Modus getModus() {
		return modus;
	}

	public void setModus(Modus modus) {
		this.modus = modus;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getTextToSend() {
		return textToSend;
	}

	public void setTextToSend(String textToSend) {
		this.textToSend = textToSend.replaceAll("\n", "\r\n");
	}

	public String getReceivedText() {
		return receivedText;
	}

	public void setReceivedText(String receivedText) {
		this.receivedText = receivedText;
	}

	public void addListener(ModelListener listener) {
		listeners.add(listener);
	}

	private void informListeners() {
		for(ModelListener listener : listeners)
			listener.modelHasChanged();
	}
	
	public void sendData() {
		try {
			if(modus == Modus.CLIENT)
				socket = new Socket(url, port);
			socket.getOutputStream().write(textToSend.getBytes());
			socket.getOutputStream().flush();
			if(modus == Modus.CLIENT)
				listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void listen() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		int b = in.read();
		if(b == -1)
			in.close();
		int length = in.available();
		byteStream.write(b);
		while(length > 0) {
			byte[] bytes = new byte[length];
			in.read(bytes, 0, length);
			byteStream.write(bytes);
			length = in.available();
		}
		receivedText = new String(byteStream.toByteArray());
		informListeners();
	}
	
	public void startServer() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();
			listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
