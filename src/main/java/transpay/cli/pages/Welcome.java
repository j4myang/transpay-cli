package transpay.cli.pages;

import java.util.Scanner;

import transpay.cli.Transpay;
import transpay.cli.components.Color;
import transpay.cli.components.ConsoleLog;
import transpay.cli.components.FlashWriter;
import transpay.cli.components.Greet;
import transpay.cli.components.Log;
import transpay.cli.components.TypeWriter;

public class Welcome {
    private int choice;
    private Scanner scan = Transpay.scan;
    
    public Welcome() {
        new Greet("TRANSPAY", Color.CYAN);

        new TypeWriter(Log.HEADING, "\t\t    Welcome to Transpay!", true);
        new TypeWriter(Log.BODY, "\t       Your trustworthy banking system\n", true);
        
        new TypeWriter(Log.BODY, "Options", true);
        new FlashWriter(Log.OPTION, "1. Register an account", true);
        new FlashWriter(Log.OPTION, "2. Login", true);
        new FlashWriter(Log.OPTION, "3. Exit", true);

        new TypeWriter(Log.BODY, "\nWhat would you like to do?", true);
        getInput();
    }

    private void getInput() {
        while (true) {
            try {
                new FlashWriter(Log.BODY, ConsoleLog.inputPrompt, false);
                String input = ConsoleLog.getInput(scan);
                choice = Integer.parseInt(input);
    
                if (choice == 1) {
                    ConsoleLog.clear(0);
                    new Registration();
                    break;
                }
                
                else if (choice == 2) {
                    ConsoleLog.clear(0);
                    new Login();
                    break;
                }
    
                if (choice == 3) {
                    Transpay.exit = true;
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

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }
}
