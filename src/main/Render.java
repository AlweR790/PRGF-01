package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Render
    {
        private BufferedImage img;
        private Canvas canvas;
        private static final int FPS = 1000 / 30;

        public Render(BufferedImage img, Canvas canvas)
            {
                this.canvas = canvas;
                this.img = img;
                setLoop();
            }

        private void setLoop()
            {
                new Timer().schedule(new TimerTask()
                    {
                        @Override
                        public void run()
                            {
                                canvas.getGraphics().drawImage(img, 0, 0, null);
                            }
                    }, 100, FPS);
            }

        public void drawPixel(int x, int y, int color)
            {
                img.setRGB(x, y, color);
            }

        public void drawLine(Point first, Point second, int color)
            {
                if(first.equals(second))
                    {
                        drawPixel((int)first.getX(), (int)first.getY(), color);
                        return;
                    }
                float k = (second.getY() - first.getY()) / (second.getX() - first.getX());
                float q = first.getY() - k * first.getX();
                if(Math.abs(k) >= 1)
                    {
                        if(first.getY() > second.getY())
                            {
                                Point temp = first;
                                first = second;
                                second = temp;
                            }

                        k = (second.getX() - first.getX()) / (second.getY() - first.getY());
                        q = first.getX() - k* first.getY();
                        for (float y = first.getY(); y <= second.getY() ; y++)
                            {
                                float x = k*y+q;
                                if(x > 1600 || x<0)
                                    return;
                                drawPixel(Math.round(x), (int)y, color );
                                System.out.println(y);
                            }
                    }
                else
                    {
                        if(first.getX()> second.getX())
                            {
                                Point temp = first;
                                first = second;
                                second = temp;
                            }
                        for (float x = first.getX(); x <= second.getX(); x++)
                            {
                                float y = k * x + q;
                                if(y > 900 || y < 0)
                                    return;
                                drawPixel((int)x, Math.round(y), color);
                                System.out.println(y);
                            }
                    }
            }
        public void clear() {
            Graphics g = img.getGraphics();
            g.setColor(Color.BLACK);
            g.clearRect(0, 0, 1600, 900);
        }

    }
