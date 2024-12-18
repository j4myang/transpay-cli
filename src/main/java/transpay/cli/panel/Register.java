package transpay.cli.panel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import transpay.account.Account;
import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class Register {
    public Register() {
        new FlashWriter(Log.HEADING, "\t\t    Register an Account\n", true);

        new TypeWriter(Log.SYSTEM, "    If going here was a mistake, use 'exit' command\n", true);

        new TypeWriter(Log.INPUT, "Enter your name:", true);
        String username = getUserName();
        String pin = getUserPIN();
        getConfirmUserPIN(pin);
        double balance = getStartingBalance();

        new FlashWriter(Log.INFO, "\nPlease wait a moment while we prepare your account...\n", true);
        
        ConsoleLog.delay(1000);

        String accountNumber = registerAccount(username, pin, balance);

        new TypeWriter(Log.SUCCESS, "Account registration successful!\n", true);

        ConsoleLog.clear(1000);

        displaySummary(accountNumber, username, pin, balance);

        Start.returnToStart();
    }


    private String getUserName() {
        while (true) {
            String input = Start.getValidatedInput(
                "", 
                test -> {
                    return !test.isBlank();
                },
                "Name cannot be empty. Please try again.",
                false);
        
            if (input != null) {
                return input;
            }
        }
    }

    private String getUserPIN() {
        new TypeWriter(Log.INPUT, "\nEnter your 6-digit PIN (hidden for security):", true);

        while (true) {
            String input = Start.getValidatedInput(
                "", 
                test -> {
                    return test.matches(Transpay.pinPattern);
                }, "PIN must be numeric and 6 digits long. Please try again.", true);
           
            if (input != null) {
                return input;
           }
        }
    }

    private void getConfirmUserPIN(String pin) {
        new TypeWriter(Log.INPUT, "\nConfirm your 6-digit PIN (hidden for security):", true);

        while (true) {
            String input = Start.getValidatedInput(
                "", 
                test1 -> {
                    return test1.matches(Transpay.pinPattern);
                }, 
                test2 -> {
                    return test2.equals(pin);
                }, 
                "PIN must be numeric and 6 digits long. Please try again.", 
                "PINs do not match. Please try again.", 
                true);

            if (input != null) {
                break;
            }
        }
    }

    private double getStartingBalance() {
        new TypeWriter(Log.INPUT, "\nEnter your starting balance (balance limit of 1 million):", true);

        while (true) {
            String input = Start.getValidatedInput(
                "PHP ", 
                test1 -> {
                    return Double.parseDouble(test1) > 0;
                }, 
                test2 -> {
                    return Double.parseDouble(test2) <= 1000000.00;
                }, 
                "Amount must be greater than PHP 0.00. Please try again.",
                "Amount exceeded the limit. Please try again.",
                false
                );
            
            if (input != null) {
                return Double.parseDouble(input);
            }
        }
    } 

    private void displaySummary(String accountNumber, String username, String pin, double balance) {
        new TypeWriter(Log.HEADING, "\t\t    Welcome aboard, " + username + "!\n", true);

        new TypeWriter(Log.BODY, "Account Number: ", false);
        new FlashWriter(Log.HEADING, accountNumber, true);

        new TypeWriter(Log.BODY, "Account Name: ", false);
        new FlashWriter(Log.HEADING, username, true);

        new TypeWriter(Log.BODY, "PIN: ", false);
        new FlashWriter(Log.HEADING, "*".repeat(pin.length()), true);

        new TypeWriter(Log.BODY, "Balance: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", balance), true);

        new TypeWriter(Log.BODY, "Created at: ", false);
        new FlashWriter(Log.HEADING, Transpay.accountSystem.getAccount(accountNumber).getDateRegistered(), true);
    }

    private String registerAccount(String name, String pin, double balance) {
        String accountNumber = "";
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        
        do {
            accountNumber = UUID.randomUUID().toString().split("-")[0];
        }
        while (Transpay.accountSystem.getAccount(accountNumber) != null);

       Account regAccount = new Account(accountNumber, pin, balance, name);
       regAccount.setDateRegistered(currentDate);

       Transpay.accountSystem.addAccount(regAccount);
       return accountNumber;
    }
}
