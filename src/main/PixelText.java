package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PixelText
    {
        private JFrame window;
        private Canvas canvas;
        private BufferedImage img;
        private Render render;
        private boolean active = false;
        private List<Point> points = new ArrayList<>();

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
                System.out.println("OK");

                canvas.addMouseListener(new MouseAdapter()
                            {
                                @Override
                                public void mouseClicked(MouseEvent mouseEvent)
									{
										Point p = new Point(mouseEvent.getX(), mouseEvent.getY());
										render.drawPixel((int)p.getX(), (int)p.getY(), Color.WHITE.getRGB());
										points.add(p);
										render.drawIPolygon(points);
									}
                            });
                canvas.addMouseMotionListener(new MouseAdapter()
                    {
                        @Override
                        public void mouseDragged(MouseEvent e)
                            {

                            }
                    });
            }
        public static void main(String[] args)
            {
                //SwingUtilities.invokeLater(PixelText::new);
				new PixelText();
            }
    }
