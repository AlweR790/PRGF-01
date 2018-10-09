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
        private Point first = new Point();
        private Point second = new Point();
        private boolean firstPoint = true;


        public PixelText()
            {
                window = new JFrame();
                window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                window.setSize(1600, 900);
                window.setTitle("PRGF Cviceni");
                window.setLocationRelativeTo(null);
                img = new BufferedImage(1601, 901, BufferedImage.TYPE_INT_RGB);
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
                                        if(firstPoint)
                                            {
                                                first.setY(mouseEvent.getY());
                                                first.setX(mouseEvent.getX());
                                                firstPoint = false;
                                                render.drawPixel((int)first.getX(), (int)first.getY(), 0x00ff00);
                                            }
                                    }
                            });
                canvas.addMouseMotionListener(new MouseAdapter()
                    {
                        @Override
                        public void mouseDragged(MouseEvent e)
                            {
                                if(!firstPoint)
                                    {
                                        render.clear();
                                        second.setX(e.getX());
                                        second.setY(e.getY());
                                        render.drawLine(first, second, 0x00ffff);
                                    }
                            }
                    });
            }
        public static void main(String[] args)
            {
                SwingUtilities.invokeLater(PixelText::new);
            }
    }
