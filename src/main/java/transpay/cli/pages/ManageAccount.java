package transpay.cli.pages;

import java.util.Scanner;

import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class ManageAccount {
    private Scanner scan = Transpay.scan;
    private int choice;
    private String PIN;

    public ManageAccount() {
        new FlashWriter(Log.HEADING, "Manage your Account\n", true);
        
        new TypeWriter(Log.BODY, "Options", true);
        new FlashWriter(Log.OPTION, "1. Change name", true);
        new FlashWriter(Log.OPTION, "2. Change PIN", true);
        new FlashWriter(Log.OPTION, "3. Go Back", true);
        
        while (true) {
            new TypeWriter(Log.BODY, "\nWhat would you like to do?", true);
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
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt, false);
                String input = ConsoleLog.getInput(scan);
                
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
        new FlashWriter(Log.BODY, "\nEnter your new name:", true);
        String name = "";
        while (true) {
            try {
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt, false);
                name = ConsoleLog.getInput(scan);

                if (name.isBlank()) {
                    new FlashWriter(Log.ERROR, "Name cannot be empty. Please try again.", true);
                    continue;
                }
                else {
                    getUserPIN();
                    String oldName = Transpay.account.getName();
                    Transpay.account.setName(name);
                    new FlashWriter(Log.INFO, "\nChanged account name from " + oldName + " to " + name, true);
                    break;
                }
            } catch (Exception e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            }
        }
    }

    private void changePIN() {
        new FlashWriter(Log.BODY, "\nEnter your new PIN:", true);
        String newPIN = "";

        while (true) {
            try {
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt, false);
                newPIN = ConsoleLog.getInput(scan);

                if (!newPIN.trim().matches("\\d{6}")) {
                    throw new NumberFormatException();
                }
                else if (Transpay.account.getPIN().equals(newPIN.trim())) {
                    new FlashWriter(Log.ERROR, "New PIN cannot be the same as the current PIN. Please try again.", true);
                    continue;
                }
                else {
                    getUserPIN();
                    
                    String oldPIN = Transpay.account.getPIN();
                    Transpay.account.setPIN(newPIN.trim());
                    new FlashWriter(Log.INFO, "\nChanged PIN from " + oldPIN + " to " + newPIN, true);
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
        new FlashWriter(Log.BODY, "Enter your PIN to proceed:", true);
        while (true) {
            try {
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt, false);
                PIN = ConsoleLog.getInput(scan);
                if (!PIN.trim().matches("\\d{6}")) {
                    throw new NumberFormatException();
                }
                else if (!Transpay.account.getPIN().equals(PIN.trim())) {
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
