package transpay.cli.panel;

import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class Admin {
    
    private String idNum;
    private String input;

    public Admin() {
        new FlashWriter(Log.HEADING, "\t\t  Log in as Administrator\n", true);
        new TypeWriter(Log.BODY, "    If going here was a mistake, use 'exit' command\n", true);

        new TypeWriter(Log.INPUT, "Enter your ID Number: ", true);
        getID();
        
        new TypeWriter(Log.SUCCESS, "\nLogin successful! Please wait a moment...", true);

        ConsoleLog.clear(1000);

        new FlashWriter(Log.HEADING, "\t\t    Good " + Dashboard.computeDayMessage() + ", " + idNum + "!\n", true);

        new TypeWriter(Log.SYSTEM, "ATM Stats\n", true);

        new TypeWriter(Log.SYSTEM, "Current Status: ", false);
        new FlashWriter(Log.HEADING, Transpay.status + "\n", true);

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

        new TypeWriter(Log.SYSTEM, "\nActions\n", true);

        new TypeWriter(Log.OPTION, "1. ", false);
        new FlashWriter(Log.BODY, "Fix", true);

        new TypeWriter(Log.OPTION, "2. ", false);
        new FlashWriter(Log.BODY, "Go Back", true);

        new TypeWriter(Log.INPUT, "\nWhat would you like to do?", true);
        getInput();
    }

    private void getID() {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                idNum = ConsoleLog.getInput();

                if (idNum.equalsIgnoreCase("exit")) {
                    new Welcome();
                    return;
                }
                else if (idNum.isBlank()) {
                    new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
                    continue;
                }
                else if (!idNum.matches("\\d{10}")) {
                    throw new NumberFormatException();
                }
                break;
            } catch (NumberFormatException e) {
                new FlashWriter(Log.ERROR, "PIN must be numeric and 6 digits long. Please try again.", true);
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }

    private void getInput() {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                input = ConsoleLog.getInput();

                if (input.equals("1")) {
                    if (Transpay.status.equals("Online")) {
                        new FlashWriter(Log.INFO, "The system is already online.", true);
                    }
                    else {
                        new FlashWriter(Log.INFO, "\nFixing the system...", true);
                        ConsoleLog.delay(1500);
                        Transpay.status = "Online";
                        new FlashWriter(Log.SUCCESS, "\nThe system is now online!", true);
                        ConsoleLog.delay(1000);
                    }
                }

                else if (input.equals("2")) {
                    new Welcome();
                    break;
                }
                else {
                    new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
                }
            } catch (NumberFormatException e) {
                new FlashWriter(Log.ERROR, "PIN must be numeric and 6 digits long. Please try again.", true);
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }

    
}
