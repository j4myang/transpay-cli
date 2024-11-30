package transpay.cli.components;

import java.io.IOException;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class ConsoleLog {
    private static final String RESET_CODE = "\033[0m";
    private static PrintStream out = System.out;
    private static PrintStream err = System.err;
    public static final String inputPrompt = "/> ";

    public static void logHeading(String text) {
        out.print(Color.BLUE + text + RESET_CODE);
    }

    public static void logBody(String text) {
        out.print(Color.WHITE + text + RESET_CODE);
    }

    public static void logSuccess(String text) {
        out.print(Color.GREEN + text + RESET_CODE);
    }

    public static void logWarning(String text) {
        out.print(Color.YELLOW + text + RESET_CODE);
    }

    public static void logInfo(String text) {
        out.print(Color.CYAN + text + RESET_CODE);
    }

    public static void logOption(String text) {
        out.print(Color.MAGENTA + text + RESET_CODE);
    }

    public static void logError(String text) {
        err.print(Color.RED + text + "\n" + RESET_CODE);
    }

    public static void clear(int milliDelay) {
        try {
            Thread.sleep(0);
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            logError("Error clearing console");
            e.printStackTrace();
        }
    }
    
    public static String getInput(Scanner scan) {
        try {
            out.print(Color.CYAN);
            String input = scan.nextLine();
            out.print(RESET_CODE);
    
            return input;
        } catch (NoSuchElementException e) {
            scan.close();
            ConsoleLog.logInfo("\nThanks for using Transpay! Goodbye.");
            System.exit(0);
            return "";
        }
    }

    public static void colorize(Color color) {
        out.print(color);
    }

    public static void decolorize() {
        out.print(RESET_CODE);
    }

    public static void print(String text) {
        out.print(text);
    }

    public static void delay(int milliDelay) {
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
