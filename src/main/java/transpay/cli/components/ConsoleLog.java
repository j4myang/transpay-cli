package transpay.cli.components;

import java.io.IOException;
import java.io.PrintStream;
import java.util.NoSuchElementException;

import transpay.cli.Transpay;

public class ConsoleLog {
    private static final String RESET_CODE = "\033[0m";
    private static final String CUSTOM_COLOR_CODE = "\033[38;2;%d;%d;%dm";
    private static PrintStream out = System.out;
    private static PrintStream err = System.err;
    public static final String inputPrompt = ">_ ";

    public static void logHeading(String text) {
        out.print(String.format(CUSTOM_COLOR_CODE, 183, 166, 76) + text + RESET_CODE);
    }

    public static void logBody(String text) {
        out.print(String.format(CUSTOM_COLOR_CODE, 221, 225, 226) + text + RESET_CODE);
    }

    public static void logSystem(String text) {
        out.print(String.format(CUSTOM_COLOR_CODE, 192, 193, 185) + text + RESET_CODE);
    }

    public static void logInfo(String text) {
        out.print(String.format(CUSTOM_COLOR_CODE, 160, 146, 179) + text + RESET_CODE);
    }

    public static void logOption(String text) {
        out.print(String.format(CUSTOM_COLOR_CODE, 198, 167, 183) + text + RESET_CODE);
    }

    public static void logInput(String text) {
        out.print(String.format(CUSTOM_COLOR_CODE, 112, 137, 159) + text + RESET_CODE);
    }
    
    public static void logSuccess(String text) {
        out.print(String.format(CUSTOM_COLOR_CODE, 120, 150, 96) + text + RESET_CODE);
    }

    public static void logError(String text) {
        err.print(String.format(CUSTOM_COLOR_CODE, 179, 96, 116) + text + RESET_CODE);
    }

    public static void clear(int milliDelay) {
        try {
            Thread.sleep(milliDelay);
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            logError("Error clearing console");
            e.printStackTrace();
        }
    }
    
    public static String getInput() {
        try {
            out.print(String.format(CUSTOM_COLOR_CODE, 112, 137, 159));
            String input = Transpay.scan.nextLine();
            out.print(RESET_CODE);
    
            return input;
        } catch (NoSuchElementException e) {
            Transpay.scan.close();
            ConsoleLog.logInfo("\nThanks for using " + Transpay.brand + "! Goodbye.");
            System.exit(0);
            return "";
        }
    }

    public static String getPassword() {
        try {
            System.out.print("\033[8m");
            String password = Transpay.scan.nextLine();
            System.out.print("\033[0m");
            return password;
        } catch (NoSuchElementException e) {
            Transpay.scan.close();
            ConsoleLog.logInfo("\nThanks for using " + Transpay.brand +"! Goodbye.");
            System.exit(0);
            return "";
        }
    }

    public static void decolorize() {
        out.print(RESET_CODE);
    }

    public static void print(String text) {
        out.print(text);
    }

    public static void delay(int milliDelay) {
        try {
            Thread.sleep(milliDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
