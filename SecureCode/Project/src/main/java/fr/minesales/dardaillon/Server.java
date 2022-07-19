package fr.minesales.dardaillon;

import fr.minesales.dardaillon.features.ServerConnection;
import fr.minesales.dardaillon.graphics.ServerPanel;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Server {

    private final int port;
    private Socket socket;
    private DataInputStream in;
    private ServerPanel panel;

    private List<ServerConnection> connections;

    public Server(int port) {
        this.port = port;
    }

    public void init(ServerPanel panel) throws IOException {
        this.panel = panel;
        connections = new ArrayList<>();

        ServerSocket server = new ServerSocket(port);

        panel.log("Waiting for a client...");

        new Thread(() -> {
            do{
                try {
                    socket = server.accept();

                    panel.log("Client connected : " + socket.getInetAddress());

                    ServerConnection connection = new ServerConnection(this, socket);
                    connection.start();
                    connections.add(connection);

                    List<ServerConnection> toDelete = new ArrayList<>();
                    for(ServerConnection c : connections) {
                        if(!c.isRunning()) toDelete.add(c);
                    }
                    for(ServerConnection c : toDelete) {
                        connections.remove(c);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } while (connections.size() == 0);
        }).start();

        /*in = new DataInputStream(
                new BufferedInputStream(socket.getInputStream()));

        String result = "";
        while(!result.equals("stop")) {
            try {
                result = in.readUTF();
                if(result.contains("date")){
                    panel.log(Calendar.getInstance().getTime().toString());
                } else
                    panel.log(result);

            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        panel.log("Server shutdown...");

        socket.close();
        in.close();*/
    }

    public ServerPanel getPanel() {
        return panel;
    }
}
