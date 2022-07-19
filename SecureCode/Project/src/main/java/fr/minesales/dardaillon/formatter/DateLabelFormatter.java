package fr.minesales.dardaillon.formatter;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

    private String datePattern = "dd/MM/yyyy HH:mm";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    public void setDatePattern(String pattern) throws IllegalArgumentException{
        dateFormatter = new SimpleDateFormat(pattern);
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }

    @Override
    public String valueToString(Object value) {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }

        return "";
    }

}
