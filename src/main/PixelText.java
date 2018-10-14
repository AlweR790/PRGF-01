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
        private BufferedImage img2;
        private Render render;
        private boolean activeIrr = false;
        private boolean activeReg = false;
        private boolean activeRegDraw = false;
        private List<Point> points = new ArrayList<>();
        private Point center;
        private Point radius;
        private Point sides;
        private int clicks;
        private int sidesN;
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
                render.drawXiaolinWuLine(new Point(100,200),new Point(800,250));
                canvas.addMouseListener(new MouseAdapter()
                            {
                                @Override
                                public void mouseClicked(MouseEvent mouseEvent)
									{
									    if(SwingUtilities.isLeftMouseButton(mouseEvent))
                                            {
                                                render.clear();
                                                Point p = new Point(mouseEvent.getX(), mouseEvent.getY());
                                                points.add(p);
                                                render.drawPolygon(points);
                                                render.drawPixel((int) p.getX(), (int) p.getY(), 0xFFFFFF);
                                                if (points.size() > 2)
                                                    {
                                                        render.drawLine(points.get(0), points.get(points.size() - 1), 0xFFFFFF);
                                                    }
                                                activeIrr = true;
                                                render.calcPolygon(center, radius,sidesN);
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
                                                            activeRegDraw = true;
                                                        break;
                                                        case 2:
                                                            sides = new Point(mouseEvent.getX(), mouseEvent.getY());
                                                            activeRegDraw = false;
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
                                render.clear();
                                render.drawXiaolinWuLine(new Point(500,500), new Point(e.getX(), e.getY()));
                                System.out.println(e.getX() +" : " + e.getY());
                                if(activeIrr)
                                    {
                                        render.clear();
                                        drawDonePolygon();
                                        render.calcPolygon(center, radius,sidesN);
                                        render.drawLine(new Point(e.getX(), e.getY()), points.get(points.size()-1), 0xFF0000);
                                        render.drawLine(new Point(e.getX(), e.getY()), points.get(0),  0xFF05000);
                                    }
                                if(activeReg)
                                    {
                                        render.clear();
                                        drawDonePolygon();
                                        render.drawDDALine(center, new Point(e.getX(), e.getY()),0xFF0000);
                                    }
								if (activeRegDraw)
									{
                                        render.clear();
										drawDonePolygon();
										Point p = new Point(e.getX(), e.getY());
										sidesN = 3+ (int)p.getY() / 35;
										render.calcPolygon(center, radius,sidesN);
									}
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
							if(e.getKeyCode() == 127)
								{
									render.clear();
									points = new ArrayList<>();
									center = new Point();
									radius = new Point();
									sides = new Point();
									sidesN = 0;
                                    clicks = 0;
                                    activeIrr = false;
                                    activeReg = false;
                                    activeRegDraw = false;
								}

                        }
                });
            }
        public static void main(String[] args)
            {
                SwingUtilities.invokeLater(PixelText::new);
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
