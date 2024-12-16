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

public class Balance {
    public Balance() {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Transaction transaction = new Transaction(Transpay.account, 0, "Balance Inquiry", date);

        new FlashWriter(Log.BODY, "Your account balance is: ", false);
        new FlashWriter(Log.BODY, "PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", Transpay.account.getBalance()), true);

        new FlashWriter(Log.BODY, "Current Date: ", false);
        new FlashWriter(Log.HEADING, date, true);

        ConsoleLog.print("\n");

        if (Transpay.status.equals("Under Maintenance")) {
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

        handleAction();
    }

    public void handleAction() {
        new TypeWriter(Log.INPUT, "\nWhat would you like to do?", true);

        while (true) {
            int action = Integer.parseInt(Dashboard.getValidatedInput(
                "", 
                test -> {
                    try {
                        if (!test.matches("\\d+")) {
                            throw new NumberFormatException();
                        }
    
                        int temp = Integer.parseInt(test);
                        
                        return temp >= 1 && temp <= 4;
                    } catch (NumberFormatException e) {
                        return false;
                }}, 
                "Invalid choice. Please try again.",
                false 
                ));

            if ((action >= 1 && action <= 3) && Transpay.status.equals("Under Maintenance")) {
                new FlashWriter(Log.ERROR, "The system is currently Under Maintenance. Please try again later.", true);
                continue;
            }
        
            switch (action) {
                case 1:
                    Withdraw.redirectToWithdraw();
                    break;
                case 2:
                    Deposit.redirectToDeposit();
                    break;
                case 3:
                    Transfer.redirectToTransfer();
                    break;
                case 4:
                    new FlashWriter(Log.INFO, "Returning to Dashboard...", false);
                    ConsoleLog.clear(1000);
                    new Dashboard();
                    break;
            }
        }
    }
}
