package transpay.cli.panel;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

import transpay.account.Account;
import transpay.bank.Transaction;
import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class Admin {
    String id = "";

    public Admin() {
        new FlashWriter(Log.HEADING, "\t\t  Log in as Administrator\n", true);
        new TypeWriter(Log.BODY, "    If going here was a mistake, use 'exit' command\n", true);

        new TypeWriter(Log.INPUT, "Enter your ID Number: ", true);
        id = getID();
        
        new TypeWriter(Log.SUCCESS, "\nLogin successful! Please wait a moment...", true);

        ConsoleLog.clear(1000);

        displayAdminPage();
    }

    private String getID() {
        while (true) {
            String input = Start.getValidatedInput(
                "", 
                test -> {
                    return test.matches("\\d{10}");
                },
                 "Invalid ID. Please try again.",
                  false);

            if (input != null) {
                return input;
            }
        }
    }

    private void displayAdminPage() {
        new FlashWriter(Log.HEADING, "\t\t    Good " + Dashboard.computeDayMessage() + ", " + id + "!\n", true);

        new TypeWriter(Log.SYSTEM, "Current Status: ", false);
        new FlashWriter(Log.HEADING, Transpay.status + "\n", true);

        new TypeWriter(Log.SYSTEM, "\nActions\n", true);

        new TypeWriter(Log.OPTION, "1. ", false);
        new FlashWriter(Log.BODY, "Fix", true);

        new TypeWriter(Log.OPTION, "2. ", false);
        new FlashWriter(Log.BODY, "Show ATM Report", true);

        new TypeWriter(Log.OPTION, "3. ", false);
        new FlashWriter(Log.BODY, "Generate Ragdoll Account", true);

        new TypeWriter(Log.OPTION, "4. ", false);
        new FlashWriter(Log.BODY, "Purge Ragdoll Accounts", true);

        new TypeWriter(Log.OPTION, "5. ", false);
        new FlashWriter(Log.BODY, "Go Back", true);

        handleAction();
    }

    private void handleAction() {
        new TypeWriter(Log.INPUT, "\nWhat would you like to do?", true);

        while (true) {
            String action = Start.getValidatedInput(
                "", 
                test -> {
                    return Double.parseDouble(test) >= 1 && Double.parseDouble(test) <= 5;
                },
                 "Invalid input. Please try again.",
                  false
            );

            switch (action) {
                case "1":
                    if (Transpay.status.equals("Online")) {
                        new FlashWriter(Log.ERROR, "The system is already online.", true);
                        break;
                    }
                    
                    new FlashWriter(Log.INFO, "\nFixing the system...", true);

                    ConsoleLog.delay(1000);

                    Transpay.status = "Online";
                    
                    new TypeWriter(Log.SUCCESS, "\nThe system is now online.", true);
                    break;

                case "2":
                    ConsoleLog.clear(0);
                    displayAtmReport();
                    break;

                case "3":
                    createRagdollAccount();
                    break;
                
                case "4":
                    purgeRagdollAccounts();
                    break;

                case "5":
                    new FlashWriter(Log.INFO, "Returning to Start page...", false);
                    ConsoleLog.clear(1000);
                    new Start();
                    break;
            }
        }
    }

    private void displayAtmReport() {
        new TypeWriter(Log.SYSTEM, "\t\t    ATM Stats\n", true);

        new TypeWriter(Log.SYSTEM, "Total Accounts: ", false);
        new FlashWriter(Log.HEADING, String.format("%,d\n", Transpay.accountSystem.length()), true);

        new TypeWriter(Log.SYSTEM, "Total Transactions: ", false);
        new FlashWriter(Log.HEADING, String.format("%,d\n", Transpay.bankSystem.length()), true);

        new TypeWriter(Log.SYSTEM, "Total Circulated Funds: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f\n", Transpay.getTotalCirculatedFunds()), true);

        new TypeWriter(Log.SYSTEM, "\tTotal Withdrawals: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", Transpay.totalWithdrawals), true);

        new TypeWriter(Log.SYSTEM, "\tTotal Deposits: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", Transpay.totalDeposits), true);

        new TypeWriter(Log.SYSTEM, "\tTotal Transfers: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", Transpay.totalTransfers), true);

        returnToAdmin();
    }

    private void createRagdollAccount() {
        new FlashWriter(Log.INFO, "\nGenerating Ragdoll Account...", true);

        String accountPIN = String.valueOf((int) (Math.random() * 900000) + 100000);
        double accountBalance = (double) (Math.random() * 10000) + 10000;
        String accountName = "Ragdoll No. " + ++Transpay.ragdollCount;
        String accountNumber = "";
        
        do {
            accountNumber = UUID.randomUUID().toString().split("-")[0];
        }
        while (Transpay.accountSystem.getAccount(accountNumber) != null);

        if (Transpay.ragdolls == null) {
            Transpay.ragdolls = new String[0];
        }

        String[] newRagdolls = new String[Transpay.ragdolls.length + 1];

        for (int i = 0; i < Transpay.ragdolls.length; i++) {
            newRagdolls[i] = Transpay.ragdolls[i];
        }
        newRagdolls[newRagdolls.length - 1] = accountNumber;
        Transpay.ragdolls = newRagdolls;

        Account regAccount = new Account(accountNumber, accountPIN, accountBalance, accountName);

        regAccount.setDateRegistered(generateRandomDate(2000, Year.now().getValue()));

        Transpay.accountSystem.addAccount(regAccount);
       
        for (int i = 0; i < new Random().nextInt(10, 51); i++) {
            generateRagdollRandomTransactions(regAccount);
        }

        ConsoleLog.delay(1000);
 
        new TypeWriter(Log.SUCCESS, "\nRagdoll account created successfully.", true);

        ConsoleLog.clear(1000);

        new TypeWriter(Log.HEADING, "\t\t    Welcome aboard, " + regAccount.getName() + "!\n", true);

        new TypeWriter(Log.BODY, "Account Number: ", false);
        new FlashWriter(Log.HEADING, regAccount.getAccountNumber(), true);

        new TypeWriter(Log.BODY, "Account Name: ", false);
        new FlashWriter(Log.HEADING, regAccount.getName(), true);

        new TypeWriter(Log.BODY, "PIN: ", false);
        new FlashWriter(Log.HEADING, regAccount.getPIN(), true);

        new TypeWriter(Log.BODY, "Balance: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", accountBalance), true);

        new TypeWriter(Log.BODY, "Created at: ", false);
        new FlashWriter(Log.HEADING, regAccount.getDateRegistered(), true);

        new TypeWriter(Log.BODY, "Transaction Count: ", false);
        new FlashWriter(Log.HEADING, String.format("%,d", Transpay.bankSystem.getTransactionsByAccount(accountNumber).length), true);
        returnToAdmin();
    }

    private void generateRagdollRandomTransactions(Account ragdollAccount) {
        String randomTransactionType = Math.random() < 0.5 ? "Deposit" : "Withdrawal";
        
        int minimum = (100 + 99) / 100 * 100;
        int maximum = (int) ragdollAccount.getBalance() / 100 * 100;

        double randomAmount = new Random().nextInt((maximum - minimum) / 100 + 1) * 100;
        String randomDate = generateRandomDate(Integer.parseInt(ragdollAccount.getDateRegistered().split("-")[0]), Year.now().getValue());

        if (randomTransactionType.equals("Deposit")) {
            double originalBalance = ragdollAccount.getBalance();
            Transaction transaction = new Transaction(ragdollAccount, randomAmount, "Deposit", randomDate);
            Transpay.bankSystem.addTransaction(transaction);

            ragdollAccount.deposit(randomAmount);
            transaction.setAccountBalance(originalBalance + randomAmount);
        }
        else {
            double originalBalance = ragdollAccount.getBalance();
            Transaction transaction = new Transaction(ragdollAccount, randomAmount, "Withdrawal", randomDate);

            ragdollAccount.withdraw(randomAmount);
            Transpay.bankSystem.addTransaction(transaction);

            transaction.setAccountBalance(originalBalance - randomAmount);
        }
    }

    private String generateRandomDate(int startYear, int endYear) {
        LocalDate startDate = LocalDate.of(startYear, 1, 1);
        LocalDate endDate = LocalDate.of(endYear, 12, 31);

        int deviation = new Random().nextInt(30 * 2 + 1) - 30;

        long dayInRange = startDate.toEpochDay() + new Random().nextInt((int) (endDate.toEpochDay() - startDate.toEpochDay() + 1));

        LocalDate randomLocalDate = LocalDate.ofEpochDay(dayInRange + deviation);

        String randomDate = randomLocalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return randomDate;
    }

    private void purgeRagdollAccounts() {
        if (Transpay.ragdolls == null) {
            new FlashWriter(Log.INFO, "\nThere are no ragdoll accounts to purge.", true);
            return;
        }

        new FlashWriter(Log.INFO, "\nPurging Ragdoll Accounts...", true);

        for (String num: Transpay.ragdolls) {
            Transpay.accountSystem.deleteAccount(num);
        }

        int length = Transpay.ragdolls.length;

        Transpay.ragdolls = null;

        ConsoleLog.delay(1000);

        new TypeWriter(Log.SUCCESS, "\nPurged " + length + " Ragdoll Accounts", true);
    }

    private void returnToAdmin() {
        new TypeWriter(Log.INPUT, "\nPress enter to go back:", true);
        new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
        ConsoleLog.getInput();

        new FlashWriter(Log.INFO, "Returning to Admin...", false);
        ConsoleLog.clear(1000);
        displayAdminPage();
    }
}
