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

public class Withdraw {
    public String accountNumber;
    public String PIN;
    public double amount;
    public double limit = 100000.00;
    private Scanner scan = Transpay.scan;
    private boolean back;
    private String date;

    public Withdraw() {
        new FlashWriter(Log.HEADING, "Withdraw from your Account\n", true);

        new TypeWriter(Log.BODY, "Withdrawal limit: PHP ", false);
        new FlashWriter(Log.INFO, String.format("%,.2f\n", limit), true);
        
        new TypeWriter(Log.BODY, "Available balance: PHP ", false);
        new FlashWriter(Log.INFO, String.format("%,.2f\n\n", Transpay.account.getBalance()), true);

        new TypeWriter(Log.BODY, "Enter the amount you want to withdraw:", true);
        getAmount();

        new TypeWriter(Log.BODY, "Enter your 6-digit PIN:", true);
        getUserPIN();

        withdraw();

        new TypeWriter(Log.BODY, "\nPress enter to go back:", true);
        goBack();

        if (back) {
            new TypeWriter(Log.INFO, "\nReturning to Dashboard page...", true);

            ConsoleLog.clear(1000);

            new Dashboard();
        }
    }

    private void getAmount() {
        while (true) {
            try {
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt + "PHP ", false);

                String input = ConsoleLog.getInput(scan);
                amount = Double.valueOf(input);
    
                if (amount > limit) {
                    new FlashWriter(Log.ERROR, String.format("Amount exceed the limit of PHP %,.2f. Please try again.", limit), true);
                    continue;
                }
                else if (amount <= 0) {
                    new FlashWriter(Log.ERROR, "Amount must be greater than PHP 0.00. Please try again.", true);
                    continue;
                }
                
                else if (amount > Transpay.account.getBalance()) {
                    new FlashWriter(Log.ERROR, "Insufficient balance. Please try again.", true);
                    continue;
                }
                break;
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }

    private void getUserPIN() {
        while (true) {  
            try {
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt, false);
                PIN = ConsoleLog.getInput(scan);
    
                if (!PIN.trim().matches("\\d{6}")) {
                    throw new NumberFormatException();
                }
                else if (!Transpay.account.getPIN().equals(PIN.trim())) {
                    new FlashWriter(Log.ERROR, "Incorrect PIN. Please try again.", true);
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                new FlashWriter(Log.ERROR, "PIN must be numeric and 6 digits long. Please try again.", true);
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }

    private void withdraw() {
        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        double originalBalance = Transpay.account.getBalance();
        Transaction transaction = new Transaction(Transpay.account, amount, "Withdrawal", date);

        Transpay.account.withdraw(amount);
        Transpay.bankSystem.addTransaction(transaction);
        
        new FlashWriter(Log.SUCCESS, "\nWithdrawal successful!\n", true);
        
        ConsoleLog.delay(1000);

        if (Receipt.isReceipt()) {
            new Receipt(originalBalance, amount, transaction);
        }
    }

    private void goBack() {
        while (true) {
            try {
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt, false);
                ConsoleLog.getInput(scan);
                back = true;
                break;
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }
}
