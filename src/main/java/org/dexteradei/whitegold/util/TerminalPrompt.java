package org.dexteradei.whitegold.util;

import java.io.Console;
import java.util.Scanner;

public class TerminalPrompt {
    public static String prompt(String message) {
        Console console = System.console();
        if (console != null) {
            return console.readLine("%s: ", message);
        } else {
            // fallback if running in IDE or system with no Console
            System.out.print(message + ": ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            scanner.close();

            return line;
        }
    }
}
