package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
        private boolean activeIrr = false;
        private boolean activeReg = false;
        private List<Point> points = new ArrayList<>();
        private Point center;
        private Point radius;
        private Point sides;
        private int clicks;
      //  private List<Point> regularPoly = new ArrayList<>();
        private boolean isFirst = true;

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
									    if(SwingUtilities.isLeftMouseButton(mouseEvent))
                                            {
                                                Point p = new Point(mouseEvent.getX(), mouseEvent.getY());
                                                points.add(p);
                                                render.drawPolygon(points);
                                                render.drawPixel((int) p.getX(), (int) p.getY(), 0xFFFFFF);
                                                if (points.size() > 2)
                                                    {
                                                        render.drawLine(points.get(0), points.get(points.size() - 1), 0xFFFFFF);
                                                    }
                                                activeIrr = true;
                                            }
                                        if(SwingUtilities.isRightMouseButton(mouseEvent))
                                            {
                                                switch (clicks)
                                                    {
                                                        case 0:
                                                            center = new Point(mouseEvent.getX(), mouseEvent.getY());
                                                            render.drawPixel((int)center.getX(), (int)center.getY(), 0xFF0000);
                                                            activeReg = true;
                                                            break;
                                                        case 1:
                                                            radius = new Point(mouseEvent.getX(), mouseEvent.getY());
                                                            activeReg = false;
                                                        break;
                                                        case 2:
                                                            sides = new Point(mouseEvent.getX(), mouseEvent.getY());
                                                            break;
                                                        default:break;
                                                    }
                                                clicks++;

                                            }

									}
                            });
                canvas.addMouseMotionListener(new MouseAdapter()
                    {
                        @Override
                        public void mouseMoved(MouseEvent e)
                            {
                                if(activeIrr)
                                    {
                                        drawDonePolygon();
                                        render.drawLine(new Point(e.getX(), e.getY()), points.get(points.size()-1), 0xFF0000);
                                    }
                                if(activeReg)
                                    {
                                        drawDonePolygon();
                                        render.drawDDALine(center, new Point(e.getX(), e.getY()),0xFF0000);
                                    }
                            }

                        @Override
                        public void mouseReleased(MouseEvent e)
                            {

                            }
                    });
                canvas.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e)
                        {
                            if(e.getKeyCode() == 27)
                                {
                                    activeIrr =false;
                                    drawDonePolygon();
                                }

                        }
                });
            }
        public static void main(String[] args)
            {
                //SwingUtilities.invokeLater(PixelText::new);
				new PixelText();
            }
        public void drawDonePolygon()
            {
                render.drawPolygon(points);
                if (points.size() > 2)
                    {
                        render.drawLine(points.get(0), points.get(points.size() - 1), 0xFFFFFF);
                    }
            }
    }
