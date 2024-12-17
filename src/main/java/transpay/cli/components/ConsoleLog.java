package transpay.cli.components;

import java.io.IOException;
import java.io.PrintStream;
import java.util.NoSuchElementException;

import transpay.cli.Transpay;

public class ConsoleLog {
    public static final String RESET_CODE = "\033[0m";
    private static final String CUSTOM_COLOR_CODE = "\033[38;2;%d;%d;%dm";
    private static PrintStream out = System.out;
    private static PrintStream err = System.err;
    public static final String inputPrompt = ">_ ";
    public static final String colorHeading = String.format(CUSTOM_COLOR_CODE, 245, 227, 92);
    public static final String colorBody = String.format(CUSTOM_COLOR_CODE, 249, 249, 252);
    public static final String colorSystem = String.format(CUSTOM_COLOR_CODE, 210, 197, 185);
    public static final String colorInfo = String.format(CUSTOM_COLOR_CODE, 95, 192, 182);
    public static final String colorOption = String.format(CUSTOM_COLOR_CODE, 31, 121, 178);
    public static final String colorInput = String.format(CUSTOM_COLOR_CODE, 250, 182, 194);
    public static final String colorSuccess = String.format(CUSTOM_COLOR_CODE, 178, 220, 103);
    public static final String colorError = String.format(CUSTOM_COLOR_CODE, 247, 70, 50);

    public static void logHeading(String text) {
        out.print(colorHeading + text + RESET_CODE);
    }

    public static void logBody(String text) {
        out.print(colorBody + text + RESET_CODE);
    }

    public static void logSystem(String text) {
        out.print(colorSystem + text + RESET_CODE);
    }

    public static void logInfo(String text) {
        out.print(colorInfo + text + RESET_CODE);
    }

    public static void logOption(String text) {
        out.print(colorOption + text + RESET_CODE);
    }

    public static void logInput(String text) {
        out.print(colorInput + text + RESET_CODE);
    }
    
    public static void logSuccess(String text) {
        out.print(colorSuccess + text + RESET_CODE);
    }

    public static void logError(String text) {
        err.print(colorError + text + RESET_CODE);
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
