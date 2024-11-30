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

public class Transfer {
    private String PIN;
    private double limit = 500000.00;
    private double amount;
    private String targetAccountNumber;
    private Scanner scan = Transpay.scan;
    private boolean back;
    private String date;

    public Transfer() {
        new FlashWriter(Log.HEADING, "Transfer to another Account\n", true);

        new TypeWriter(Log.BODY, "Transfer limit: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f\n", limit), true);

        new TypeWriter(Log.BODY, "Enter the account number you want to transfer to:", true);
        getTargetAccountNumber();

        new TypeWriter(Log.BODY, "Enter the amount you want to transfer:", true);
        getAmount();

        new TypeWriter(Log.BODY, "Enter your 6-digit PIN:", true);
        getUserPIN();

        transfer();

        new TypeWriter(Log.BODY, "\nPress enter to go back:", true);
        goBack();

        if (back) {
            new TypeWriter(Log.INFO, "\nReturning to Dashboard page...", true);

            ConsoleLog.clear(1000);

            new Dashboard();
        }
    }

    private void getTargetAccountNumber() {
        while (true) {  
            try {
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt, false);
                targetAccountNumber = ConsoleLog.getInput(scan);
    
                if (targetAccountNumber.isBlank()) {
                    new FlashWriter(Log.ERROR, "Account number cannot be empty. Please try again.", true);
                    continue;
                }
                else if (Transpay.accountSystem.getAccount(targetAccountNumber) == null) {
                    new FlashWriter(Log.ERROR, "Account number not found. Please try again.", true);
                    continue;
                }
                else if (targetAccountNumber.equals(Transpay.account.getAccountNumber())) {
                    new FlashWriter(Log.ERROR, "You cannot transfer to your own account. Please try again.", true);
                    continue;
                }
                break;
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
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

    private void transfer() {
        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Transaction transaction = new Transaction(Transpay.account, amount, "Transfer", date);
        transaction.setTarget(targetAccountNumber);

        Transpay.account.transfer(Transpay.accountSystem.getAccount(targetAccountNumber), amount);
        Transpay.bankSystem.addTransaction(transaction);

        new FlashWriter(Log.SUCCESS, "\nTransfer successful!\n", true);

        ConsoleLog.delay(1000);

        if (Receipt.isReceipt()) {
            new Receipt(Transpay.account.getBalance(), amount, transaction);
        }
    }

    private void goBack() {
        while (true) {
            try {
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt, false);
                String input = ConsoleLog.getInput(scan);
                
                if (!input.isBlank()) {
                    new FlashWriter(Log.ERROR, "Invalid input. Please try again.", false);
                }
                back = true;
                break;
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }
}

