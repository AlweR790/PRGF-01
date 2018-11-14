package main.fill;

import main.geometry.Line;
import main.geometry.Point;
import main.image.IPattern;
import main.render.Render;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanLine implements IFiller
    {
        private List<Point> points;
        private Render r;
        private int color;
        private boolean fillerOn;
        private IPattern filler;


        public ScanLine(List<Point> points, Render r, int color, boolean patternOn, IPattern filler)
            {
                this.points = points;
                this.r = r;
                this.color = color;
                this.fillerOn = patternOn;
                this.filler = filler;
            }

        @Override
        public void setBufferedImage(BufferedImage img)
            {

            }

        @Override
        public void fill()
            {

            }


        public void init()
            {
                List<Line> edges = new ArrayList<>();
                List<Integer> inter = new ArrayList<>();
                float yMax = points.get(0).getY();
                float yMin = points.get(0).getY();
                for (int i = 0; i < points.size(); i++)
                    {
                        if (points.get(i).Y > yMax)
                            {
                                yMax = points.get(i).Y;
                            }
                        if (points.get(i).Y < yMin)
                            {
                                yMin = points.get(i).Y;
                            }

                        if (i + 1 < points.size())
                            {
                                Line l = new Line(points.get(i), points.get(i + 1));
                                l.orientate();
                                if (l.isntHorizontal())
                                    edges.add(l);
                            }


                    }

                Line l = new Line(points.get(0), points.get(points.size() - 1));
                l.orientate();
                if (l.isntHorizontal())
                    edges.add(l);


                for (float y = yMin; y <= yMax; y++)
                    {
                        for (Line line : edges)
                            {
                                if (line.isIntersection(y))
                                    inter.add(line.intersection(y));
                            }

                        Collections.sort(inter);

                        for (int i = 0; i < inter.size(); i += 2)
                            {
                                if (!fillerOn)
                                    r.drawLine(new Point(inter.get(i), y), new Point(inter.get((i + 1) % inter.size()), y), color, false, null);
                                else
                                    r.drawLine(new Point(inter.get(i), y), new Point(inter.get((i + 1) % inter.size()), y), color, true, filler);

                            }
                        inter.clear();
                    }


            }
    }
