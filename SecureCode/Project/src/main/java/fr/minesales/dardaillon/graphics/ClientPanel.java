package fr.minesales.dardaillon.graphics;

import fr.minesales.dardaillon.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Objects;

public class ClientPanel extends JPanel {
    private final Client client;
    private final JTextArea log, msg;
    private final JButton sendButton;
    private final JComboBox<String> dateFormatPicker;

    private boolean isPickingDate = false;

    public ClientPanel(Client client) {
        this.client = client;

        setLayout(new BorderLayout());

        log = new JTextArea();
        log.setEditable(false);
        add(log, BorderLayout.CENTER);

        msg = new JTextArea();
        msg.setEditable(true);
        msg.setBackground(Color.getColor("EEEEEE"));
        msg.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    if(msg.getText().length() > 0) sendCommand();
                    e.consume();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        sendButton = new JButton("Send");
        sendButton.addActionListener(a -> sendCommand());

        Box b = Box.createHorizontalBox();
        b.add(msg);
        b.add(sendButton);

        add(b, BorderLayout.SOUTH);

        dateFormatPicker = new JComboBox<>(
                new String[]{"Select a date format...",
                             "dd/MM/YYYY",
                             "MM/dd/YYYY",
                             "dd/MM/YYYY HH:mm:ss",
                             "MM/dd/YYYY H:mm:ss",
                             "Custom..."});

        dateFormatPicker.addActionListener(a -> {
            String selection = (String) dateFormatPicker.getSelectedItem();
            assert selection != null;

            if(!selection.contains("...")) sendCommand("date " + selection);
            else enableInteraction(true);
        });

        dateFormatPicker.setVisible(false);
        add(dateFormatPicker, BorderLayout.NORTH);
    }

    private void sendCommand(String cmd){
        if(cmd.startsWith("date")) {
            if(!isDateFormatValid(cmd.substring(5))) {
                log("Wrong date format !");
                return;
            }
            isPickingDate = false;
            dateFormatPicker.setVisible(false);
            dateFormatPicker.setSelectedIndex(0);
        }

        try {
            client.send(cmd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            enableInteraction(true);
        }
    }

    private void sendCommand() {
        enableInteraction(false);
        String command = msg.getText();
        msg.setText("");

        if(command.equalsIgnoreCase("date")){
            pickDate();
        } else if (Objects.equals(dateFormatPicker.getSelectedItem(), "Custom...")) {
            sendCommand("date " + command);
        } else {
            sendCommand(command);
        }

    }

    private void enableInteraction(boolean enable) {
        msg.setEditable(enable);
        msg.setEnabled(enable);
        sendButton.setEnabled(enable);
    }

    private void pickDate() {
        isPickingDate = true;
        dateFormatPicker.setVisible(true);
    }

    private boolean isDateFormatValid(String dateFormat){
        /*Pattern pattern = Pattern.compile("", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(dateFormat);
        return !matcher.find();*/
        return dateFormat != null && dateFormat.length() > 0;
    }

    public void log(String msg) {
        log.append(msg);
        log.append("\n");
    }
}
