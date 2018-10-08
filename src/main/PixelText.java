package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PixelText
    {
        private JFrame window;
        private Canvas canvas;
        private BufferedImage img;
        private Render render;

        public PixelText()
            {
                window = new JFrame();
                window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                window.setSize(1600, 900);
                window.setTitle("PRGF Cviceni");
                img = new BufferedImage(1600, 900, BufferedImage.TYPE_INT_RGB);
                canvas = new Canvas();
                render = new Render(img, canvas);
                window.add(canvas);
                window.setVisible(true);
                canvas.addMouseListener(
                        new MouseAdapter()
                            {
                                @Override
                                public void mouseClicked(MouseEvent mouseEvent)
                                    {
                                        render.drawPixel(mouseEvent.getX(), mouseEvent.getY(), Color.WHITE.getRGB());
                                    }
                            });
                render.drawLine(100, 200, 200, 400, 0xFFFF00);
            }


        public static void main(String[] args)
            {
                SwingUtilities.invokeLater(PixelText::new);
            }
    }
