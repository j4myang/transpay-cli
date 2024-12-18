package transpay.cli;

import java.util.Scanner;
import java.util.Random;
import transpay.account.Account;
import transpay.account.AccountSystem;
import transpay.bank.BankSystem;
import transpay.cli.components.ConsoleLog;
import transpay.cli.panel.Start;

public class Transpay {
    public static String brand = "Transpay";
    public static AccountSystem accountSystem = new AccountSystem();
    public static BankSystem bankSystem = new BankSystem();
    public static final Scanner scan = new Scanner(System.in);
    public static Account account;
    public static boolean exit = false;
    public static final String pinPattern = "\\d{6}";
    public static final String adminPattern = "\\d{10}";

    public static final double depositLimit = 100000.00;
    public static final double depositMultiple = 100.00;
    public static final double withdrawalLimit = 100000.00;
    public static final double withdrawalMultiple = 100.00;
    public static final double transferLimit = 500000.00;
    public static double totalDeposits = 0.00;
    public static double totalWithdrawals = 0.00;
    public static double totalTransfers = 0.00;
    public static int ragdollCount = 0;

    public static String status = "";   
    public static String[] statuses = {"Online", "Offline", "Under Maintenance"};
    
    public static String[] ragdolls;
    
    public Transpay() {
        while (!exit) {
            status = statuses[new Random().nextInt(statuses.length)];
            new Start();
        }
        
        scan.close();
        ConsoleLog.logInfo("\nThanks for using " + brand + "! Goodbye.");
        System.exit(0);
    }

    public static double getTotalCirculatedFunds() {
        return totalDeposits + totalWithdrawals + totalTransfers;
    }
}
