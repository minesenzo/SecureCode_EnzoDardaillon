package fr.minesales.dardaillon;

import fr.minesales.dardaillon.graphics.ClientPanel;

import java.io.*;
import java.net.Socket;

public class Client {
    private final String address;
    private final int port;
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private ClientPanel panel;

    private boolean sending;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void init(ClientPanel panel) throws IOException {
        if(this.panel == null) this.panel = panel;

        socket = new Socket(address, port);
        panel.log("Connexion ok");

        // sends output to the socket
        output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    public void send(String msg) throws IOException {
        panel.log("> " + msg);
        output.writeUTF(msg);
        output.flush();
        sending = true;

        receive();
    }

    private void receive() throws IOException {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String result = input.readUTF();
        panel.log(result);
        sending = false;
    }

    public void close() throws IOException {
        output.close();
        input.close();
        socket.close();
    }
}
