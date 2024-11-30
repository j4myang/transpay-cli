package transpay.cli.pages;

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
        new FlashWriter(Log.HEADING, "Your Account\n", true);

        new TypeWriter(Log.BODY, "Account Number: ", false);
        new FlashWriter(Log.INFO, Transpay.account.getAccountNumber(), true);

        new TypeWriter(Log.BODY, "Account Name: ", false);
        new FlashWriter(Log.INFO, Transpay.account.getName(), true);

        new TypeWriter(Log.BODY, "Balance: PHP ", false);
        new FlashWriter(Log.INFO, String.format("%,.2f", Transpay.account.getBalance()), true);

        new TypeWriter(Log.BODY, "PIN: ", false);
        new FlashWriter(Log.INFO, "*".repeat(Transpay.account.getPIN().length()), true);

        new TypeWriter(Log.BODY, "\n\nOptions", true);  
        new FlashWriter(Log.OPTION, "1. View Transaction History", true);
        new FlashWriter(Log.OPTION, "2. Show PIN", true);
        new FlashWriter(Log.OPTION, "3. Go Back", true);

        new TypeWriter(Log.BODY, "\nWhat would you like to do?", true);
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
            new TypeWriter(Log.INFO, "\nReturning to Dashboard page...", true);
            ConsoleLog.clear(1000);
            new Dashboard();
        }
    }

    private void getInput() {
        while (true) {
            try {
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt, false);
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
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt, false);
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
            new FlashWriter(Log.INFO, Transpay.account.getName(), true);
            new FlashWriter(Log.HEADING, "Your Transaction History\n", true);
    
            Transaction[] transactions = Transpay.bankSystem.getTransactionsByAccount(Transpay.account.getAccountNumber());
    
            if (transactions.length == 0) {
                new FlashWriter(Log.INFO, "No transactions found.", true);
            }
            else {
                for (Transaction transaction : transactions) {
                    new TypeWriter(Log.BODY, "Transaction Date: ", false);
                    new FlashWriter(Log.INFO, transaction.getDate(), true);
    
                    new TypeWriter(Log.BODY, "Transaction Type: ", false);
                    new FlashWriter(Log.INFO, transaction.getType(), true);
    
                    if (transaction.getTarget() != null) {
                        new TypeWriter(Log.BODY, "Transferred to ", false);
                        new FlashWriter(Log.INFO, transaction.getTarget(), true);
                    }
    
                    if (!transaction.getType().equals("Balance Inquiry")) {
                        ConsoleLog.print("\t");
                        new TypeWriter(Log.BODY, transaction.getType().equals("Withdrawal") ? "-" : "+", false);
                        new FlashWriter(Log.INFO, String.format("PHP %,.2f", transaction.getAmount()), true);
                    }

                    ConsoleLog.print("\n\n");
                }
            }
    
            new FlashWriter(Log.BODY, "\nPress enter to go back:", true);
            goBack();
        }
    }

    private void showPIN() {
        new FlashWriter(Log.INFO, "Your PIN is: " + Transpay.account.getPIN(), true);
        
        new FlashWriter(Log.BODY, "Press enter to go back:", true);
        goBack();
    }
}
