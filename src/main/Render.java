package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Render
    {
        private BufferedImage img;
        private Canvas canvas;
        private static final int FPS  = 1000 / 30;

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
                                    }
                            }
                    }
            }
        public void clear() {
            Graphics g = img.getGraphics();
            g.setColor(Color.BLACK);
            g.clearRect(0, 0, 1600, 900);
        }


        public void drawIPolygon(List<Point> points)
            {
                System.out.println(points.get(points.size()-1).toString() + " to " + points.get(0).toString());
                for(int i = 0; i < points.size()-1; i++)
                    {
                        System.out.println(points.get(i).toString()+ "  " + i);
                        Point p1 = new Point(points.get(i).getX(), points.get(i).getY());
                        Point p2 = new Point(points.get(i+1).getX(), points.get(i+1).getY());
                        drawDDALine(p1,p2, 0xFFFFFF);
                    }
                    if(points.size()>1)
                        {
                           drawDDALine(points.get(points.size()-1), points.get(0), 0xFFFFFF);
                        }
                System.out.println("____________");
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
                                drawPixel(Math.round(x1), (int)y, color);
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
                                drawPixel(x, Math.round(y), color);
                                x += 1;
                                y += k;
                            } while (x < end.getX());
                    }

            }

    }
