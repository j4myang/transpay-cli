package transpay.cli.panel;

import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class ManageAccount {
    private int choice;
    private String PIN;

    public ManageAccount() {
        new FlashWriter(Log.HEADING, "\t\t    Manage your Account\n", true);
        
        new TypeWriter(Log.SYSTEM, "Options", true);

        new TypeWriter(Log.OPTION, "1. ", false);
        new FlashWriter(Log.BODY, "Change name", true);

        new TypeWriter(Log.OPTION, "2. ", false);
        new FlashWriter(Log.BODY, "Change PIN", true);

        new TypeWriter(Log.OPTION, "3. ", false);
        new FlashWriter(Log.BODY, "Go Back", true);
        
        while (true) {
            new TypeWriter(Log.INPUT, "\nWhat would you like to do?", true);
            getInput();
    
            switch (choice) {
                case 1 -> {changeName();} 
                case 2 -> {changePIN();} 
                case 3 -> {ConsoleLog.clear(0); new Dashboard();} 
            }
        }
    }

    private void getInput() {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                String input = ConsoleLog.getInput();
                
                if (!input.matches("\\d+")) {
                    throw new NumberFormatException();
                }
                
                choice = Integer.parseInt(input);

                if (choice >= 1 && choice <= 3) {
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

    private void changeName() {
        new FlashWriter(Log.INPUT, "\nEnter your new name:", true);
        String name = "";
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                name = ConsoleLog.getInput();

                if (name.isBlank()) {
                    new FlashWriter(Log.ERROR, "Name cannot be empty. Please try again.", true);
                    continue;
                }
                else {
                    ConsoleLog.print("\n");

                    getUserPIN();
                    
                    new TypeWriter(Log.INFO, "Changing name...", true);

                    ConsoleLog.delay(1000);

                    String oldName = Transpay.account.getName();
                    Transpay.account.setName(name);
                    new FlashWriter(Log.SUCCESS, "\nChanged account name from " + oldName + " to " + name, true);
                    break;
                }
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }

    private void changePIN() {
        new FlashWriter(Log.INPUT, "\nEnter your new PIN (hidden for security):", true);
        String newPIN = "";

        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                newPIN = ConsoleLog.getPassword();

                if (!newPIN.matches("\\d{6}")) {
                    throw new NumberFormatException();
                }
                else if (Transpay.account.getPIN().equals(newPIN)) {
                    new FlashWriter(Log.ERROR, "New PIN cannot be the same as the current PIN. Please try again.", true);
                    continue;
                }
                else {
                    ConsoleLog.print("\n");

                    getUserPIN();

                    new TypeWriter(Log.INFO, "Changing PIN...", true);

                    ConsoleLog.delay(1000);

                    Transpay.account.setPIN(newPIN);
                    new FlashWriter(Log.SUCCESS, "\nChanged PIN", true);
                    break;
                }
            } catch (NumberFormatException e) {
                new FlashWriter(Log.ERROR, "PIN must be numeric and 6 digits long. Please try again.", true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getUserPIN() {
        new FlashWriter(Log.INPUT, "Enter your PIN to proceed (hidden for security):", true);
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
                e.printStackTrace();
            }
        }
    }
}
