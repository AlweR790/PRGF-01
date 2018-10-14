package main;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.abs;
import static java.lang.Math.floor;

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

		public void drawPixelBrightness(int x, int y, float bright)
			{
				img.setRGB(x, y, Color.HSBtoRGB(0, 0, 1-bright));
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
				float x0, x1, y0, y1, dx, dy, grad, xEnding, yEnding, xGap, xPixel1, yPixel1, xPixel2, yPixel2, interY;

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
				grad = dy/dx;
				if (grad == 0)
					grad = 1;

				xEnding = round(x0);
				yEnding = y0 + grad * (xEnding - x0);
				xGap = rfpart(x0 + 0.5f);
				xPixel1 = xEnding;
				yPixel1 = ipart(yEnding);
				if(steep)
					{
						drawPixelBrightness((int)yPixel1, (int)xPixel1, rfpart(yEnding)* xGap);
						drawPixelBrightness((int)yPixel1+1, (int)xPixel1, fpart(yEnding)* xGap);
					}
				else
					{
						drawPixelBrightness((int)xPixel1,(int) yPixel1, rfpart(yEnding)* xGap);
						drawPixelBrightness((int) xPixel1, (int) yPixel1 + 1, fpart(yEnding)*xGap );
					}
				interY = yEnding + grad;

				xEnding = round(x1);
				yEnding = y1 + grad * (xEnding - x1);
				xGap = fpart(x1 + 0.5f);
				xPixel2 = xEnding;
				yPixel2 = ipart(yEnding);

				if(steep)
					{
						drawPixelBrightness((int)yPixel2, (int) xPixel2, rfpart(yEnding)*xGap );
						drawPixelBrightness((int)yPixel2, (int) xPixel2+1, fpart(yEnding)*xGap );
					}
				else
					{
						drawPixelBrightness((int) xPixel2, (int) yPixel2, rfpart(yEnding)*xGap);
						drawPixelBrightness((int) xPixel2, (int) yPixel2+1, fpart(xEnding)*xGap);
					}

				for(float x = xPixel1 + 1; x <= xPixel2 -1; x++)
					{
						if(steep)
							{
								drawPixelBrightness(ipart(interY),(int) x, rfpart(interY));
								drawPixelBrightness(ipart(interY)+1, (int)x, fpart(interY));
							}
						else
							{
								drawPixelBrightness((int)x, ipart(interY), rfpart(interY));
								drawPixelBrightness((int)x, ipart(interY), rfpart(interY));
							}
						interY+= grad;
					}


			}


		public int ipart(float x)
			{
				return (int) floor(x);
			}

		public float round(float x)
			{
				return ipart(x + 0.5f);
			}

		public float fpart(float x)
			{
				return x - (float) floor(x);
			}

		public float rfpart(float x)
			{
				return 1 - fpart(x);
			}


	}
