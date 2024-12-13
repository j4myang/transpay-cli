package transpay.cli.panel;

import java.util.Scanner;

import transpay.account.Account;
import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class Login {
    private String PIN;
    private Scanner scan = Transpay.scan;
    private Account account;

    public Login() {
        new FlashWriter(Log.HEADING, "\t\t    Login to your Account\n", true);
        
        new TypeWriter(Log.BODY, "    If going here was a mistake, use 'exit' command\n", true);
        
        new TypeWriter(Log.INPUT, "Enter your account number:", true);
        getAccountNumber();

        ConsoleLog.print("\n");

        new TypeWriter(Log.INPUT, "Enter your 6-digit PIN (hidden for security):", true);
        getUserPIN();

        Transpay.account = account;
        
        new TypeWriter(Log.INFO, "\nVerifying your account...", true);

        ConsoleLog.delay(1000);
        
        new TypeWriter(Log.SUCCESS, "\nLogin successful! Please wait a moment...", true);

        ConsoleLog.clear(1000);

        new Dashboard();
    }

    private void getAccountNumber() {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                String accountNumber = ConsoleLog.getInput(scan);
                
                account = Transpay.accountSystem.getAccount(accountNumber);
                
                if (accountNumber.equalsIgnoreCase("exit")) {
                    ConsoleLog.clear(0);
                    new Welcome();
                    break;
                }
                else if (accountNumber.isBlank()) {
                    new FlashWriter(Log.ERROR, "Account number cannot be empty. Please try again.", true);
                    continue;
                }
                else if (account == null) {
                    new FlashWriter(Log.ERROR, "Account number does not exist. Please try again.", true);
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
                PIN = ConsoleLog.getPassword(scan);

                if (PIN.equalsIgnoreCase("exit")) {
                    ConsoleLog.clear(0);
                    new Welcome();
                    break;
                }
                else if (!PIN.matches("\\d{6}")) {
                    throw new NumberFormatException();
                }
                else if (!account.getPIN().equals(PIN)) {
                    new FlashWriter(Log.ERROR, "Incorrect PIN. Please try again.", true);
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
}