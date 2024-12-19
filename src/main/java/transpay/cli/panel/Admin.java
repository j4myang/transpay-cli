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
                    return;

                case "3":
                    createRagdollAccount();
                    return;
                
                case "4":
                    purgeRagdollAccounts();
                    break;

                case "5":
                    new FlashWriter(Log.INFO, "Returning to Start page...", false);
                    ConsoleLog.clear(1000);
                    new Start();
                    return;
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
        double accountBalance = new Random().nextDouble(150.00, 99999.99);
        String accountName = "Ragdoll No. " + ++Transpay.ragdollCount;
        String accountNumber = "";
        
        do {
            accountNumber = "Ragdoll-" + UUID.randomUUID().toString().split("-")[0];
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
       
        for (int i = 0; i < new Random().nextInt(50, 101); i++) {
            generateRagdollRandomTransactions(regAccount);
        }

        ConsoleLog.delay(1000);
 
        new TypeWriter(Log.SUCCESS, "\nRagdoll account created successfully.", true);

        ConsoleLog.clear(1000);

        new TypeWriter(Log.HEADING, "\t\t    Welcome aboard, " + regAccount.getName() + "!\n", true);

        new TypeWriter(Log.SYSTEM, "Account Number: ", false);
        new FlashWriter(Log.HEADING, regAccount.getAccountNumber(), true);

        new TypeWriter(Log.SYSTEM, "Account Name: ", false);
        new FlashWriter(Log.HEADING, regAccount.getName(), true);

        new TypeWriter(Log.SYSTEM, "PIN: ", false);
        new FlashWriter(Log.HEADING, regAccount.getPIN(), true);

        new TypeWriter(Log.SYSTEM, "Starting Balance: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", accountBalance), true);

        new TypeWriter(Log.SYSTEM, "Ending Balance: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", regAccount.getBalance()), true);

        new TypeWriter(Log.SYSTEM, "Created at: ", false);
        new FlashWriter(Log.HEADING, regAccount.getDateRegistered(), true);

        new TypeWriter(Log.SYSTEM, "Transaction Count: ", false);
        new FlashWriter(Log.HEADING, String.format("%,d", Transpay.bankSystem.getTransactionsByAccount(accountNumber).length), true);
        returnToAdmin();
    }

    private void generateRagdollRandomTransactions(Account ragdollAccount) {
        String[] transactionTypes = {"Deposit", "Withdrawal", "Transfer"};

        String randomTransactionType = Transpay.ragdolls.length > 1 ? 
        transactionTypes[new Random().nextInt(transactionTypes.length)] : 
        transactionTypes[new Random().nextInt(transactionTypes.length - 1)];

        int minimum = 100;
        int maximum = (int) ragdollAccount.getBalance() / 100 * 100;
        
        if (maximum < minimum) {
            maximum = 1000;
        }

        double randomAmount = new Random().nextInt(100, 100000);

        String randomDate = generateRandomDate(Integer.parseInt(ragdollAccount.getDateRegistered().split("-")[0]), Year.now().getValue());

        if (ragdollAccount.getBalance() - randomAmount <= 100) {
            randomTransactionType = "Deposit";
        }

        if (randomTransactionType.equals("Deposit")) {
            double originalBalance = ragdollAccount.getBalance();
            Transaction transaction = new Transaction(ragdollAccount, randomAmount, "Deposit", randomDate);
            Transpay.bankSystem.addTransaction(transaction);

            ragdollAccount.deposit(randomAmount);
            transaction.setAccountBalance(originalBalance + randomAmount);

            Transpay.totalDeposits += randomAmount;
        } else if (randomTransactionType.equals("Withdrawal")) {
            double originalBalance = ragdollAccount.getBalance();
            Transaction transaction = new Transaction(ragdollAccount, randomAmount, "Withdrawal", randomDate);
            Transpay.bankSystem.addTransaction(transaction);
    
            ragdollAccount.withdraw(randomAmount);
            transaction.setAccountBalance(originalBalance - randomAmount);
    
            Transpay.totalWithdrawals += randomAmount;
        } else if (randomTransactionType.equals("Transfer")) {
            String randomTargetAccount = "";

            randomAmount = new Random().nextInt(100, 500000);
            do {
                randomTargetAccount = Transpay.ragdolls[new Random().nextInt(Transpay.ragdolls.length)];
            } while (randomTargetAccount.equals(ragdollAccount.getAccountNumber()));

            double originalBalance = ragdollAccount.getBalance();
            Transaction transaction = new Transaction(ragdollAccount, randomAmount, "Transfer", randomDate);
            transaction.setTarget(randomTargetAccount);
            transaction.setTransferType("Sender");
            transaction.setAccountBalance(originalBalance - randomAmount);
    
            Transaction receiverTransaction = new Transaction(Transpay.accountSystem.getAccount(randomTargetAccount), randomAmount, "Transfer", randomDate);
            receiverTransaction.setTarget(ragdollAccount.getAccountNumber());
            receiverTransaction.setTransferType("Receiver");
            receiverTransaction.setAccountBalance(receiverTransaction.getAccount().getBalance() + randomAmount);
    
            Transpay.totalTransfers += randomAmount;
    
            ragdollAccount.transfer(Transpay.accountSystem.getAccount(randomTargetAccount), randomAmount);
            Transpay.bankSystem.addTransaction(transaction);
            Transpay.bankSystem.addTransaction(receiverTransaction);
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
        return;
    }
}
