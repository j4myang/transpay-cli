package transpay.cli.pages;

import java.time.OffsetTime;
import java.util.Scanner;

import transpay.cli.Transpay;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class Dashboard {;
    private int choice;
    private Scanner scan = Transpay.scan;

    public Dashboard() {
        new FlashWriter(Log.HEADING, "Good " + computeDayMessage() + ", " + Transpay.account.getName() + "!\n", true);

        new TypeWriter(Log.BODY, "Transactions", true);
        new FlashWriter(Log.OPTION, "1. Withdraw", true);
        new FlashWriter(Log.OPTION, "2. Deposit", true);
        new FlashWriter(Log.OPTION, "3. Transfer", true);
        new FlashWriter(Log.OPTION, "4. View balance", true);
        new FlashWriter(Log.OPTION, "5. View account details", true);

        new TypeWriter(Log.BODY, "Settings", true);
        new FlashWriter(Log.OPTION, "6. Manage account", true);
        new FlashWriter(Log.OPTION, "7. Logout", true);
        new FlashWriter(Log.OPTION, "8. Exit", true);

        new TypeWriter(Log.BODY, "Advanced", true);
        new FlashWriter(Log.OPTION, "9. Delete", true);

        new TypeWriter(Log.BODY, "\nWhat would you like to do?", true);
        getInput();

        switch (choice) {
            case 1 -> {ConsoleLog.clear(0); new Withdraw();}
            case 2 -> {ConsoleLog.clear(0); new Deposit();}
            case 3 -> {ConsoleLog.clear(0); new Transfer();}
            case 4 -> {ConsoleLog.clear(0); new Balance();}
            case 5 -> {ConsoleLog.clear(0); new AccountDetail();}
            case 6 -> {ConsoleLog.clear(0); new ManageAccount();}
            case 7 -> {ConsoleLog.clear(0); new Welcome();}
            case 8 -> {ConsoleLog.clear(0); Transpay.exit = true;}
            case 9 -> {ConsoleLog.clear(0); new Delete();}
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

    private String computeDayMessage() {
        int currentHour = OffsetTime.now().getHour();

        return currentHour < 12 ? "Morning" : currentHour < 17 ? "Afternoon" : "Evening";
    }
}
