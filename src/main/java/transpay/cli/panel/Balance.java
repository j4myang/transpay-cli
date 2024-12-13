package transpay.cli.panel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import transpay.bank.Transaction;
import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.Receipt;
import transpay.cli.components.TypeWriter;

public class Balance {
    private int choice;
    private String date;
    private Scanner scan = Transpay.scan;

    public Balance() {
        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Transaction transaction = new Transaction(Transpay.account, 0, "Balance Inquiry", date);

        new FlashWriter(Log.BODY, "Your account balance is: ", false);
        new FlashWriter(Log.BODY, "PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", Transpay.account.getBalance()), true);

        new FlashWriter(Log.BODY, "Current Date: ", false);
        new FlashWriter(Log.HEADING, date, true);

        ConsoleLog.print("\n");

        if (Transpay.status.equals("Maintenance")) {
            new FlashWriter(Log.INFO, "The system cannot curently print a receipt", true);
        } 
        else if (Receipt.isReceipt()) {
            new Receipt(Transpay.account.getBalance(), 0, transaction);
        }
        
        new TypeWriter(Log.SYSTEM, "\nQuick Transactions", true);

        new TypeWriter(Log.OPTION, "1. ", false);
        new FlashWriter(Log.BODY, "Withdraw", true);

        new TypeWriter(Log.OPTION, "2. ", false);
        new FlashWriter(Log.BODY, "Deposit", true);

        new TypeWriter(Log.OPTION, "3. ", false);
        new FlashWriter(Log.BODY, "Transfer", true);

        new TypeWriter(Log.OPTION, "4. ", false);
        new FlashWriter(Log.BODY, "Go Back", true);

        new TypeWriter(Log.INPUT, "\nWhat would you like to do?", true);

        while (true) {
            getInput();

            switch (choice) {
                case 1:
                    if (Transpay.status.equals("Maintenance")) {
                        new FlashWriter(Log.ERROR, "The system is currently under maintenance. Please try again later.", true);
                        continue;
                    }
                    else {
                        new FlashWriter(Log.INFO, "\nRedirecting to Withdraw page...", true);
                        ConsoleLog.clear(1000);
                        new Withdraw();
                        break;
                    }
                case 2:
                    if (Transpay.status.equals("Maintenance")) {
                        new FlashWriter(Log.ERROR, "The system is currently under maintenance. Please try again later.", true);
                        continue;
                    }
                    else {
                        new FlashWriter(Log.INFO, "\nRedirecting to Deposit page...", true);
                        ConsoleLog.clear(1000);
                        new Deposit();
                        break;
                    }
                case 3:
                    if (Transpay.status.equals("Maintenance")) {
                        new FlashWriter(Log.ERROR, "The system is currently under maintenance. Please try again later.", true);
                        continue;
                    }
                    else {
                        new FlashWriter(Log.INFO, "\nRedirecting to Transfer page...", true);
                        ConsoleLog.clear(1000);
                        new Transfer();
                        break;
                    }
                case 4:
                    new FlashWriter(Log.INFO, "\nReturning to Dashboard page...", true);
                    ConsoleLog.clear(1000);
                    new Dashboard();
                    break;
            }    
        } 
    }

    public void getInput() {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                String input = ConsoleLog.getInput(scan);

                if (!input.matches("\\d+")) {
                    throw new NumberFormatException();
                }

                choice = Integer.parseInt(input);

                if (choice >=1 && choice <= 4) {
                    break;
                }
                else {
                    new FlashWriter(Log.ERROR, "Invalid choice. Please try again.", true);
                }
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }
}
