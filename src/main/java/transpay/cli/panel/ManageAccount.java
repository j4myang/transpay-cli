package transpay.cli.panel;

import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class ManageAccount {
    public ManageAccount() {
        new FlashWriter(Log.HEADING, "\t\t    Manage your Account\n", true);
        
        new TypeWriter(Log.SYSTEM, "Options", true);

        new TypeWriter(Log.OPTION, "1. ", false);
        new FlashWriter(Log.BODY, "Change name", true);

        new TypeWriter(Log.OPTION, "2. ", false);
        new FlashWriter(Log.BODY, "Change PIN", true);

        new TypeWriter(Log.OPTION, "3. ", false);
        new FlashWriter(Log.BODY, "Go Back", true);
        
        handleAction();
    }

    private void handleAction() {
        new TypeWriter(Log.INPUT, "\nWhat would you like to do?", true);

        while (true) {
            int action = Integer.parseInt(Dashboard.getValidatedInput("", test -> {
                try {
                    if (!test.matches("\\d+")) {
                        throw new NumberFormatException();
                    }

                    int temp = Integer.parseInt(test);
                    
                    return temp >= 1 && temp <= 3;
                } catch (NumberFormatException e) {
                    return false;
            }}, "Invalid choice. Please try again.", false));

            switch (action) {
                case 1:
                    changeName();
                    break;
                case 2:
                    changePIN();
                    break;
                case 3:
                    new FlashWriter(Log.INFO, "Returning to Dashboard...", false);
                    ConsoleLog.clear(1000);
                    new Dashboard();
                    break;
            }
        }
    }


    private void changeName() {
        new FlashWriter(Log.INPUT, "\nEnter your new name:", true);
        
        while (true) {
            String name = Dashboard.getValidatedInput(
                "", 
                test -> {
                    return !test.isBlank();
                }, 
                "Name cannot be empty. Please try again.", 
                false);
            
            if (name != null) {
                getUserPIN();

                new FlashWriter(Log.INFO, "Changing name...", true);
        
                ConsoleLog.delay(1000);
        
                String oldName = Transpay.account.getName();
                Transpay.account.setName(name);
                new TypeWriter(Log.SUCCESS, "\nChanged account name from " + oldName + " to " + name, true);
                return;
            }
        }
    }

    private void changePIN() {
        new FlashWriter(Log.INPUT, "\nEnter your new PIN (hidden for security):", true);

        while (true) {
            String newPin = Dashboard.getValidatedInput(
                "", 
                test1 -> {
                    return test1.matches(Transpay.pinPattern);
                }, 
                test2 -> {
                    return !Transpay.account.getPIN().equals(test2);
                }, 
                "PIN must be numeric and 6 digits long. Please try again.", 
                "New PIN cannot be the same as the current PIN. Please try again.", 
                true);
          
            if (newPin != null) {
                getUserPIN();

                new FlashWriter(Log.INFO, "\nChanging PIN...", true);

                ConsoleLog.delay(1000);

                Transpay.account.setPIN(newPin);
                new TypeWriter(Log.SUCCESS, "\nChanged PIN", true);
                return;
            }
        }
    }

    private void getUserPIN() {
        new FlashWriter(Log.INPUT, "\nEnter your PIN to proceed (hidden for security):", true);

        while (true) {
            String pin = Dashboard.getValidatedInput(
                "", 
                test1 -> test1.matches(Transpay.pinPattern), 
                test2 -> test2.equals(Transpay.account.getPIN()),
                "PIN must be numeric and 6 digits long. Please try again.", 
                "PIN is incorrect. Please try again.",
                true);

            if (pin != null) {
                break;
            }
        }
    }
}
