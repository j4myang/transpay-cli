package transpay.cli.panel;

import java.util.UUID;

import transpay.account.Account;
import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class Register {
    private String accountNumber;
    private String name;
    private String PIN;
    private String confirmPIN;
    private double balance;
    private boolean back = false;

    public Register() {
        new FlashWriter(Log.HEADING, "\t\t    Register an Account\n", true);

        new TypeWriter(Log.SYSTEM, "    If going here was a mistake, use 'exit' command\n", true);

        new TypeWriter(Log.INPUT, "Enter your name:", true);
        getUserName();

        ConsoleLog.print("\n");

        new TypeWriter(Log.INPUT, "Enter your 6-digit PIN (hidden for security):", true);
        getUserPIN();

        ConsoleLog.print("\n");

        new TypeWriter(Log.INPUT, "Confirm your 6-digit PIN (hidden for security):", true);
        getConfirmUserPIN();

        ConsoleLog.print("\n");

        new TypeWriter(Log.INPUT, "Enter your starting balance:", true);
        getStartingBalance();

        registerAccount();

        new TypeWriter(Log.INFO, "\nPlease wait a moment while we prepare your account...\n", true);

        ConsoleLog.delay(1000);

        new TypeWriter(Log.SUCCESS, "Account registration successful!\n", true);

        ConsoleLog.clear(1000);

        new TypeWriter(Log.HEADING, "\t\t    Welcome aboard, " + name + "!\n", true);

        displaySummary();

        new TypeWriter(Log.INPUT, "\nPress enter to go back:", true);
        goBack();

        if (back) {
            new FlashWriter(Log.INFO, "\nReturning to Welcome page...", true);

            ConsoleLog.clear(1000);

            new Welcome();
        }
    }


    private void getUserName() {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                name = ConsoleLog.getInput();
                
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
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                PIN = ConsoleLog.getPassword();

                if (PIN.equalsIgnoreCase("exit")) {
                    ConsoleLog.clear(0);
                    new Welcome();
                    break;
                }
                else if (!PIN.matches("\\d{6}")) {
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
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                confirmPIN = ConsoleLog.getPassword();
                
                if (confirmPIN.equalsIgnoreCase("exit")) {
                    ConsoleLog.clear(0);
                    new Welcome();
                    break;
                }
                else if (!confirmPIN.matches("\\d{6}")) {
                    throw new NumberFormatException();
                }
                else if (!PIN.equals(confirmPIN)) {
                    new FlashWriter(Log.ERROR, "PINs do not match. Please try again.", true);
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                new FlashWriter(Log.ERROR, "PIN must be numeric and 6 digits long. Please try again.", true);
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }

    private void getStartingBalance() {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt + "PHP ", false);
                String input = ConsoleLog.getInput();

                if (input.equalsIgnoreCase("exit")) {
                    ConsoleLog.clear(0);
                    new Welcome();
                    break;
                }

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
        new FlashWriter(Log.HEADING, accountNumber, true);

        new TypeWriter(Log.BODY, "Account Name: ", false);
        new FlashWriter(Log.HEADING, name, true);

        new TypeWriter(Log.BODY, "PIN: ", false);
        new FlashWriter(Log.HEADING, "*".repeat(PIN.length()), true);

        new TypeWriter(Log.BODY, "Balance: PHP ", false);
        new FlashWriter(Log.HEADING, String.format("%,.2f", balance), true);
    }

    private void goBack() {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                ConsoleLog.getInput();
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

       Account regAccount = new Account(accountNumber, PIN, balance, name);
               
       Transpay.accountSystem.addAccount(regAccount);
    }
}
