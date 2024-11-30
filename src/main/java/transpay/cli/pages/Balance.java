package transpay.cli.pages;

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

        new FlashWriter(Log.HEADING, "Your account balance is: PHP ", false);
        new FlashWriter(Log.INFO, String.format("%,.2f", Transpay.account.getBalance()), true);

        new FlashWriter(Log.HEADING, "Current Date: ", false);
        new FlashWriter(Log.INFO, date, true);

        if (Receipt.isReceipt()) {
            new Receipt(Transpay.account.getBalance(), 0, transaction);
        }
        
        ConsoleLog.delay(1000);
        
        new TypeWriter(Log.BODY, "\nQuick Transactions", true);

        new FlashWriter(Log.OPTION, "1. Withdraw", true);
        new FlashWriter(Log.OPTION, "2. Deposit", true);
        new FlashWriter(Log.OPTION, "3. Transfer", true);
        new FlashWriter(Log.OPTION, "4. Go Back", true);

        new TypeWriter(Log.BODY, "\nWhat would you like to do?", true);

        getInput();

        switch (choice) {
            case 1:
                new FlashWriter(Log.INFO, "\nRedirecting to Withdraw page...", true);
                ConsoleLog.clear(1000);
                new Withdraw();
                break;
            case 2:
                new FlashWriter(Log.INFO, "\nRedirecting to Deposit page...", true);
                ConsoleLog.clear(1000);
                new Deposit();
                break;
            case 3:
                new FlashWriter(Log.INFO, "\nRedirecting to Transfer page...", true);
                ConsoleLog.clear(1000);
                new Transfer();
                break;
            case 4:
                new FlashWriter(Log.INFO, "\nReturning to Dashboard page...", true);
                ConsoleLog.clear(1000);
                new Dashboard();
                break;
        }            
    }

    public void getInput() {
        while (true) {
            try {
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt, false);
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
