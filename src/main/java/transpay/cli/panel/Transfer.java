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

public class Transfer {
    public Transfer() {
        displayTransferPage();
        String targetAccount = getTargetAccountNumber();
        double amount = getAmount();
        Dashboard.getUserPIN();
        processTransfer(targetAccount, amount);
        Dashboard.returnToDashboard();
    }

    private void displayTransferPage() {
        new FlashWriter(Log.HEADING, "\t\t    Transfer to another Account\n", true);
        new TypeWriter(Log.BODY, "    If going here was a mistake, use 'exit' command\n", true);

        new TypeWriter(Log.BODY, "Transfer limit: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f\n", Transpay.transferLimit), true);

        new TypeWriter(Log.BODY, "Available balance: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f\n\n", Transpay.account.getBalance()), true);
    }

    private String getTargetAccountNumber() {
        new TypeWriter(Log.INPUT, "Enter the account number you want to transfer to:", true);

        while (true) {
            String input = Dashboard.getValidatedInput(
                "", 
                test1 -> {
                    return !test1.isBlank();
                },
                test2 -> {
                    return Transpay.accountSystem.getAccount(test2) != null;
                }, 
                test3 -> {
                    return !test3.equals(Transpay.account.getAccountNumber());
                }, 
                "Account number cannot be empty. Please try again.", 
                "Account not found. Please try again.",
                "You cannot transfer to your own account. Please try again.",
                false);

            if (input != null) {
                return input;
            }
        }
    }

    private double getAmount() {
        new TypeWriter(Log.INPUT, "\nEnter the amount you want to transfer:", true);

        while (true) {
            String input = Dashboard.getValidatedInput(
                "PHP ", 
                test1 -> {
                    return Double.parseDouble(test1) < Transpay.account.getBalance();
                }, 
                test2 -> {
                    return Double.parseDouble(test2) < Transpay.transferLimit;
                }, 
                test3 -> {
                    return Double.parseDouble(test3) > 0;
                }, 
                "Insufficient account balance. Please try again.",
                "Amount exceeded the transfer limit. Please try again.",
                "Amount must be a positive digit. Please try again.",
                false
                );
            
            if (input != null) {
                return Double.parseDouble(input);
            }
        }
    }

    private void processTransfer(String targetAccount, double amount) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Transaction transaction = new Transaction(Transpay.account, amount, "Transfer", date);
        transaction.setTarget(targetAccount);
        transaction.setTransferType("Sender");

        Transaction receiverTransaction = new Transaction(Transpay.accountSystem.getAccount(targetAccount), amount, "Transfer", date);
        receiverTransaction.setTarget(Transpay.account.getAccountNumber());
        receiverTransaction.setTransferType("Receiver");

        double originalBalance = Transpay.account.getBalance();

        Transpay.account.transfer(Transpay.accountSystem.getAccount(targetAccount), amount);
        Transpay.bankSystem.addTransaction(transaction);
        Transpay.bankSystem.addTransaction(receiverTransaction);
        Transpay.totalTransfers += amount;

        transaction.setAccountBalance(originalBalance - amount);
        
        new TypeWriter(Log.SUCCESS, "\nTransfer successful!\n", true);

        ConsoleLog.delay(1000);

        if (Receipt.isReceipt()) {
            new Receipt(originalBalance, amount, transaction);
        }
    }

    public static void redirectToTransfer() {
        new FlashWriter(Log.INFO, "Redirecting to Transfer...", false);
        ConsoleLog.clear(1000);
        new Transfer();
    }
}