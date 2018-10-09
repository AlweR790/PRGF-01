package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Render
    {
        private BufferedImage img;
        private Canvas canvas;
        private static final int FPS  = 1000 / 144;

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
                                if(x < 1600 && x>0  && y > 0 && y<900)
                                    {
                                        drawPixel(Math.round(x), (int) y, color);
                                        System.out.println(x + "  :  " + y);
                                    }
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
                                if(x < 1600 && x>0  && y > 0 && y<900)
                                    {
                                        drawPixel((int)x, Math.round(y), color);
                                        System.out.println(x + "  :  " + y);
                                    }
                            }
                    }
            }
        public void clear() {
            Graphics g = img.getGraphics();
            g.setColor(Color.BLACK);
            g.clearRect(0, 0, 1600, 900);
        }

        public void drawDDALine(Point start, Point end, int color)
            {
                int x;
                float k, q, y;
                k = (end.getY() - start.getY()) / (end.getX()-start.getX());
                if(Math.abs(k) >= 1)
                    {
                        float x1;
                        q = 1/k;
                        x1 = start.getX();
                        y = (int) start.getY();

                        if(y> end.getY())
                            {
                                int temp = (int)x1;
                                x1= end.getX();
                                end.setX(temp);
                                temp = (int) y;
                                y = (int) end.getY();
                                end.setY(temp);
                            }
                        do
                            {
                                drawPixel(Math.round(x1), (int)y, 0x00ffff);
                                y+=1; x1+=q;
                            }
                        while (y<end.getY());

                    }
                else
                    {
                        k = (end.getY() - start.getY()) / (end.getX()-start.getX());
                        x = (int)start.getX();
                        y = (int)start.getY();
                        if (x > end.getX())
                            {
                                int temp = x;
                                x = (int) end.getX();
                                end.setX(temp);
                                temp = (int) y;
                                y = (int) end.getY();
                                end.setY(temp);
                            }

                        do
                            {
                                drawPixel(x, Math.round(y), 0x00ffff);
                                x += 1;
                                y += k;
                            } while (x < end.getX());
                    }

            }

    }
