package main.app;

import main.geometryObjects.Point;
import main.render.Render;

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
        private static final int width = 1900;
        private static final int height = 1000;
        private Canvas canvas;
        private BufferedImage img;
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
        private JLabel statusBar;

        public PixelText()
            {
                window = new JFrame();
                window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                window.setSize(width, height);
                window.setTitle("PRGF Ukol 1");
                window.setLocationRelativeTo(null);
                img = new BufferedImage(window.getWidth(), window.getHeight(), BufferedImage.TYPE_INT_RGB);
                canvas = new Canvas();
                render = new Render(img, canvas);
                window.add(canvas);
                window.setVisible(true);
                window.setResizable(false);
                statusBar = new JLabel();
                statusBar.setOpaque(false);
                statusBar.setText("Antialiasing: " + antiAliasing + "  Press SPACE to switch, ESC to stop drawing and DEL to start again, RMB for Irregular Polynome, LMB for Regular Polynome");
                window.add(statusBar, BorderLayout.SOUTH);
                canvas.addMouseListener(new MouseAdapter()
                    {
                        @Override
                        public void mouseClicked(MouseEvent mouseEvent)
                            {
                                if (SwingUtilities.isLeftMouseButton(mouseEvent))
                                    {
                                        if (mouseEvent.isAltDown())
                                            render.seedFill.init(mouseEvent.getX(), mouseEvent.getY(), 0x0000FF);
                                        else
                                            {
                                                render.clear();
                                                Point p = new Point(mouseEvent.getX(), mouseEvent.getY());
                                                points.add(p);
                                                render.drawPolygon(points, antiAliasing, 0, 0xFFFFFF);
                                                render.drawPixel((int) p.getX(), (int) p.getY(), 0xFFFFFF);
                                                if (points.size() > 2)
                                                    {
                                                        if (antiAliasing)
                                                            render.drawXiaolinWuLine(points.get(0), points.get(points.size() - 1), 0);
                                                        else
                                                            render.drawLine(points.get(0), points.get(points.size() - 1), 0xFFFFFF);
                                                    }
                                                activeIrr = true;
                                                if (done)
                                                    render.calcPolygon(center, radius, sidesN, antiAliasing, 0, 0xFFFFFF);
                                            }
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
                                                    activeRegDraw = false;
                                                    render.clear();
                                                    sides = new Point(mouseEvent.getX(), mouseEvent.getY());
                                                    sidesN = 3 + (int) sides.getY() / 35;
                                                    done = true;
                                                    drawDonePolygon();
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
                                            render.calcPolygon(center, radius, sidesN, antiAliasing, 0, 0xFFFFFF);
                                        if (antiAliasing)
                                            {
                                                render.drawXiaolinWuLine(new Point(e.getX(), e.getY()), points.get(points.size() - 1), 1);
                                                render.drawXiaolinWuLine(new Point(e.getX(), e.getY()), points.get(0), 1);
                                            }
                                        else
                                            {
                                                render.drawLine(new Point(e.getX(), e.getY()), points.get(points.size() - 1), 0xFF0000);
                                                render.drawLine(new Point(e.getX(), e.getY()), points.get(0), 0xFF0000);
                                            }
                                    }
                                if (activeReg)
                                    {
                                        render.clear();
                                        drawDonePolygon();
                                        render.calcPolygon(center, new Point(e.getX(), e.getY()), 3, antiAliasing, 1, 0xFF0000);
                                        if (antiAliasing)
                                            render.drawXiaolinWuLine(center, new Point(e.getX(), e.getY()), 1);
                                        else
                                            render.drawDDALine(center, new Point(e.getX(), e.getY()), 0xFF0000);

                                    }
                                if (activeRegDraw)
                                    {
                                        render.clear();
                                        drawDonePolygon();
                                        Point p = new Point(e.getX(), e.getY());
                                        sidesN = 3 + (int) p.getY() / 35;
                                        render.calcPolygon(center, radius, sidesN, antiAliasing, 1, 0xFF0000);
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
                                        activeReg = false;
                                        activeRegDraw = false;

                                        render.clear();
                                        if (done)
                                            render.calcPolygon(center, radius, sidesN, antiAliasing, 0, 0xFFFFFF);
                                        else
                                            {
                                                center = null;
                                                radius = null;
                                                clicks = 0;
                                            }

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
                                        render.clear();
                                        drawDonePolygon();
                                        statusBar.setText("Antialiasing: " + antiAliasing + "  Press SPACE to switch, ESC to stop drawing and DEL to start again, RMB for Irregular Polynome, LMB for Regular Polynome");
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
                if (done)
                    render.calcPolygon(center, radius, sidesN, antiAliasing, 0, 0xFFFFFF);
                render.drawPolygon(points, antiAliasing, 0, 0xFFFFFF);
                if (points.size() > 2)
                    {
                        if (antiAliasing)
                            render.drawXiaolinWuLine(points.get(0), points.get(points.size() - 1), 0);
                        else
                            render.drawLine(points.get(0), points.get(points.size() - 1), 0xFFFFFF);
                    }
            }
    }
