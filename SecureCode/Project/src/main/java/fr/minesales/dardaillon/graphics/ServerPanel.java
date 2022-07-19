package fr.minesales.dardaillon.graphics;

import fr.minesales.dardaillon.Main;

import javax.swing.*;
import java.awt.*;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.Date;

public class ServerPanel extends JPanel {
    private final JTextArea log;
    private final JButton setTimeButton, saveButton, cancelButton;
    private final JSpinner selectYear;
    private final Box bTime;

    public ServerPanel() {
        setLayout(new BorderLayout());

        log = new JTextArea();
        log.setEditable(false);
        add(log, BorderLayout.CENTER);

        setTimeButton = new JButton("Set System Time");
        setTimeButton.addActionListener(a -> enableSetTime(true));

        bTime = Box.createVerticalBox();
        Box bSpinner = Box.createHorizontalBox();

        selectYear = new JSpinner(new SpinnerDateModel());

        bSpinner.add(selectYear);

        saveButton = new JButton("Save");
        saveButton.addActionListener(a -> {
            Date selectedDate = (Date) selectYear.getValue();
            int year = selectedDate.getYear()+1900;
            int month = selectedDate.getMonth()+1;
            int day = selectedDate.getDate();
            int hour = selectedDate.getHours();
            int minute = selectedDate.getMinutes();
            int second = selectedDate.getSeconds();
            enableSetTime(false);
            Main.runWithPrivileges("java", "-jar TimeSetup-jar-with-dependencies.jar " +
                    year + " " + month + " " + day + " " + hour + " " + minute + " " + second);
        });
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(a -> enableSetTime(false));

        bTime.add(bSpinner);
        bTime.add(saveButton);
        bTime.add(cancelButton);

        bTime.setVisible(false);
        add(setTimeButton, BorderLayout.NORTH);

    }

    public void enableSetTime(boolean enable) {
        bTime.setVisible(enable);
        setTimeButton.setVisible(!enable);
        if(enable) {
            add(bTime, BorderLayout.NORTH);
        } else {
            add(setTimeButton, BorderLayout.NORTH);
        }
    }

    public void log(String msg) {
        log.append(msg);
        log.append("\n");
        System.out.println(msg);
    }
}
