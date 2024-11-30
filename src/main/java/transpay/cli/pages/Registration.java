package transpay.cli.pages;

import java.util.Scanner;
import java.util.UUID;

import transpay.account.Account;
import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class Registration {
    private String accountNumber;
    private String name;
    private String PIN;
    private String confirmPIN;
    private double balance;
    private Scanner scan = Transpay.scan;
    private boolean back = false;

    public Registration() {
        new FlashWriter(Log.HEADING, "Register an Account\n", true);

        new TypeWriter(Log.BODY, "If going here is a mistake, use 'exit' command\n", true);

        new TypeWriter(Log.BODY, "Enter your name:", true);
        getUserName();

        new TypeWriter(Log.BODY, "Enter your 6-digit PIN:", true);
        getUserPIN();

        new TypeWriter(Log.BODY, "Confirm your 6-digit PIN:", true);
        getConfirmUserPIN();

        new TypeWriter(Log.BODY, "Enter your starting balance:", true);
        getStartingBalance();

        registerAccount();

        new TypeWriter(Log.INFO, "\n\nPlease wait a moment while we prepare your account...\n\n", true);

        ConsoleLog.delay(1000);

        new TypeWriter(Log.SUCCESS, "Account registration successful!\n\n", true);

        new TypeWriter(Log.INFO, "Welcome aboard, " + name + "!\n", true);

        new TypeWriter(Log.HEADING, "\nAccount Summary\n", true);
        displaySummary();

        new TypeWriter(Log.BODY, "\nPress enter to go back:", true);
        goBack();

        if (back) {
            new TypeWriter(Log.INFO, "\nReturning to Welcome page...", true);

            ConsoleLog.clear(1000);

            new Welcome();
        }
    }


    private void getUserName() {
        while (true) {
            try {
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt, false);
                name = ConsoleLog.getInput(scan);
                
                if (name.equalsIgnoreCase("exit")) {
                    ConsoleLog.clear(0);
                    new Welcome();
                    break;
                }
                else if (name.isBlank()) {
                    new FlashWriter(Log.ERROR, "Name cannot be empty. Please try again.", true);
                    continue;
                }
                break;
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }

    private void getUserPIN() {
        while (true) {  
            try {
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt, false);
                PIN = ConsoleLog.getInput(scan);

                if (PIN.equalsIgnoreCase("exit")) {
                    ConsoleLog.clear(0);
                    new Welcome();
                    break;
                }
                else if (!PIN.trim().matches("\\d{6}")) {
                    throw new NumberFormatException();
                }
                if (PIN.length() != 6) {
                    new FlashWriter(Log.ERROR, "PIN must be 6 digits long. Please try again.", true);
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                new FlashWriter(Log.ERROR, "PIN must be numeric and 6 digits long. Please try again.", true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getConfirmUserPIN() {
        while (true) {  
            try {
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt, false);
                confirmPIN = ConsoleLog.getInput(scan);
                Integer.parseInt(confirmPIN);
    
                if (confirmPIN.length() != 6) {
                    new FlashWriter(Log.ERROR, "PIN must be 6 digits long. Please try again.", true);
                    continue;
                }
    
                else if (!PIN.trim().equals(confirmPIN.trim())) {
                    new FlashWriter(Log.ERROR, "PINs do not match. Please try again.", true);
                    continue;
                }
                break;
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "PIN contained non-numeric characters. Please try again.", true);
            }
        }
    }

    private void getStartingBalance() {
        while (true) {
            try {
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt + "PHP ", false);
                String input = ConsoleLog.getInput(scan);
                balance = Double.valueOf(input);
    
                if (balance > Double.MAX_VALUE) {
                    new FlashWriter(Log.ERROR, "Amount is too large. Please try again.", true);
                    continue;
                }
    
                else if (balance <= 0) {
                    new FlashWriter(Log.ERROR, "Amount must be greater than PHP 0.00. Please try again.", true);
                    continue;
                }
                break;
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    } 

    private void displaySummary() {
        new TypeWriter(Log.BODY, "Account Number: ", false);
        new FlashWriter(Log.INFO, accountNumber, true);

        new TypeWriter(Log.BODY, "Account Name: ", false);
        new FlashWriter(Log.INFO, name, true);

        new TypeWriter(Log.BODY, "PIN: ", false);
        new FlashWriter(Log.INFO, PIN, true);

        new TypeWriter(Log.BODY, "Balance: PHP ", false);
        new FlashWriter(Log.INFO, String.format("%,.2f", balance), true);
    }

    private void goBack() {
        while (true) {
            try {
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt, false);
                ConsoleLog.getInput(scan);
                back = true;
                break;
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }

    private void registerAccount() {
        do {
            accountNumber = UUID.randomUUID().toString().split("-")[0];
        }
        while (Transpay.accountSystem.getAccount(accountNumber) != null);

       Account regAccount = new Account(accountNumber, PIN.trim(), balance, name);
               
       Transpay.accountSystem.addAccount(regAccount);
    }
}
