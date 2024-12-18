package transpay.cli.panel;

import java.util.function.Predicate;

import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Greet;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class Welcome {
    public Welcome() {
        new Greet(Transpay.brand.toUpperCase());

        new TypeWriter(Log.HEADING, "\t\t    Welcome to " + Transpay.brand + "!", true);
        new TypeWriter(Log.SYSTEM, "\t       Your trustworthy banking system\n", true);

        new TypeWriter(Log.SYSTEM, "Current Status: ", false);
        new FlashWriter(Log.HEADING, Transpay.status, true);
        
        new TypeWriter(Log.SYSTEM, "\nOptions\n", true);

        new TypeWriter(Log.OPTION, "1. ", false);
        new FlashWriter(Log.BODY, "Register an account", true);

        new TypeWriter(Log.OPTION, "2. ", false);
        new FlashWriter(Log.BODY, "Login", true);

        new TypeWriter(Log.OPTION, "3. ", false);
        new FlashWriter(Log.BODY, "Exit", true);

        new TypeWriter(Log.OPTION, "4. ", false);
        new FlashWriter(Log.BODY, "Administrator", true);

        handleAction();
    }

    private void handleAction() {
        new TypeWriter(Log.INPUT, "\nWhat would you like to do?", true);

        while (true) {
            int action = Integer.parseInt(getValidatedInput(
                "",
                test -> {
                    try {
                        if (!test.matches("\\d+")) {
                            throw new NumberFormatException();
                        }
    
                        int temp = Integer.parseInt(test);
                        
                        return temp >= 1 && temp <= 4;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }, 
                "Invalid choice. Please try again.",
                false
                ));

            if ((action == 1 || action == 2) && Transpay.status.equals("Offline")) {
                new FlashWriter(Log.ERROR, "The system is currently offline. Please try again later.", true);
                continue;
            }

            switch (action) {
                case 1:
                    ConsoleLog.clear(0);
                    new Register();
                    break;
                case 2:
                    ConsoleLog.clear(0);
                    new Login();
                    break;
                case 3:
                    Transpay.exit = true;
                    return;
                case 4:
                    ConsoleLog.clear(0);
                    new Admin();
                    break;
            }
        }
    }

    public static String getValidatedInput(String prompt, Predicate<String> validator, String errorMessage, boolean isPassword) {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt + prompt, false);
                String input = isPassword ? ConsoleLog.getPassword() : ConsoleLog.getInput();
                if (input.equalsIgnoreCase("exit")) {
                    ConsoleLog.clear(0);
                    new Welcome();
                    return null;
                }
                if (validator.test(input)) {
                    return input;
                }
                new FlashWriter(Log.ERROR, errorMessage, true);
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }

    public static String getValidatedInput(String prompt, Predicate<String> validator1, Predicate<String> validator2, String errorMessage1, String errorMessage2, boolean isPassword) {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt + prompt, false);
                String input = isPassword ? ConsoleLog.getPassword() : ConsoleLog.getInput();
                if (input.equalsIgnoreCase("exit")) {
                    ConsoleLog.clear(0);
                    new Welcome();
                    return null;
                }

                if (validator1.test(input)) {
                    if (validator2.test(input)) {
                        return input;
                    } else {
                        new FlashWriter(Log.ERROR, errorMessage2, true);
                    }
                } else {
                    new FlashWriter(Log.ERROR, errorMessage1, true);
                }
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }
   
    public static String getValidatedInput(String prompt, Predicate<String> validator1, Predicate<String> validator2, Predicate<String> validator3, String errorMessage1, String errorMessage2, String errorMessage3, boolean isPassword) {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt + prompt, false);
                String input = isPassword ? ConsoleLog.getPassword() : ConsoleLog.getInput();
                if (input.equalsIgnoreCase("exit")) {
                    ConsoleLog.clear(0);
                    new Welcome();
                    return null;
                }

                if (validator1.test(input)) {
                    if (validator2.test(input)) {
                        if (validator3.test(input)) {
                            return input;
                        } else {
                            new FlashWriter(Log.ERROR, errorMessage3, true);
                        }
                    } else {
                        new FlashWriter(Log.ERROR, errorMessage2, true);
                    }
                } else {
                    new FlashWriter(Log.ERROR, errorMessage1, true);
                }
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }
    
    public static void returnToWelcome() {
        new TypeWriter(Log.INPUT, "\nPress enter to go back:", true);
        new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
        ConsoleLog.getInput();

        new FlashWriter(Log.INFO, "Returning to Start page...", false);
        ConsoleLog.clear(1000);
        new Welcome();
    }
}
