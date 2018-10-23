package main.FIll;

import java.awt.image.BufferedImage;

public class SeedFill implements IFiller
    {
        private BufferedImage img;
        private int x, y, color, background;

        @Override
        public void setBufferedImage(BufferedImage img)
            {
                this.img = img;
            }

        @Override
        public void fill()
            {
                seed(x, y);
            }

        @Override
        public void setBackgroundColor()
            {

            }

        public void init(int x, int y, int color)
            {
                this.x = x;
                this.y = y;
                this.color = color;
                this.background = img.getRGB(x, y);
                fill();
            }

        public void seed(int ax, int ay)
            {
                if (background == img.getRGB(ax, ay) && ax >= 0 && ax < img.getWidth() && ay >= 0 && ay < img.getHeight())
                    {
                        img.setRGB(ax, ay, color);

                        seed(ax + 1, ay);
                        seed(ax, ay + 1);
                        seed(ax - 1, ay);
                        seed(ax, ay - 1);
                    }
            }
    }