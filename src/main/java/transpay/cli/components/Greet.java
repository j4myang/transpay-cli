package transpay.cli.components;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Greet {
    private Font font = new Font("Monospace", Font.BOLD, 12);


    public Greet(String input) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        g.setFont(font);
        int width = g.getFontMetrics().stringWidth(input);

        BufferedImage img1 = new BufferedImage(width, 12, BufferedImage.TYPE_INT_RGB);
        Graphics g1 = img1.getGraphics();
        g1.setFont(font);

        ((Graphics2D) g1).drawString(input, 0, g1.getFontMetrics(g1.getFont()).getAscent() - g1.getFontMetrics(g1.getFont()).getDescent());

        ConsoleLog.clear(0);

        for (int i = 0; i < 12; i++) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int j = 0; j < width; j++) {
                stringBuilder.append(img1.getRGB(j, i) == java.awt.Color.WHITE.getRGB() ? "]" : " ");
            }

            if (stringBuilder.toString().trim().isBlank()) {
                continue;
            }
            
            new TypeWriter(Log.HEADING, stringBuilder.toString(), 1, true);
	    }

        ConsoleLog.clear(1000);
    }
}
