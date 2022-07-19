package fr.minesales.dardaillon;

import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import fr.minesales.dardaillon.elevations.Shell32X;
import fr.minesales.dardaillon.formatter.DateLabelFormatter;
import fr.minesales.dardaillon.graphics.Frame;
import fr.minesales.dardaillon.utils.OSUtils;

import java.text.ParseException;
import java.util.Calendar;

public class Main {

    public static void main(String[] args) {
        //executeAsAdministrator("java", "-jar TimeChanger-jar-with-dependencies.jar");

        //runWithPrivileges("java", "-jar TimeChanger-jar-with-dependencies.jar");

        new Frame();
    }

    public static void runWithPrivileges(String command, String args){
        switch (OSUtils.getOS()) {
            case WINDOWS: executeAsAdministrator(command, args); return;
            case LINUX: System.out.println("Work in progress for LINUX users..."); break;
            default: System.out.println("Not available for your Operating System ! (Available for : WINDOWS)"); break;
        }

        System.exit(0);
    }

    public static void executeAsAdministrator(String command, String args) {
        Shell32X.SHELLEXECUTEINFO execInfo = new Shell32X.SHELLEXECUTEINFO();
        execInfo.lpFile = new WString(command);
        if (args != null)
            execInfo.lpParameters = new WString(args);
        execInfo.nShow = Shell32X.SW_SHOWDEFAULT;
        execInfo.fMask = Shell32X.SEE_MASK_NOCLOSEPROCESS;
        execInfo.lpVerb = new WString("runas");
        boolean result = Shell32X.INSTANCE.ShellExecuteEx(execInfo);

        if (!result) {
            int lastError = Kernel32.INSTANCE.GetLastError();
            String errorMessage = Kernel32Util.formatMessageFromLastErrorCode(lastError);
            throw new RuntimeException("Error performing elevation: " + lastError + ": " + errorMessage + " (apperror=" + execInfo.hInstApp + ")");
        }
    }
}
