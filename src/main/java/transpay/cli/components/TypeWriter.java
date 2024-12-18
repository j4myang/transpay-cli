package transpay.cli.components;

public class TypeWriter {
    public TypeWriter(Log logType, String text, boolean newLine) {
        for (char c : text.toCharArray()) {
            String character = String.valueOf(c);

            try {
                switch (logType) {
                    case ERROR -> ConsoleLog.logError(character);
                    case SUCCESS -> ConsoleLog.logSuccess(character);
                    case INFO -> ConsoleLog.logInfo(character);
                    case BODY -> ConsoleLog.logBody(character);
                    case HEADING -> ConsoleLog.logHeading(character);
                    case OPTION -> ConsoleLog.logOption(character);
                    case INPUT -> ConsoleLog.logInput(character);
                    case SYSTEM -> ConsoleLog.logSystem(character);
                }

                Thread.sleep(5);
            } catch (InterruptedException e) {
                ConsoleLog.logError("TypeWriter aborted. Thread interrupted.");
                e.printStackTrace();
                break;
            }
        }

        if (newLine) {
            ConsoleLog.print("\n");
        }
    }
}
