package fr.minesales.dardaillon.features;

import fr.minesales.dardaillon.Server;
import fr.minesales.dardaillon.formatter.DateLabelFormatter;

import java.io.*;
import java.net.Socket;
import java.util.Calendar;

public class ServerConnection extends Thread implements Runnable {
    private final Server server;
    private final Socket socket;
    private final DataOutputStream output;
    private final DataInputStream input;
    private boolean isRunning;

    public ServerConnection(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;

        output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    @Override
    public void run() {
        isRunning = true;
        String result = "";
        while(!socket.isClosed() && !result.equals("stop")) {
            try {
                result = input.readUTF();
                if(result.startsWith("date")) {
                    result = result.substring(5);

                    try {
                        DateLabelFormatter formatter = new DateLabelFormatter();
                        formatter.setDatePattern(result);
                        result = formatter.valueToString(Calendar.getInstance());
                    } catch (IllegalArgumentException e) {
                        result = "Wrong date format !";
                    }
                }
                server.getPanel().log(result);
                output.writeUTF(result);
                output.flush();
            } catch(IOException e) {
                try {
                    server.getPanel().log("Client disconnected : " + socket.getInetAddress());
                    socket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        try {
            isRunning = false;
            close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void close() throws IOException {
        output.close();
        input.close();
        socket.close();
    }

    public boolean isRunning() {
        return isRunning;
    }
}
