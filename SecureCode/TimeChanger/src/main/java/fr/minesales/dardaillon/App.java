package fr.minesales.dardaillon;

import com.sun.jna.Function;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.Arrays;

import static java.nio.file.attribute.AclEntryFlag.DIRECTORY_INHERIT;
import static java.nio.file.attribute.AclEntryFlag.FILE_INHERIT;

public class App {
    public static void main(String[] args) {
        Function function = Function.getFunction("kernel32", "SetSystemTime");

        WinBase.SYSTEMTIME s = new WinBase.SYSTEMTIME();
        s.wYear = Short.parseShort(args[0]);
        s.wMonth = Short.parseShort(args[1]);
        s.wDay = Short.parseShort(args[2]);
        s.wHour = Short.parseShort(args[3]);
        s.wMinute = Short.parseShort(args[4]);
        s.wSecond = Short.parseShort(args[5]);

        Object[] params = new Object[]{s};

        System.out.println("invoking...");
        boolean result = (Boolean) function.invoke(Boolean.class, params);
        System.out.println(result);
    }

    public void test() throws IOException {
        Path path = Paths.get("TimeChanger-jar-with-dependencies.jar");

        UserPrincipal authenticatedUsers = path.getFileSystem().getUserPrincipalLookupService()
                .lookupPrincipalByName("Authenticated Users");
        AclFileAttributeView view = Files.getFileAttributeView(path, AclFileAttributeView.class);

        // Create ACL to give "Authenticated Users" "modify" access
        AclEntry entry = AclEntry.newBuilder()
                .setType(AclEntryType.ALLOW)
                .setPrincipal(authenticatedUsers)
                .setFlags(DIRECTORY_INHERIT,
                        FILE_INHERIT)
                .setPermissions(AclEntryPermission.valueOf(WinNT.SE_SYSTEMTIME_NAME))
                .build();

        //WinNT.SE_SYSTEMTIME_NAME
    }
}
