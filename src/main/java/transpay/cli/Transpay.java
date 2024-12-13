package transpay.cli;

import java.util.Scanner;
import java.util.Random;
import transpay.account.Account;
import transpay.account.AccountSystem;
import transpay.bank.BankSystem;
import transpay.cli.components.ConsoleLog;
import transpay.cli.panel.Welcome;

public class Transpay {
    public static AccountSystem accountSystem = new AccountSystem();
    public static BankSystem bankSystem = new BankSystem();
    public static final Scanner scan = new Scanner(System.in);
    public static Account account;
    public static boolean exit = false;
    public static double totalDeposits = 0.00;
    public static double totalWithdrawals = 0.00;
    public static double totalTransfers = 0.00;
    public static String status = "";

    public Transpay() {
        String[] statuses = {"Online", "Offline", "Maintenance"};
        status = statuses[new Random().nextInt(statuses.length)];

        while (!exit) {
            new Welcome();
        }
        
        scan.close();
        ConsoleLog.logInfo("\nThanks for using Transpay! Goodbye.");
        System.exit(0);
    }

    public static double getTotalCirculatedFunds() {
        return totalDeposits + totalWithdrawals + totalTransfers;
    }
}
