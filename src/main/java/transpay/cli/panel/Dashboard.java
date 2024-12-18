package transpay.cli.panel;

import java.time.LocalTime;
import java.util.function.Predicate;

import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class Dashboard {
    public Dashboard() {
        displayDashboardPage();

        new TypeWriter(Log.INPUT, "\nWhat would you like to do?", true);

        while (true) {
            int choice = Integer.parseInt(Welcome.getValidatedInput("", test -> {
                try {
                    if (!test.matches("\\d+")) {
                        throw new NumberFormatException();
                    }

                    int temp = Integer.parseInt(test);
                    
                    return temp >= 1 && temp <= 9;
                } catch (NumberFormatException e) {
                    return false;
            }}, "Invalid choice. Please try again.", false));

            handleAction(choice);
        }
    }

    private void displayDashboardPage() {
        new FlashWriter(Log.HEADING, "\t\t    Good " + computeDayMessage() + ", " + Transpay.account.getName() + "!\n", true);

        new TypeWriter(Log.SYSTEM, "Current Status: ", false);
        new FlashWriter(Log.HEADING, Transpay.status, true);

        new TypeWriter(Log.SYSTEM, "\nTransactions\n", true);

        new TypeWriter(Log.OPTION, "1. ", false);
        new FlashWriter(Log.BODY, "Withdraw", true);

        new TypeWriter(Log.OPTION, "2. ", false);
        new FlashWriter(Log.BODY, "Deposit", true);

        new TypeWriter(Log.OPTION, "3. ", false);
        new FlashWriter(Log.BODY, "Transfer", true);

        new TypeWriter(Log.OPTION, "4. ", false);
        new FlashWriter(Log.BODY, "View balance", true);

        new TypeWriter(Log.BODY, "\nSettings\n", true);

        new TypeWriter(Log.OPTION, "5. ", false);
        new FlashWriter(Log.BODY, "View account details", true);

        new TypeWriter(Log.OPTION, "6. ", false);
        new FlashWriter(Log.BODY, "Manage account", true);

        new TypeWriter(Log.OPTION, "7. ", false);
        new FlashWriter(Log.BODY, "Logout", true);

        new TypeWriter(Log.OPTION, "8. ", false);
        new FlashWriter(Log.BODY, "Exit", true);

        new TypeWriter(Log.BODY, "\nAdvanced\n", true);

        new TypeWriter(Log.OPTION, "9. ", false);
        new FlashWriter(Log.BODY, "Delete account", true);
    }

    private void handleAction(int action) {
        if (action >= 1 && action <= 3 && Transpay.status.equals("Under Maintenance")) {
            new FlashWriter(Log.ERROR, "The system is currently Under Maintenance. Please try again later.", true);
            return;
        }

        switch (action) {
            case 1:
                ConsoleLog.clear(0); 
                new Withdraw();
                break;
            case 2:
                ConsoleLog.clear(0); 
                new Deposit();
                break;
            case 3:
                ConsoleLog.clear(0); 
                new Transfer();
                break;
            case 4:
                ConsoleLog.clear(0);
                new Balance();
                break;
            case 5:
                ConsoleLog.clear(0);
                new AccountDetail();
                break;
            case 6:
                ConsoleLog.clear(0);
                new ManageAccount();
                break;
            case 7:
                ConsoleLog.clear(0);
                new Welcome();
                break;
            case 8:
                ConsoleLog.clear(0);
                Transpay.exit = true;
                break;
            case 9:
                ConsoleLog.clear(0);
                new Delete();
                break;
        }
    }

    public static String getValidatedInput(String prompt, Predicate<String> validator, String errorMessage, boolean isPassword) {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt + prompt, false);
                String input = isPassword ? ConsoleLog.getPassword() : ConsoleLog.getInput();
                if (input.equalsIgnoreCase("exit")) {
                    ConsoleLog.clear(0);
                    new Dashboard();
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
                    new Dashboard();
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
                    new Dashboard();
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
    
    public static void getUserPIN() {
        new TypeWriter(Log.INPUT, "\nEnter your 6-digit PIN (hidden for security):", true);

        while (true) {
            String pin = getValidatedInput(
                "", 
                test1 -> test1.matches(Transpay.pinPattern), 
                test2 -> test2.equals(Transpay.account.getPIN()),
                "PIN must be numeric and 6 digits long. Please try again.", 
                "PIN is incorrect. Please try again.",
                true);

            if (pin != null) {
                break;
            }
        }
    }

    public static void returnToDashboard() {
        new TypeWriter(Log.INPUT, "\nPress enter to go back:", true);
        new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
        ConsoleLog.getInput();

        new FlashWriter(Log.INFO, "Returning to Dashboard...", false);

        ConsoleLog.clear(1000);

        new Dashboard();
    }

    public static String computeDayMessage() {
        int currentHour = LocalTime.now().getHour();

        return currentHour < 12 ? "Morning" : currentHour < 18 ? "Afternoon" : "Evening";
    }
}
