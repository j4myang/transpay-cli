package transpay.cli.components;

public class Greet {
    public Greet(String input) {
        String brand = " _______  ______    _______  __    _  _______  _______  _______  __   __ \r\n" +
                        "|       ||    _ |  |   _   ||  |  | ||       ||       ||   _   ||  | |  |\r\n" +
                        "|_     _||   | ||  |  |_|  ||   |_| ||  _____||    _  ||  |_|  ||  |_|  |\r\n" +
                        "  |   |  |   |_||_ |       ||       || |_____ |   |_| ||       ||       |\r\n" +
                        "  |   |  |    __  ||       ||  _    ||_____  ||    ___||       ||_     _|\r\n" +
                        "  |   |  |   |  | ||   _   || | |   | _____| ||   |    |   _   |  |   |  \r\n" +
                        "  |___|  |___|  |_||__| |__||_|  |__||_______||___|    |__| |__|  |___|  ";

        ConsoleLog.clear(0);

        for (char c: brand.toCharArray()) {
            new TypeWriter(Log.HEADING, String.valueOf(c), false);
        }

        ConsoleLog.clear(1000);
        
        ConsoleLog.print("\n");
    }
}
