package transpay.cli.panel;

import java.time.LocalTime;
import java.util.Scanner;

import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class Dashboard {
    private int choice;
    private Scanner scan = Transpay.scan;

    public Dashboard() {
        new FlashWriter(Log.HEADING, "\t\t    Good " + computeDayMessage() + ", " + Transpay.account.getName() + "!\n", true);

        new TypeWriter(Log.SYSTEM, "Current Status: ", false);
        new FlashWriter(Log.HEADING, Transpay.status, true);

        new TypeWriter(Log.SYSTEM, "\nTransactions\n", true);

        new TypeWriter(Log.OPTION, "1. ", false);
        new FlashWriter(Log.BODY, "Withdraw", true);

        new TypeWriter(Log.OPTION, "2. ", false);
        new FlashWriter(Log.BODY, "Deposit", true);

        new TypeWriter(Log.OPTION, "3. ", false);
        new FlashWriter(Log.BODY, "Transfer", true);

        new TypeWriter(Log.OPTION, "4. ", false);
        new FlashWriter(Log.BODY, "View balance", true);

        new TypeWriter(Log.BODY, "\nSettings\n", true);

        new TypeWriter(Log.OPTION, "5. ", false);
        new FlashWriter(Log.BODY, "View account details", true);

        new TypeWriter(Log.OPTION, "6. ", false);
        new FlashWriter(Log.BODY, "Manage account", true);

        new TypeWriter(Log.OPTION, "7. ", false);
        new FlashWriter(Log.BODY, "Logout", true);

        new TypeWriter(Log.OPTION, "8. ", false);
        new FlashWriter(Log.BODY, "Exit", true);

        new TypeWriter(Log.BODY, "\nAdvanced\n", true);

        new TypeWriter(Log.OPTION, "9. ", false);
        new FlashWriter(Log.BODY, "Delete account", true);

        new TypeWriter(Log.INPUT, "\nWhat would you like to do?", true);

        while (true) {
            getInput();

            switch (choice) {
                case 1:
                    if (Transpay.status.equals("Maintenance")) {
                        new FlashWriter(Log.ERROR, "The system is currently under maintenance. Please try again later.", true);
                        continue;
                    }
    
                    ConsoleLog.clear(0); 
                    new Withdraw();
                    break;
                case 2:
                    if (Transpay.status.equals("Maintenance")) {
                        new FlashWriter(Log.ERROR, "The system is currently under maintenance. Please try again later.", true);
                        continue;
                    }
    
                    ConsoleLog.clear(0); 
                    new Deposit();
                    break;
                case 3:
                    if (Transpay.status.equals("Maintenance")) {
                        new FlashWriter(Log.ERROR, "The system is currently under maintenance. Please try again later.", true);
                        continue;
                    }
    
                    ConsoleLog.clear(0); 
                    new Transfer();
                    break;
                case 4:
                    ConsoleLog.clear(0);
                    new Balance();
                    break;
                case 5:
                    ConsoleLog.clear(0);
                    new AccountDetail();
                    break;
                case 6:
                    ConsoleLog.clear(0);
                    new ManageAccount();
                    break;
                case 7:
                    ConsoleLog.clear(0);
                    new Welcome();
                    break;
                case 8:
                    ConsoleLog.clear(0);
                    Transpay.exit = true;
                    break;
                case 9:
                    ConsoleLog.clear(0);
                    new Delete();
                    break;
            }
        }
    }

    private void getInput() {
        while (true) {
            try {
                new FlashWriter(Log.INPUT, ConsoleLog.inputPrompt, false);
                String input = ConsoleLog.getInput(scan);
    
                if (!input.matches("\\d+")) {
                    throw new NumberFormatException();
                }

                choice = Integer.parseInt(input);
                
                if (choice >= 1 && choice <= 9) {
                    break;
                }
                else {
                    new FlashWriter(Log.ERROR, "Invalid choice. Please try again.", true);
                }
            } catch (NumberFormatException e) {
                new FlashWriter(Log.ERROR, "Invalid input. Please try again.", true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String computeDayMessage() {
        int currentHour = LocalTime.now().getHour();

        return currentHour < 12 ? "Morning" : currentHour < 18 ? "Afternoon" : "Evening";
    }
}
