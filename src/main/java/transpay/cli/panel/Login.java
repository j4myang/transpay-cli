package transpay.cli.panel;

import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class Login {

    public Login() {
        new FlashWriter(Log.HEADING, "\t\t    Login to your Account\n", true);
        
        new TypeWriter(Log.BODY, "    If going here was a mistake, use 'exit' command\n", true);
        
        String accountNumber = getAccountNumber();
        getUserPIN(accountNumber);

        new FlashWriter(Log.INFO, "\nVerifying your account...", true);
        ConsoleLog.delay(1000);
        new TypeWriter(Log.SUCCESS, "\nLogin successful! Please wait a moment...", true);
        ConsoleLog.clear(1000);

        Transpay.account = Transpay.accountSystem.getAccount(accountNumber);

        new Dashboard();
    }

    private String getAccountNumber() {
        new TypeWriter(Log.INPUT, "Enter your account number:", true);

        while (true) {
            String input = Start.getValidatedInput(
                "", 
                test1 -> {
                    return !test1.isBlank();
                }, 
                test2 -> {
                    return Transpay.accountSystem.getAccount(test2) != null;
                }, 
                "Account number cannot be empty. Please try again.", 
                "Account does not exist. Please try again.",
                false
                );

            if (input != null) {
                return input;
            }
        }
    }

    private void getUserPIN(String accountNumber) {
        new TypeWriter(Log.INPUT, "\nEnter your 6-digit PIN (hidden for security):", true);

        while (true) {
            String pin = Start.getValidatedInput(
                "", 
                test1 -> test1.matches(Transpay.pinPattern), 
                test2 -> test2.equals(Transpay.accountSystem.getAccount(accountNumber).getPIN()),
                "PIN must be numeric and 6 digits long. Please try again.", 
                "PIN is incorrect. Please try again.",
                true);

            if (pin != null) {
                break;
            }
        }
    }
}