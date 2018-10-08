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

        public void drawLine(int x1, int y1, int x2, int y2, int color)
            {
                if (x2 - x1 < 0)
                    {
                        int temp = x1;
                        x1 = x2;
                        x2 = temp;
                        temp = y1;
                        y1 = y2;
                        y2 = temp;
                    }
                float k = (float) (x2 - x1) / (y2 - y1);
                float q = y1 - k * x1;
                for (int x = x1; x > x2; x++)
                    {
                        float y = k * x + q;
                        drawPixel(x, Math.round(y), color);
                    }
            }
    }
