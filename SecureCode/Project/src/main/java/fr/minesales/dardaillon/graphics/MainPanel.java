package fr.minesales.dardaillon.graphics;

import fr.minesales.dardaillon.Client;
import fr.minesales.dardaillon.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class MainPanel extends JPanel {
    private final Frame frame;
    private final int port = 5001;

    private Box b;

    public MainPanel(Frame frame) {
        this.frame = frame;

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Choose a role :");

        JButton clientButton = new JButton("Client");
        clientButton.addActionListener(a -> initClient());

        JButton serverButton = new JButton("Server");
        serverButton.addActionListener(a -> initServer());

        b = Box.createHorizontalBox();

        b.add(title);
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setMaximumSize( new Dimension(16, Integer.MAX_VALUE) );
        JSeparator separator2 = new JSeparator(SwingConstants.VERTICAL);
        separator2.setMaximumSize( new Dimension(16, Integer.MAX_VALUE) );
        b.add(separator);
        b.add(clientButton);
        b.add(separator2);
        b.add(serverButton);

        add(b, BorderLayout.NORTH);
    }

    private void initClient(){
        System.out.println("Init client...");

        frame.setTitle("SecureCode - Client");

        Client client = new Client("0.0.0.0", port);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    client.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        try {
            ClientPanel panel = new ClientPanel(client);
            add(panel, BorderLayout.CENTER);
            client.init(panel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            finishInit();
        }
    }

    private void initServer(){
        System.out.println("Init server...");

        frame.setTitle("SecureCode - Server");

        Server server = new Server(port);
        try {
            ServerPanel panel = new ServerPanel();
            add(panel, BorderLayout.CENTER);
            server.init(panel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            finishInit();
        }
    }

    private void finishInit(){
        b.setVisible(false);
    }
}
