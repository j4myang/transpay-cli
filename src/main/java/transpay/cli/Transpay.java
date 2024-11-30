package transpay.cli;

import java.util.Scanner;

import transpay.account.Account;
import transpay.account.AccountSystem;
import transpay.bank.BankSystem;
import transpay.cli.components.ConsoleLog;
import transpay.cli.pages.Welcome;

public class Transpay {
    public static AccountSystem accountSystem = new AccountSystem();
    public static BankSystem bankSystem = new BankSystem();
    public static final Scanner scan = new Scanner(System.in);
    public static Account account;
    public static boolean exit = false;

    public Transpay() {
        while (!exit) {
            new Welcome();
        }
        
        scan.close();
        ConsoleLog.logInfo("\nThanks for using Transpay! Goodbye.");
        System.exit(0);
    }
}
