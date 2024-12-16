package transpay.cli.panel;

import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Greet;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class Welcome {
    private int choice;
    
    
    public Welcome() {
        new Greet(Transpay.brand.toUpperCase());

        new TypeWriter(Log.HEADING, "\t\t    Welcome to " + Transpay.brand + "!", true);
        new TypeWriter(Log.SYSTEM, "\t       Your trustworthy banking system\n", true);

        new TypeWriter(Log.SYSTEM, "Current Status: ", false);
        new FlashWriter(Log.HEADING, Transpay.status, true);
        
        new TypeWriter(Log.SYSTEM, "\nOptions\n", true);

        new TypeWriter(Log.OPTION, "1. ", false);
        new FlashWriter(Log.BODY, "Register an account", true);

        new TypeWriter(Log.OPTION, "2. ", false);
        new FlashWriter(Log.BODY, "Login", true);

        new TypeWriter(Log.OPTION, "3. ", false);
        new FlashWriter(Log.BODY, "Exit", true);

        new TypeWriter(Log.OPTION, "4. ", false);
        new FlashWriter(Log.BODY, "Administrator", true);

        new TypeWriter(Log.INPUT, "\nWhat would you like to do?", true);
        getInput();
    }

    private void getInput() {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                String input = ConsoleLog.getInput();
                choice = Integer.parseInt(input);
                
                if (Transpay.status.equals("Offline") && (choice == 1 || choice == 2)) {
                    new FlashWriter(Log.ERROR, "The system is currently offline. Please try again later.", true);
                    
                    ConsoleLog.delay(1000);
                    continue;
                }

                else if (choice == 1) {
                    ConsoleLog.clear(0);
                    new Register();
                    break;
                }
                
                else if (choice == 2) {
                    ConsoleLog.clear(0);
                    new Login();
                    break;
                }
    
                else if (choice == 3) {
                    Transpay.exit = true;
                    break;
                }
                
                else if (choice == 4) {
                    ConsoleLog.clear(0);
                    new Admin();
                    break;
                }
                else {
                    String errorMsg = "Invalid choice. Please try again.";
                    new FlashWriter(Log.ERROR, errorMsg, true);
                }
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }
}
