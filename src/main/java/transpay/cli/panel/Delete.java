package transpay.cli.panel;

import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class Delete {
    public Delete() {
        new FlashWriter(Log.HEADING, "\t\t    Delete your Account\n", true);

        getConfirmation();
        getUserPIN();
        deleteAccount();
    }

    private void getConfirmation() {
        new TypeWriter(Log.INPUT, "Are you sure you want to delete your account? (y/n)", true);

        while (true) {
            String input = Dashboard.getValidatedInput(
                "", 
                test -> {
                    return test.equalsIgnoreCase("y") || test.equalsIgnoreCase("n");
                }, 
                 "Invalid choice. Please try again.", 
                 false);
            
            if (input.equalsIgnoreCase("y")) {
                return; 
            }
            else {
                new FlashWriter(Log.INFO, "Returning to Dashboard...", false);
                ConsoleLog.clear(1000);
                new Dashboard();
                return;
            }
        }
    }

    private void getUserPIN() {
        new TypeWriter(Log.INPUT, "\nEnter your 6-digit PIN (hidden for security):", true);

        while (true) {  
            String input = Dashboard.getValidatedInput(
                "", 
                test1 -> {
                    return test1.matches("\\d{6}");
                }, 
                test2 -> {
                    return test2.equals(Transpay.account.getPIN());
                },
                 "PIN must be numeric and 6 digits long. Please try again.", 
                 "Incorrect PIN. Please try again.",
                 false);
            
            if (input != null) {
                break;
            }
        }
    }

    private void deleteAccount() {
        Transpay.accountSystem.deleteAccount(Transpay.account.getAccountNumber());
        new TypeWriter(Log.SUCCESS, "\nAccount deleted successfully.", true);

        ConsoleLog.clear(1000);
        new Start();
    }
}
