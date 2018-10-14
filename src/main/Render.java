package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Render
    {
        private BufferedImage img;
        private Canvas canvas;
        private static final int FPS = 1000 / 100;

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
        public void drawPixelBrightness (double x, double y, double bright)
            {
                img.setRGB((int)x, (int)y, Color.HSBtoRGB(0,0,(float)bright*100));
            }

        public void drawLine(Point first, Point second, int color)
            {
                if (first.equals(second))
                    {
                        drawPixel((int) first.getX(), (int) first.getY(), color);
                        return;
                    }
                float k = (second.getY() - first.getY()) / (second.getX() - first.getX());
                float q = first.getY() - k * first.getX();
                if (Math.abs(k) >= 1)
                    {
                        if (first.getY() > second.getY())
                            {
                                Point temp = first;
                                first = second;
                                second = temp;
                            }

                        k = (second.getX() - first.getX()) / (second.getY() - first.getY());
                        q = first.getX() - k * first.getY();
                        for (float y = first.getY(); y <= second.getY(); y++)
                            {
                                float x = k * y + q;
                                if (x < 1600 && x > 0 && y > 0 && y < 900)
                                    {
                                        drawPixel(Math.round(x), (int) y, color);
                                    }
                            }
                    } else
                    {
                        if (first.getX() > second.getX())
                            {
                                Point temp = first;
                                first = second;
                                second = temp;
                            }
                        for (float x = first.getX(); x <= second.getX(); x++)
                            {
                                float y = k * x + q;
                                if (x < 1600 && x > 0 && y > 0 && y < 900)
                                    {
                                        drawPixel((int) x, Math.round(y), color);
                                    }
                            }
                    }
            }

        public void clear()
            {
                Graphics g = img.getGraphics();
                g.setColor(Color.BLACK);
                g.clearRect(0, 0, 1600, 900);
            }


        public void drawPolygon(List<Point> points)
            {
                for (int i = 0; i < points.size() - 1; i++)
                    {
                        Point p1 = new Point(points.get(i).getX(), points.get(i).getY());
                        Point p2 = new Point(points.get(i + 1).getX(), points.get(i + 1).getY());

                        drawDDALine(p1, p2, 0xFFFFFF);
                    }
            }

        public void calcPolygon(Point center, Point radius, int sidesN)
            {
                List<Point> poylgonPoints = new ArrayList<>();
                float circleRadius = distance(center, radius);
                float angle = 2 * (float) Math.PI / (float) sidesN;
                float startAngle = (float) Math.atan2(radius.getY() - center.getY(), radius.getX() - center.getX());
                for (int i = 1; i <= sidesN; i++)
                    {
                        Point p = new Point();
                        p.setX(center.getX() + (float) Math.cos(startAngle + angle * i) * circleRadius);
                        p.setY(center.getY() + (float) Math.sin(startAngle + angle * i) * circleRadius);
                        poylgonPoints.add(p);
                    }
                drawPolygon(poylgonPoints);
                drawDDALine(poylgonPoints.get(poylgonPoints.size() - 1), poylgonPoints.get(0), 0xFFFFFF);
            }

        public float distance(Point center, Point radius)
            {
                return (float) Math.sqrt(Math.pow((radius.getX() - center.getX()), 2) + Math.pow((radius.getY() - center.getY()), 2));
            }


        public void drawDDALine(Point start, Point end, int color)
            {
                int x;
                float k, q, y;
                k = (end.getY() - start.getY()) / (end.getX() - start.getX());
                if (Math.abs(k) >= 1)
                    {
                        float x1;
                        q = 1 / k;
                        x1 = start.getX();
                        y = (int) start.getY();

                        if (y > end.getY())
                            {
                                int temp = (int) x1;
                                x1 = end.getX();
                                end.setX(temp);
                                temp = (int) y;
                                y = (int) end.getY();
                                end.setY(temp);
                            }
                        do
                            {
                                drawPixel(Math.round(x1), (int) y, color);
                                y += 1;
                                x1 += q;
                            }
                        while (y < end.getY());

                    } else
                    {
                        k = (end.getY() - start.getY()) / (end.getX() - start.getX());
                        x = (int) start.getX();
                        y = (int) start.getY();
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

        public void drawXiaolinWuLine(Point start, Point end)
            {
/*                List<Point> points;
                XiaolinWuLine line = new XiaolinWuLine();
                points = line.drawLine(start, end);

                for (Point p: points)
                    {
                        img.setRGB((int)p.getX(),(int)p.getY(), Color.HSBtoRGB(0,0, p.getBrightness()));
                    }*/
                boolean up = Math.abs(end.getY()-start.getY()) > Math.abs(end.getX()-start.getX());
                double x0 = start.getX();
                double x1 = end.getX();
                double y0 = start.getY();
                double y1 = end.getY();

                if(up)
                    {
                        double temp = x0;
                        x0 = y0;
                        y0 = temp;

                        temp = x1;
                        x1 = y1;
                        y1 = temp;
                    }
                if(x0>x1)
                    {
                        double temp = x0;
                        x0 = x1;
                        x1 =  temp;

                        temp = y0;
                        y0 = y1;
                        y1 = temp;
                    }
                double dx = x1 - x0;
                double dy = y1 - x0;
                double grad = dy/dx;
                if(grad == 0)
                    grad = 1;

                double xEnd = round(x0);
                double yEnd = y0 + grad * (xEnd - x0);
                double xGap = rfpart(x0 + 0.5);
                double xPxl1 = xEnd;
                double yPxl1 = ipart(yEnd);
                if(up)
                    {
                       drawPixelBrightness(yPxl1, xPxl1, rfpart(yEnd) * xGap);
                       drawPixelBrightness(yPxl1+1, xPxl1, fpart(yEnd)*xGap);
                    }
                else
                    {
                        drawPixelBrightness(xPxl1, yPxl1, rfpart(yEnd) * xGap);
                        drawPixelBrightness(xPxl1, yPxl1+1, rfpart(yEnd) * xGap);
                    }
                double intery = yEnd + grad;

                xEnd = round(x1);
                yEnd = y1+grad*(xEnd-x1);
                xGap = fpart(x1 + 0.5);
                double xPxl2 = xEnd;
                double yPxl2 = ipart(yEnd);
                up = !up;
                if(!up)
                    {
                        drawPixelBrightness(yPxl2, xPxl2, rfpart(yEnd) * xGap);
                        drawPixelBrightness(yPxl2+1, xPxl2, fpart(yEnd)*xGap);
                    }
                else
                    {
                        drawPixelBrightness(xPxl2, yPxl2, rfpart(yEnd) * xGap);
                        drawPixelBrightness(xPxl2, yPxl2+1, rfpart(yEnd) * xGap);
                    }
                if(up)
                    {
                        for(double x = xPxl1 + 1; x <= xPxl2-1; x++ )
                            {
                                drawPixelBrightness(ipart(intery), x, rfpart(intery));
                                drawPixelBrightness(ipart(intery) +1, x, fpart(intery));
                                intery += grad;
                            }
                    }
                else
                    {
                        for(double x = xPxl1 + 1; x <= xPxl2-1; x++)
                            {
                                drawPixelBrightness(x, ipart(intery), rfpart(intery));
                                drawPixelBrightness(x, ipart(intery)+1, fpart(intery));
                                intery += grad;
                            }
                    }


            }
        public double ipart(double x)
            {
                return Math.floor(x);
            }
        public double round(double x)
            {
                return ipart(x + 0.5);
            }
        public double fpart (double x)
             {
                 return x - Math.floor(x);
             }
        public double rfpart(double x)
            {
                return 1-fpart(x);
            }




    }
