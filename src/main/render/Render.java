package main.render;


import main.geometry.Line;
import main.geometry.Point;
import main.image.IPattern;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.abs;


public class Render
    {

        private static final Render instance = new Render(); //Singleton
        private BufferedImage img;
        private Canvas canvas;
        private static final int FPS = 1000 / 100;


        private Render()
            {
            }

        public void inicialize(BufferedImage img, Canvas canvas)
            {
                this.img = img;
                this.canvas = canvas;
                setLoop();
            }

        private void setLoop()
            {
                new Timer().schedule(new TimerTask()
                    {
                        @Override
                        public void run()
                            {
                                try
                                    {
                                        canvas.getGraphics().drawImage(img, 0, 0, null);   //Po založení projectu na Linuxu se občas bugne na Win, proto try chatch
                                    }
                                catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                            }
                    }, 100, FPS);
            }

        public void drawPixel(int x, int y, int color)
            {
                if (x > 0 && y > 0 && x < canvas.getWidth() && y < canvas.getHeight())
                    img.setRGB(x, y, color);
            }

        private void drawPixelBrightness(int x, int y, float bright, float saturation)
            {
                if (x > 0 && y > 0 && x < canvas.getWidth() && y < canvas.getHeight())
                    img.setRGB(x, y, Color.HSBtoRGB(0, saturation, bright));

            }

        public void drawLine(Point first, Point second, int color, boolean patternOn, IPattern pattern)
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
                                if (x < img.getWidth() && x > 0 && y > 0 && y < img.getHeight())
                                    {
                                        if (patternOn)
                                            drawPixel(Math.round(x), (int) y, pattern.getbImage().getRGB((int) x % pattern.getLengthX(), (int) y % pattern.getLengthX()));
                                        else
                                            drawPixel(Math.round(x), (int) y, color);
                                    }
                            }
                    }
                else
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
                                if (x < img.getWidth() && x > 0 && y > 0 && y < img.getHeight())
                                    {

                                        if (patternOn)
                                            drawPixel(Math.round(x), (int) y, pattern.getbImage().getRGB((int) x % pattern.getLengthX(), (int) y % pattern.getLengthX()));
                                        else
                                            drawPixel(Math.round(x), (int) y, color);
                                    }
                            }
                    }
            }

        public void clear()
            {
                Graphics g = img.getGraphics();
                g.setColor(Color.black);
                g.clearRect(0, 0, img.getWidth(), img.getHeight());
            }


        public void drawPolygon(List<Point> points, boolean antiAliasing, float saturation, int color)
            {
                if (points.isEmpty())
                    return;
                for (int i = 0; i < points.size() - 1; i++)
                    {
                        Point p1 = new Point(points.get(i).getX(), points.get(i).getY());
                        Point p2 = new Point(points.get(i + 1).getX(), points.get(i + 1).getY());

                        if (antiAliasing)
                            drawXiaolinWuLine(p1, p2, saturation);
                        else
                            drawDDALine(p1, p2, color);
                    }
                if (antiAliasing)
                    drawXiaolinWuLine(points.get(0), points.get(points.size() - 1), saturation);
                else
                    drawLine(points.get(0), points.get(points.size() - 1), color, false, null);


            }

      /*  public void calcPolygon(Point center, Point radius, int sidesN, boolean antiAliasing, float saturation, int color)
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
                drawPolygon(poylgonPoints, antiAliasing, saturation, color);
                if (antiAliasing)
                    drawXiaolinWuLine(poylgonPoints.get(poylgonPoints.size() - 1), poylgonPoints.get(0), saturation);
                else
                    drawDDALine(poylgonPoints.get(poylgonPoints.size() - 1), poylgonPoints.get(0), color);
            }


        private float distance(Point center, Point radius)
            {
                return (float) Math.sqrt(Math.pow((radius.getX() - center.getX()), 2) + Math.pow((radius.getY() - center.getY()), 2));
            }
*/

        private void drawDDALine(Point start, Point end, int color)
            {
                int x;
                float k, q, y;
                k = (end.getY() - start.getY()) / (end.getX() - start.getX());
                if (abs(k) >= 1)
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

                    }
                else
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

        public void drawXiaolinWuLine(Point start, Point end, float saturation)
            {

                float x0, x1, y0, y1, dx, dy, grad;
                x0 = start.getX();
                x1 = end.getX();

                y0 = start.getY();
                y1 = end.getY();

                boolean steep = abs(y1 - y0) > abs(x1 - x0);

                if (steep)
                    {
                        float temp = x0;
                        x0 = y0;
                        y0 = temp;

                        temp = x1;
                        x1 = y1;
                        y1 = temp;
                    }
                if (x0 > x1)
                    {
                        float temp = x0;
                        x0 = x1;
                        x1 = temp;

                        temp = y0;

                        y0 = y1;
                        y1 = temp;
                    }
                dx = x1 - x0;
                dy = y1 - y0;
                grad = dy / dx;
                float y = y0 + grad;
                for (float x = x0; x <= x1; x++)
                    {
                        if (steep)
                            {
                                drawPixelBrightness((int) y, (int) x, 1 - (y - (int) y), saturation);
                                drawPixelBrightness((int) y + 1, (int) x, y - (int) y, saturation);
                            }
                        else
                            {
                                drawPixelBrightness((int) x, (int) y, 1 - (y - (int) y), saturation);
                                drawPixelBrightness((int) x, (int) y + 1, y - (int) y, saturation);
                            }
                        y += grad;
                    }
            }

        public List<Point> clip(List<Point> polygonPoints, List<Point> clipPoints)
            {
                List<Point> input;
                List<Line> edges = new ArrayList<>();

                for (int i = 0; i < clipPoints.size(); i++)
                    {
                        edges.add(new Line(clipPoints.get(i), clipPoints.get((i + 1) % clipPoints.size())));
                    }

                for (Line edge : edges)
                    {

                        input = new ArrayList<>(polygonPoints);
                        polygonPoints.clear();
                        if (!input.isEmpty())
                            {
                                Point v1 = input.get(input.size() - 1);
                                for (Point v2 : input)
                                    {
                                        if (edge.isInside(v2))
                                            {
                                                if (!edge.isInside(v1))
                                                    polygonPoints.add(edge.intersection(v1, v2));
                                                polygonPoints.add(v2);
                                            }
                                        else
                                            {
                                                if (edge.isInside(v1))
                                                    polygonPoints.add(edge.intersection(v1, v2));
                                            }
                                        v1 = v2;
                                    }
                            }
                    }

                return polygonPoints;
            }

        public static Render getInstance()
            {
                return instance;
            }

    }
