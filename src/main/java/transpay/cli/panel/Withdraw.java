package transpay.cli.panel;

import java.text.SimpleDateFormat;
import java.util.Date;

import transpay.bank.Transaction;
import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.Receipt;
import transpay.cli.components.TypeWriter;

public class Withdraw {
    public Withdraw() {
        displayWithdrawPage();
        double amount = getAmount();    
        Dashboard.getUserPIN();
        processWithdraw(amount);
        Dashboard.returnToDashboard();
    }

    private void displayWithdrawPage() {
        new FlashWriter(Log.HEADING, "\t\t    Withdraw from your Account\n", true);
        new TypeWriter(Log.SYSTEM, "    If going here was a mistake, use 'exit' command\n", true);
        
        new TypeWriter(Log.BODY, "Withdrawal limit: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f\n", Transpay.depositLimit), true);
        
        new TypeWriter(Log.BODY, "Withdrawal multiple: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f\n", Transpay.depositMultiple), true);

        new TypeWriter(Log.BODY, "Available balance: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f\n\n", Transpay.account.getBalance()), true);
    }

    private double getAmount() {
        new TypeWriter(Log.INPUT, "Enter the amount you want to withdraw:", true);

        while (true) {
            String input = Dashboard.getValidatedInput(
                "PHP ",
                 test1 -> {
                    return Double.parseDouble(test1) <= Transpay.account.getBalance();
                 }, 
                 test2 -> {
                    double temp = Double.parseDouble(test2);
                    return temp > 0 && temp <= Transpay.depositLimit && temp % Transpay.depositMultiple == 0;
                 }, 
                 "Insufficient account balance. Please try again.",
                 "Amount must be positive, within the withdrawal limit, and a multiple of PHP " + String.format("%,.2f", Transpay.depositMultiple) + ". Please try again.",
                   false);
            
            if (input != null) {
                return Double.parseDouble(input);
            }
        }
    }

    private void processWithdraw(double amount) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        double originalBalance = Transpay.account.getBalance();
        Transaction transaction = new Transaction(Transpay.account, amount, "Withdrawal", date);

        Transpay.account.withdraw(amount);
        Transpay.bankSystem.addTransaction(transaction);
        Transpay.totalWithdrawals += amount;

        transaction.setAccountBalance(originalBalance - amount);
        
        new TypeWriter(Log.SUCCESS, "\nWithdrawal successful!\n", true);
        
        ConsoleLog.delay(1000);

        if (Receipt.isReceipt()) {
            new Receipt(originalBalance, amount, transaction);
        }
    }

    public static void redirectToWithdraw() {
        new FlashWriter(Log.INFO, "Redirecting to Withdraw...", false);
        ConsoleLog.clear(1000);
        new Withdraw();
    }
}
