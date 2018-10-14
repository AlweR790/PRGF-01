package main;

import java.util.ArrayList;
import java.util.List;

public class XiaolinWuLine
	{
		private double x0,x1,y0,y1, dx, dy, grad, xEnd, yEnd, xGap, xPixel1, yPixel1, xPixel2, yPixel2, interY ;

		public XiaolinWuLine()
			{

			}
		private int ipart(double x)
			{
				return (int)x;
			}
		private double fpart(double x)
			{
				return x - Math.floor(x);
			}
		private double rfpart(double x)
			{
				return 1 - fpart(x);
			}
		public List<Point> drawLine(Point start, Point end)
			{
				return drawLine(start.getX(), start.getY(), end.getX(), end.getY());
			}
		public List<Point> drawLine(double x0, double y0, double  x1, double y1)
			{
				List<Point> points = new ArrayList<>();
				boolean uprising = Math.abs(y1 - y0) > Math.abs(x1-x0);
				if(uprising)
					drawLine(y0,x0, y1, x1);
				if(x0> x1)
					drawLine(x1, y1, x0, y0);
				dx = x1 - x0;
				dy =  y1 - y0;
				grad = dy / dx;

				xEnd = Math.round(x0);
				yEnd = y0 + (grad * (xEnd - x0));
				xGap = rfpart(x0 + 0.5);
				xPixel1 = xEnd;
				yPixel1 = ipart(yEnd);

				if(uprising)
					{
						points.add(new Point((float)yPixel1,(float) xPixel1, (float)(rfpart(yEnd)*xGap)));
						points.add(new Point((float)yPixel1+1,(float) xPixel1, (float)(fpart(yEnd)*xGap)));
					}
				else
					{
						points.add(new Point((float)xPixel1,(float) yPixel1, (float)(rfpart(yEnd)*xGap)));
						points.add(new Point((float)xPixel1,(float) yPixel1+1, (float)(fpart(yEnd)*xGap)));
					}
				interY = yEnd + grad;

				xEnd = Math.round(x1);
				yEnd = y1 + (grad * (xEnd - x1));
				xGap = fpart(x1+0.5);
				xPixel2 = xEnd;
				yPixel2 = ipart(yEnd);

				if(uprising)
					{
						points.add(new Point((float) yPixel2, (float) xPixel2, (float)(rfpart(yEnd)*xGap)));
						points.add(new Point((float) yPixel2+1, (float) xPixel2, (float)(rfpart(yEnd)*xGap)));
					}
				else
					{
						points.add(new Point((float) yPixel2, (float) xPixel2, (float)(rfpart(yEnd)*xGap)));
						points.add(new Point((float) xPixel2, (float) yPixel2+1, (float)(rfpart(yEnd)*xGap)));
					}
				for(double x = xPixel1+1; x <= xPixel2 - 1; x++)
					{
						if (uprising)
							{
								points.add(new Point(ipart(interY), (float) x, (float) rfpart(interY)));
								points.add(new Point(ipart(interY) + 1, (float) x, (float) fpart(interY)));
							} else
							{
								points.add(new Point((float) x, ipart(interY), (float) rfpart(interY)));
								points.add(new Point((float) x, ipart(interY) + 1, (float) fpart(interY)));
							}
						interY += grad;
					}
				return points;
			}
	}
