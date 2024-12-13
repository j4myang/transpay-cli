package transpay.cli.panel;

import java.util.Scanner;

import transpay.bank.Transaction;
import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class AccountDetail {
    private int choice;
    private Scanner scan = Transpay.scan;
    private boolean back = false;

    public AccountDetail() {
        new FlashWriter(Log.HEADING, "\t\t    Your Account\n", true);

        new TypeWriter(Log.BODY, "Account Number: ", false);
        new FlashWriter(Log.HEADING, Transpay.account.getAccountNumber(), true);

        new TypeWriter(Log.BODY, "Account Name: ", false);
        new FlashWriter(Log.HEADING, Transpay.account.getName(), true);

        new TypeWriter(Log.BODY, "Balance: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", Transpay.account.getBalance()), true);

        new TypeWriter(Log.BODY, "PIN: ", false);
        new FlashWriter(Log.HEADING, "*".repeat(Transpay.account.getPIN().length()), true);

        new TypeWriter(Log.SYSTEM, "\n\nOptions", true);  

        new TypeWriter(Log.OPTION, "1. ", false);
        new FlashWriter(Log.BODY, "View transaction history", true);

        new TypeWriter(Log.OPTION, "2. ", false);
        new FlashWriter(Log.BODY, "Show PIN", true);

        new TypeWriter(Log.OPTION, "3. ", false);
        new FlashWriter(Log.BODY, "Go Back", true);

        new TypeWriter(Log.INPUT, "\nWhat would you like to do?", true);
        getInput();

        switch (choice) {
            case 1:
                ConsoleLog.clear(0);
                viewTransactionHistory();
                break;
            case 2:
                ConsoleLog.clear(0);
                showPIN();
                break;
            case 3:
                back = true;
                break;
        }

        if (back) {
            new FlashWriter(Log.INFO, "\nReturning to Dashboard page...", true);
            ConsoleLog.clear(1000);
            new Dashboard();
        }
    }

    private void getInput() {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                String input = ConsoleLog.getInput(scan);

                if (!input.matches("\\d+")) {
                    throw new NumberFormatException();
                }

                choice = Integer.parseInt(input);

                if (choice >= 1 && choice <= 3) {
                    break;
                }
                else {
                    new FlashWriter(Log.ERROR, "Invalid choice. Please try again.", true);
                }
            } catch (NumberFormatException e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void goBack() {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                ConsoleLog.getInput(scan);
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ConsoleLog.clear(0);
        new AccountDetail();
    }

    private void viewTransactionHistory() {
        while (true) {
            new FlashWriter(Log.HEADING, "\t\t    Your Transaction History\n", true);
    
            Transaction[] transactions = Transpay.bankSystem.getTransactionsByAccount(Transpay.account.getAccountNumber());
    
            if (transactions.length == 0) {
                new FlashWriter(Log.INFO, "No transactions found.", true);
            }
            else {
                for (Transaction transaction : transactions) {
                    new TypeWriter(Log.BODY, "Transaction Date: ", false);
                    new FlashWriter(Log.HEADING, transaction.getDate(), true);
    
                    new TypeWriter(Log.BODY, "Transaction Type: ", false);
                    new FlashWriter(Log.HEADING, transaction.getType(), true);
    
                    if (transaction.getTarget() != null) {
                        new TypeWriter(Log.BODY, "Receiver Account: ", false);
                        new FlashWriter(
                            Log.HEADING, 
                            transaction.getTarget().replace(
                                transaction.getTarget().substring(
                                    1, 
                                    transaction.getTarget().length() - 3),
                                "*".repeat(transaction.getTarget().length() - 3)),
                     true);
                    }
    
                    ConsoleLog.print("\t");

                    if (transaction.getType().equals("Withdrawal")) {
                        new TypeWriter(Log.ERROR, "-", false);
                    }
                    else if (transaction.getType().equals("Deposit")) {
                        new TypeWriter(Log.SUCCESS, "+", false);
                    }
                    else if (transaction.getType().equals("Transfer")) {
                        new TypeWriter(Log.INFO, "~", false);
                    }
                    new TypeWriter(Log.BODY, "  PHP ", false);
                    new FlashWriter(Log.HEADING, String.format("%,.2f", transaction.getAmount()), true);
                
                    ConsoleLog.print("\n\n");
                }
            }
    
            new FlashWriter(Log.INPUT, "Press enter to go back:", true);
            goBack();
        }
    }

    private void showPIN() {
        new FlashWriter(Log.HEADING, "\t\t    " + Transpay.account.getName(), true);

        ConsoleLog.print("\n");
        
        new TypeWriter(Log.BODY, "Your PIN is: ", false);
        new FlashWriter(Log.HEADING, Transpay.account.getPIN(), true);

        new FlashWriter(Log.BODY, "\n\nPress enter to go back:", true);
        goBack();
    }
}
