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
        private boolean done = false;
        private boolean antiAliasing = false;
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
                render.drawXiaolinWuLine(new Point(800, 200), new Point(100, 300));
                canvas.addMouseListener(new MouseAdapter()
                    {
                        @Override
                        public void mouseClicked(MouseEvent mouseEvent)
                            {
                                if (SwingUtilities.isLeftMouseButton(mouseEvent))
                                    {
                                        render.clear();
                                        Point p = new Point(mouseEvent.getX(), mouseEvent.getY());
                                        points.add(p);
                                        render.drawPolygon(points, antiAliasing);
                                        render.drawPixel((int) p.getX(), (int) p.getY(), 0xFFFFFF);
                                        if (points.size() > 2)
                                            {
                                                if (antiAliasing)
                                                    render.drawXiaolinWuLine(points.get(0), points.get(points.size() - 1));
                                                else
                                                    render.drawLine(points.get(0), points.get(points.size() - 1), 0xFFFFFF);
                                            }
                                        activeIrr = true;
                                        if (done)
                                            render.calcPolygon(center, radius, sidesN, antiAliasing);
                                    }
                                if (SwingUtilities.isRightMouseButton(mouseEvent))
                                    {
                                        switch (clicks)
                                            {
                                                case 0:
                                                    center = new Point(mouseEvent.getX(), mouseEvent.getY());
                                                    render.drawPixel((int) center.getX(), (int) center.getY(), 0xFF0000);
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
                                                    done = true;
                                                    break;
                                                default:
                                                    break;
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
                                if (activeIrr)
                                    {
                                        render.clear();
                                        drawDonePolygon();
                                        if (done)
                                            render.calcPolygon(center, radius, sidesN, antiAliasing);
                                        if (antiAliasing)
                                            {
                                                render.drawXiaolinWuLine(new Point(e.getX(), e.getY()), points.get(points.size() - 1));
                                                render.drawXiaolinWuLine(new Point(e.getX(), e.getY()), points.get(0));
                                            }
                                        else
                                            {
                                                render.drawLine(new Point(e.getX(), e.getY()), points.get(points.size() - 1), 0xFF0000);
                                                render.drawLine(new Point(e.getX(), e.getY()), points.get(0), 0xFF05000);
                                            }
                                    }
                                if (activeReg)
                                    {
                                        render.clear();
                                        drawDonePolygon();
                                        if (antiAliasing)
                                            render.drawXiaolinWuLine(center, new Point(e.getX(), e.getY()));
                                        else
                                            render.drawDDALine(center, new Point(e.getX(), e.getY()), 0xFF0000);
                                    }
                                if (activeRegDraw)
                                    {
                                        render.clear();
                                        drawDonePolygon();
                                        Point p = new Point(e.getX(), e.getY());
                                        sidesN = 3 + (int) p.getY() / 35;
                                        render.calcPolygon(center, radius, sidesN, antiAliasing);
                                    }
                            }


                    });
                canvas.addKeyListener(new KeyAdapter()
                    {
                        @Override
                        public void keyPressed(KeyEvent e)
                            {
                                if (e.getKeyCode() == 27)
                                    {
                                        activeIrr = false;
                                        render.clear();
                                        if (done)
                                            render.calcPolygon(center, radius, sidesN, antiAliasing);
                                        drawDonePolygon();
                                    }
                                if (e.getKeyCode() == 127)
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
                                        done = false;
                                    }
                                if (e.getKeyCode() == 32)
                                    {
                                        antiAliasing = !antiAliasing;
                                        drawDonePolygon();
                                    }
                                System.out.println(e.getKeyCode());

                            }
                    });
            }

        public static void main(String[] args)
            {
                SwingUtilities.invokeLater(PixelText::new);
            }

        public void drawDonePolygon()
            {
                render.drawPolygon(points, antiAliasing);
                if (points.size() > 2)
                    {
                        if (antiAliasing)
                            render.drawXiaolinWuLine(points.get(0), points.get(points.size() - 1));
                        else
                            render.drawLine(points.get(0), points.get(points.size() - 1), 0xFFFFFF);
                    }
            }
    }
