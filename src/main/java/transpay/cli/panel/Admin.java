package transpay.cli.panel;

import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class Admin {
    String id = "";

    public Admin() {
        new FlashWriter(Log.HEADING, "\t\t  Log in as Administrator\n", true);
        new TypeWriter(Log.BODY, "    If going here was a mistake, use 'exit' command\n", true);

        new TypeWriter(Log.INPUT, "Enter your ID Number: ", true);
        id = getID();
        
        new TypeWriter(Log.SUCCESS, "\nLogin successful! Please wait a moment...", true);

        ConsoleLog.clear(1000);

        displayAdminPage();
    }

    private String getID() {
        while (true) {
            String input = Welcome.getValidatedInput(
                "", 
                test -> {
                    return test.matches("\\d{10}");
                },
                 "Invalid ID. Please try again.",
                  false);

            if (input != null) {
                return input;
            }
        }
    }

    private void displayAdminPage() {
        new FlashWriter(Log.HEADING, "\t\t    Good " + Dashboard.computeDayMessage() + ", " + id + "!\n", true);

        new TypeWriter(Log.SYSTEM, "\t\t    ATM Stats\n", true);

        new TypeWriter(Log.SYSTEM, "Current Status: ", false);
        new FlashWriter(Log.HEADING, Transpay.status + "\n", true);

        new TypeWriter(Log.SYSTEM, "\nActions\n", true);

        new TypeWriter(Log.OPTION, "1. ", false);
        new FlashWriter(Log.BODY, "Fix", true);

        new TypeWriter(Log.OPTION, "2. ", false);
        new FlashWriter(Log.BODY, "Show ATM Report", true);

        new TypeWriter(Log.OPTION, "3. ", false);
        new FlashWriter(Log.BODY, "Go Back", true);

        handleAction();
    }

    private void handleAction() {
        new TypeWriter(Log.INPUT, "\nWhat would you like to do?", true);

        while (true) {
            String action = Welcome.getValidatedInput(
                "", 
                test -> {
                    return Double.parseDouble(test) >= 1 && Double.parseDouble(test) <= 3;
                },
                 "Invalid input. Please try again.",
                  false
            );

            switch (action) {
                case "1":
                    if (Transpay.status.equals("Online")) {
                        new FlashWriter(Log.ERROR, "The system is already online.", true);
                        break;
                    }
                    
                    new FlashWriter(Log.INFO, "\nFixing the system...", true);

                    ConsoleLog.delay(1000);

                    Transpay.status = "Online";
                    
                    new TypeWriter(Log.SUCCESS, "\nThe system is now online.", true);
                    break;

                case "2":
                    ConsoleLog.clear(0);
                    displayAtmReport();
                    break;
                
                case "3":
                    ConsoleLog.clear(0);
                    new Welcome();
                    break;
            }
        }
    }

    private void displayAtmReport() {
        new TypeWriter(Log.SYSTEM, "Total Accounts: ", false);
        new FlashWriter(Log.HEADING, String.format("%,d\n", Transpay.accountSystem.length()), true);

        new TypeWriter(Log.SYSTEM, "Total Transactions: ", false);
        new FlashWriter(Log.HEADING, String.format("%,d\n", Transpay.bankSystem.length()), true);

        new TypeWriter(Log.SYSTEM, "Total Circulated Funds: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f\n", Transpay.getTotalCirculatedFunds()), true);

        new TypeWriter(Log.SYSTEM, "\tTotal Withdrawals: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", Transpay.totalWithdrawals), true);

        new TypeWriter(Log.SYSTEM, "\tTotal Deposits: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", Transpay.totalDeposits), true);

        new TypeWriter(Log.SYSTEM, "\tTotal Transfers: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", Transpay.totalTransfers), true);

        returnToAdmin();
    }

    private void returnToAdmin() {
        new TypeWriter(Log.INPUT, "\nPress enter to go back:", true);
        new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
        ConsoleLog.getInput();

        new FlashWriter(Log.INFO, "Returning to Admin...", false);
        ConsoleLog.clear(1000);
        displayAdminPage();
    }
}
