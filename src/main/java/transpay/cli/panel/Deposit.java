package transpay.cli.panel;

import java.util.Date;
import java.text.SimpleDateFormat;

import transpay.bank.Transaction;
import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.Receipt;
import transpay.cli.components.TypeWriter;

public class Deposit {
    public Deposit() {
        displayDepositPage();
        double amount = getAmount();
        Dashboard.getUserPIN();
        processDeposit(amount);
        Dashboard.returnToDashboard();
    }

    private void displayDepositPage() {
        new FlashWriter(Log.HEADING, "\t\t    Deposit to your Account\n", true);
        new TypeWriter(Log.BODY, "    If going here was a mistake, use 'exit' command\n", true);

        new TypeWriter(Log.BODY, "Deposit limit: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f\n", Transpay.depositLimit), true);

        new TypeWriter(Log.BODY, "Deposit multiple: PHP ", false);
        
        new FlashWriter(Log.HEADING, String.format("%,.2f\n", Transpay.depositMultiple), true);
    }

    private double getAmount() {
        new TypeWriter(Log.INPUT, "Enter the amount you want to deposit:", true);

        while (true) {
            String input = Dashboard.getValidatedInput(
                "PHP ", 
                test -> {
                    try {
                        double temp = Double.parseDouble(test);
                        return temp > 0 && temp <= Transpay.depositLimit && temp % Transpay.depositMultiple == 0;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                "Amount must be positive, within the deposit limit, and a multiple of PHP " + String.format("%,.2f", Transpay.depositMultiple) + ". Please try again.",
                false
            );

            if (input != null) {
                return Double.parseDouble(input);
            }
        }
    }

    private void processDeposit(double amount) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        
        double originalBalance = Transpay.account.getBalance();
        Transaction transaction = new Transaction(Transpay.account, amount, "Deposit", date);

        Transpay.account.deposit(amount);
        Transpay.bankSystem.addTransaction(transaction);
        Transpay.totalDeposits += amount;

        transaction.setAccountBalance(originalBalance + amount);

        new TypeWriter(Log.SUCCESS, "\nDeposit successful!\n", true);

        ConsoleLog.delay(1000);

        if (Receipt.isReceipt()) {
            new Receipt(originalBalance, amount, transaction);
        }
    }

    public static void redirectToDeposit() {
        new FlashWriter(Log.INFO, "Redirecting to Deposit...", false);
        ConsoleLog.clear(1000);
        new Deposit();
    }
}
