package transpay.cli.components;

import transpay.bank.Transaction;
import transpay.cli.Transpay;

public class Receipt {
    public Receipt(double originalBalance, double amount, Transaction transaction) {
        new FlashWriter(Log.SYSTEM, "\n\t\t\t  " + Transpay.brand, true);
        new FlashWriter(Log.SYSTEM, "\t       Your trustworthy banking system\n", true);

        new FlashWriter(Log.BODY, "\t  Transaction Type: ", false);
        new FlashWriter(Log.HEADING, transaction.getType(), true);

        new FlashWriter(Log.BODY, "\t  Date: ", false);
        new FlashWriter(Log.HEADING, transaction.getDate(), true);

        new FlashWriter(Log.BODY, "\t  Account Number: ", false);
        new FlashWriter(Log.HEADING, Transpay.account.getAccountNumber() + "\n", true);

        switch (transaction.getType()) {
            case "Deposit":
                new FlashWriter(Log.BODY, "\t  Original Balance: PHP ", false);
                new FlashWriter(Log.HEADING, String.format("%,.2f", originalBalance), true);

                new FlashWriter(Log.BODY, "\t  Deposit Amount: PHP ", false);
                new FlashWriter(Log.HEADING, String.format("%,.2f", amount), true);

                new FlashWriter(Log.BODY, "\t  New Balance: PHP ", false);
                new FlashWriter(Log.HEADING, String.format("%,.2f", Transpay.account.getBalance()), true);
                break;
            case "Withdrawal":
                new FlashWriter(Log.BODY, "\t  Original Balance: PHP ", false);
                new FlashWriter(Log.HEADING, String.format("%,.2f", originalBalance), true);

                new FlashWriter(Log.BODY, "\t  Withdraw Amount: PHP ", false);
                new FlashWriter(Log.HEADING, String.format("%,.2f", amount), true);

                new FlashWriter(Log.BODY, "\t  New Balance: PHP ", false);
                new FlashWriter(Log.HEADING, String.format("%,.2f", Transpay.account.getBalance()), true);
                break;
            case "Transfer":
                new FlashWriter(Log.BODY, "\t  Receiver Account: ", false);
                new FlashWriter(Log.HEADING, transaction.getTarget().replace(
                    transaction.getTarget().substring(1, transaction.getTarget().length() - 3),
                    "*".repeat(transaction.getTarget().length() - 3)), true);

                new FlashWriter(Log.BODY, "\t  Original Balance: PHP ", false);
                new FlashWriter(Log.HEADING, String.format("%,.2f", originalBalance), true);

                new FlashWriter(Log.BODY, "\t  Transer Amount: PHP ", false);
                new FlashWriter(Log.HEADING, String.format("%,.2f", amount), true);

                new FlashWriter(Log.BODY, "\t  New Balance: PHP ", false);
                new FlashWriter(Log.HEADING, String.format("%,.2f", Transpay.account.getBalance()), true);
                break;
            case "Balance Inquiry":
                new FlashWriter(Log.BODY, "\t  Current Balance: PHP ", false);
                new FlashWriter(Log.HEADING, String.format("%,.2f", Transpay.account.getBalance()), true);
                break;
        }
        new FlashWriter(Log.SYSTEM, "\n\t      Thank you for using our service!", true);
        new FlashWriter(Log.SYSTEM, "\t        " + Transpay.brand + "@official.receipt", true);
    }

    public static boolean isReceipt() {
        new TypeWriter(Log.INPUT, "Do you need a receipt? (y/n)", true);
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                String input = ConsoleLog.getInput();

                if (!input.equals("y") && !input.equals("n")) {
                    throw new IllegalArgumentException();
                }

                if (input.equals("y")) {
                    return true;
                }
                return false;
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }
}
