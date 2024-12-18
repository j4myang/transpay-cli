package transpay.cli.panel;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;

import transpay.bank.Transaction;
import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class AccountDetail {
    public AccountDetail() {

        displayAccountDetailPage();
        handleAction();
    }

    private void displayAccountDetailPage() {
        new FlashWriter(Log.HEADING, "\t\t    Your Account\n", true);

        new TypeWriter(Log.BODY, "Account Number: ", false);
        new FlashWriter(Log.HEADING, Transpay.account.getAccountNumber(), true);

        new TypeWriter(Log.BODY, "Account Name: ", false);
        new FlashWriter(Log.HEADING, Transpay.account.getName(), true);

        new TypeWriter(Log.BODY, "Balance: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", Transpay.account.getBalance()), true);

        new TypeWriter(Log.BODY, "PIN: ", false);
        new FlashWriter(Log.HEADING, "*".repeat(Transpay.account.getPIN().length()), true);

        new TypeWriter(Log.BODY, "Created at: ", false);
        new FlashWriter(Log.HEADING, Transpay.account.getDateRegistered(), true);

        new TypeWriter(Log.SYSTEM, "\n\nOptions", true);  

        new TypeWriter(Log.OPTION, "1. ", false);
        new FlashWriter(Log.BODY, "View transaction history", true);
        
        new TypeWriter(Log.OPTION, "2. ", false);
        new FlashWriter(Log.BODY, "View Statement of Account", true);

        new TypeWriter(Log.OPTION, "3. ", false);
        new FlashWriter(Log.BODY, "Show PIN", true);

        new TypeWriter(Log.OPTION, "4. ", false);
        new FlashWriter(Log.BODY, "Go Back", true);

    }

    private void handleAction() {
        new TypeWriter(Log.INPUT, "\nWhat would you like to do?", true);

        while (true) {
            int action = Integer.parseInt(Dashboard.getValidatedInput("", test -> {
                try {
                    if (!test.matches("\\d+")) {
                        throw new NumberFormatException();
                    }

                    int temp = Integer.parseInt(test);
                    
                    return temp >= 1 && temp <= 4;
                } catch (NumberFormatException e) {
                    return false;
            }}, "Invalid choice. Please try again.", false));

            switch (action) {
                case 1:
                    ConsoleLog.clear(0);
                    viewTransactionHistory();
                    break;
                case 2:
                    ConsoleLog.clear(0);
                    viewStatementOfAccount();
                    break;
                case 3:
                    ConsoleLog.clear(0);
                    showPIN();
                    break;
                case 4:
                    new FlashWriter(Log.INFO, "Returning to Dashboard...", false);
                    ConsoleLog.clear(1000);
                    new Dashboard();
                    break;
            }
        }
    }

    private void viewTransactionHistory() {
        while (true) {
            new FlashWriter(Log.HEADING, "\t\t    Your Transaction History\n", true);
    
            Transaction[] transactions = Transpay.bankSystem.getTransactionsByAccountByDateDESC(Transpay.account.getAccountNumber());
    
            if (transactions.length == 0) {
                new FlashWriter(Log.INFO, "No transactions found.", true);
            }
            else {
                for (Transaction transaction : transactions) {
                    new TypeWriter(Log.BODY, "Transaction Date: ", false);
                    new FlashWriter(Log.HEADING, transaction.getDate(), true);
    
                    new TypeWriter(Log.BODY, "Transaction Type: ", false);
                    new FlashWriter(Log.HEADING, transaction.getType(), true);
    
                    if (transaction.getType().equals("Transfer")) {
                        if (transaction.getTransferType().equals("Receiver")) {
                            new TypeWriter(Log.BODY, "Sender Account: ", false);
                            new FlashWriter(
                                Log.HEADING, 
                                transaction.getTarget().replace(
                                    transaction.getTarget().substring(
                                        1, 
                                        transaction.getTarget().length() - 3),
                                    "*".repeat(transaction.getTarget().length() - 3)),
                             true);

                             new TypeWriter(Log.SUCCESS, "\t+", false);
                        }
                        else {
                            new TypeWriter(Log.BODY, "Receiver Account: ", false);
                            new FlashWriter(
                                Log.HEADING, 
                                transaction.getTarget().replace(
                                    transaction.getTarget().substring(
                                        1, 
                                        transaction.getTarget().length() - 3),
                                    "*".repeat(transaction.getTarget().length() - 3)),
                         true);

                            new TypeWriter(Log.ERROR, "\t-", false);
                        }
                    }

                    if (transaction.getType().equals("Withdrawal")) {
                        new TypeWriter(Log.ERROR, "\t-", false);
                    }
                    else if (transaction.getType().equals("Deposit")) {
                        new TypeWriter(Log.SUCCESS, "\t+", false);
                    }

                    new TypeWriter(Log.BODY, "  PHP ", false);
                    new FlashWriter(Log.HEADING, String.format("%,.2f", transaction.getAmount()), true);
                
                    ConsoleLog.print("\n\n");
                }
            }
            
            returnToAccountDetail();
        }
    }

    private void showPIN() {
        new FlashWriter(Log.HEADING, "\t\t    " + Transpay.account.getName(), true);

        ConsoleLog.print("\n");
        
        new TypeWriter(Log.BODY, "Your PIN is: ", false);
        new FlashWriter(Log.HEADING, Transpay.account.getPIN(), true);

        returnToAccountDetail();
    }

    private void viewStatementOfAccount() {
        new FlashWriter(Log.HEADING, "\t\t    Set Statement of Account Date\n", true);

        new TypeWriter(Log.BODY, "    If going here was a mistake, use 'exit' command\n", true);

        new FlashWriter(Log.INPUT, "Enter the year range (YYYY):", true);
        String year = getYear();

        new FlashWriter(Log.INPUT, "Enter the start month (MM):", true);
        String startMonth = getMonth();

        new FlashWriter(Log.INPUT, "Enter the end month (MM):", true);
        String endMonth = getMonth();

        Transaction[] transactions = Transpay.bankSystem.getAccountTransactionByDateRange(
            Transpay.account.getAccountNumber(),
            year,
            startMonth,
            endMonth
        );

        if (transactions.length == 0) {
            new FlashWriter(Log.INFO, "\nNo transactions found.\n", true);
            returnToAccountDetail();
            return;
        }

        double startingBalance = (transactions[0].getType().equals("Transfer") && 
            transactions[0].getTransferType().equals("Receiver")) || 
            transactions[0].getType().equals("Deposit") ? 
            transactions[0].getAccountBalance() - transactions[0].getAmount() : 
            transactions[0].getAccountBalance() + transactions[0].getAmount();

        double paidOutBalance = 0;
        double paidInBalance = 0;
        
        for (Transaction transaction : transactions) {
            if (transaction.getType().equals("Deposit")) {
                paidInBalance += transaction.getAmount();
            }
            else {
                if (transaction.getType().equals("Transfer") && transaction.getTransferType().equals("Sender")) {
                    paidOutBalance += transaction.getAmount();
                    continue;
                }

                paidInBalance += transaction.getAmount();
            }
        }

        String brand = " _______  ______    _______  __    _  _______  _______  _______  __   __ \r\n" +
        "|       ||    _ |  |   _   ||  |  | ||       ||       ||   _   ||  | |  |  Statement\r\n" +
        "|_     _||   | ||  |  |_|  ||   |_| ||  _____||    _  ||  |_|  ||  |_|  |    of\r\n" +
        "  |   |  |   |_||_ |       ||       || |_____ |   |_| ||       ||       |  Account\r\n" +
        "  |   |  |    __  ||       ||  _    ||_____  ||    ___||       ||_     _|\r\n" +
        "  |   |  |   |  | ||   _   || | |   | _____| ||   |    |   _   |  |   |  \r\n" +
        "  |___|  |___|  |_||__| |__||_|  |__||_______||___|    |__| |__|  |___|  ";

        ConsoleLog.clear(0);

        new FlashWriter(Log.HEADING, brand, true);

        new FlashWriter(Log.SYSTEM, "\nAccount Number: ", false);
        new FlashWriter(Log.HEADING, Transpay.account.getAccountNumber(), true);

        new FlashWriter(Log.SYSTEM, "Account Name: ", false);
        new FlashWriter(Log.HEADING, Transpay.account.getName(), true);

        new FlashWriter(Log.SYSTEM, "\nPeriod: ", false);
        new FlashWriter(
            Log.HEADING, 
            "01 " +  
            Month.of(Integer.parseInt(startMonth)).getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH) + " " +
            year + 
            " to 01 " + 
            Month.of(Integer.parseInt(endMonth)).getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH) + " " +  
            ((Integer.parseInt(endMonth) < 12 || Integer.parseInt(startMonth) == Integer.parseInt(endMonth)) ? String.valueOf(Integer.parseInt(year) + 1) : year), 
            true);

        new FlashWriter(Log.SYSTEM, "\n\t Starting Balance: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", startingBalance), true);

        new FlashWriter(Log.SYSTEM, "\t Paid In: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", paidInBalance), true);

        new FlashWriter(Log.SYSTEM, "\t Paid Out: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", paidOutBalance), true);

        new FlashWriter(Log.SYSTEM, "\t Ending Balance: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", startingBalance + paidInBalance - paidOutBalance), true);

        new FlashWriter(Log.INFO, String.format(
    "\n%-15s %-12s %-30s %15s %20s\n", 
    "Date", "Type", "Description", "Cash Flow", "Real-time Balance"), true);

        new FlashWriter(Log.INFO, String.format(
    "%-15s %-12s %-30s %15s %20s", 
            "-".repeat(15), "-".repeat(12), "-".repeat(30), "-".repeat(15), "-".repeat(20)
        ), true);

        for (Transaction transaction : transactions) {
            String[] date = transaction.getDate().split("-");
            String formattedDate = String.format("%s %s %s", date[2].split(" ")[0], 
                Month.of(Integer.parseInt(date[1])).getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH), 
                date[0]);

            String description;
            if (transaction.getTarget() != null) {
                String maskedTarget = transaction.getTarget().replace(
                    transaction.getTarget().substring(1, transaction.getTarget().length() - 3),
                    "*".repeat(transaction.getTarget().length() - 3));

                if (transaction.getTransferType().equals("Receiver")) {
                    description = "Received from " + maskedTarget;
                } else {
                    description = "Sent to " + maskedTarget;
                }
            } else {
                description = "-";
            }

            if (description.length() > 30) {
                description = description.substring(0, 27) + "...";
            }

            String cashFlow = transaction.getType().equals("Deposit") || transaction.getType().equals("Transfer") && transaction.getTransferType().equals("Receiver") ? 
                String.format(ConsoleLog.colorSuccess + "+ " + ConsoleLog.RESET_CODE + "PHP %,.2f", transaction.getAmount()) : 
                String.format(ConsoleLog.colorError + "- " + ConsoleLog.RESET_CODE +"PHP %,.2f", transaction.getAmount());

            String balance = String.format("PHP %,.2f", transaction.getAccountBalance());

            new FlashWriter(Log.BODY, String.format(
                "%-15s %-12s %-30s %15s %20s", 
                formattedDate, 
                transaction.getType(), 
                description, 
                cashFlow, 
                balance
            ), true);
        }

        returnToAccountDetail();
    }
    
    private String getYear() {
        while (true) {
            String year = Dashboard.getValidatedInput("", test1 -> {
                try {
                    int temp = Integer.parseInt(test1);
                    return temp >= Integer.parseInt(Transpay.account.getDateRegistered().split("-")[0]) && temp <= Calendar.getInstance().get(Calendar.YEAR);
                } catch (NumberFormatException e) {
                    return false;
                }
            }, test2 -> {
                return test2.matches("\\d{4}");
            },
             "Invalid year range. Please try again.",
             "Follow the year format (YYYY). Please try again.", false);
    
            if (year != null) {
                return year;
            }
        }
    }

    private String getMonth() {
        while (true) {
            String month = Dashboard.getValidatedInput("", test1 -> {
                try {
                    int temp = Integer.parseInt(test1);
                    return temp >= 1 && temp <= 12;
                } catch (NumberFormatException e) {
                    return false;
                }
            },
            test2 -> {
                return test2.matches("\\d{2}");
            }, "Invalid month range. Please try again.", 
            "Follow the month format (MM). Please try again.", false);
    
            if (month != null) {
                return month;
            }
        }
    }

    private void returnToAccountDetail() {
        new FlashWriter(Log.INPUT, "\n\nPress enter to go back:", true);
        new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
        ConsoleLog.getInput();

        new FlashWriter(Log.INFO, "Returning to Account Detail...", false);
        ConsoleLog.clear(1000);

        new AccountDetail();
        return;
    }
}
