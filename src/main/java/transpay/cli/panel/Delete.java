package transpay.cli.panel;

import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class Delete {
    private String choice;
    private String PIN;
    
    public Delete() {
        new FlashWriter(Log.HEADING, "Delete your Account\n", true);

        new TypeWriter(Log.INPUT, "Are you sure you want to delete your account? (y/n)", true);
        getInput();

        ConsoleLog.print("\n");

        new TypeWriter(Log.INPUT, "Enter your 6-digit PIN (hidden for security):", true);
        getUserPIN();

        deleteAccount();

        new FlashWriter(Log.INFO, "\nReturning to Welcome page...", true);

        ConsoleLog.clear(1000);

        new Welcome();
    }

    private void getInput() {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                choice = ConsoleLog.getInput();
                
                if (choice.equalsIgnoreCase("y")) {
                    break;
                }
                else if (choice.equalsIgnoreCase("n")) {
                    new FlashWriter(Log.INFO, "Returning to Dashboard page...", true);
                    ConsoleLog.clear(1000);
                    new Dashboard();
                    break;
                }
                else {
                    new FlashWriter(Log.ERROR, "Invalid choice. Please try again.", true);
                }
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
    
                if (!PIN.matches("\\d{6}")) {
                    throw new NumberFormatException();
                }
                else if (!Transpay.account.getPIN().equals(PIN)) {
                    new FlashWriter(Log.ERROR, "Incorrect PIN. Please try again.", true);
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

    private void deleteAccount() {
        Transpay.accountSystem.deleteAccount(Transpay.account.getAccountNumber());
        new FlashWriter(Log.INFO, "\nAccount deleted successfully.", true);

        ConsoleLog.clear(1000);
        new Welcome();
    }
}
