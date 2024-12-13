package transpay.cli.components;

public class FlashWriter {
    public FlashWriter(Log logType, String text, boolean newLine) {
        switch (logType) {
            case ERROR -> ConsoleLog.logError(text);
            case SUCCESS -> ConsoleLog.logSuccess(text);
            case INFO -> ConsoleLog.logInfo(text);
            case BODY -> ConsoleLog.logBody(text);
            case HEADING -> ConsoleLog.logHeading(text);
            case OPTION -> ConsoleLog.logOption(text);
            case INPUT -> ConsoleLog.logInput(text);
            case SYSTEM -> ConsoleLog.logSystem(text);
        }
        
        if (newLine) {
            ConsoleLog.print("\n");
        }
    }
}
