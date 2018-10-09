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
                window.setLocationRelativeTo(null);
                img = new BufferedImage(1600, 900, BufferedImage.TYPE_INT_RGB);
                canvas = new Canvas();
                render = new Render(img, canvas);
                window.add(canvas);
                window.setVisible(true);


                /*canvas.addMouseListener(
                        new MouseAdapter()
                            {
                                @Override
                                public void mouseClicked(MouseEvent mouseEvent)
                                    {
                                        render.drawPixel(mouseEvent.getX(), mouseEvent.getY(), Color.WHITE.getRGB());
                                    }
                            });*/
                Point p1 = new Point(800,450);
                canvas.addMouseMotionListener(new MouseAdapter()
                    {
                        @Override
                        public void mouseDragged(MouseEvent e)
                            {
                                render.clear();

                                Point p2 = new Point(e.getX(), e.getY());
                                System.out.println("Call");
                                render.drawLine(p1, p2, 0x00ffff);
                            }
                    });
            }
        public static void main(String[] args)
            {
                SwingUtilities.invokeLater(PixelText::new);
            }
    }
