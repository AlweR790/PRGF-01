package main.fill;

import main.image.IPattern;

import java.awt.image.BufferedImage;

public class SeedFill implements IFiller
    {
        private BufferedImage img;
        private int x, y, color, background;
        private boolean fillerOn = false;
        private IPattern filler;


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


        public void init(int x, int y, int color, boolean fillerOn, IPattern filler)
            {
                this.x = x;
                this.y = y;
                this.color = color;
                this.background = img.getRGB(x, y);
                this.fillerOn = fillerOn;
                this.filler = filler;

                fill();
                System.out.println("Done");
            }

        private void seed(int ax, int ay)
            {
                if (background == img.getRGB(ax, ay) && ax > 0 && ax < img.getWidth() - 1 && ay > 0 && ay < img.getHeight() - 1)
                    {
                        if (fillerOn)
                            {
                                int colorPattern = filler.getbImage().getRGB(ax % filler.getLengthX(), ay % filler.getLengthY());
                                img.setRGB(ax, ay, colorPattern);
                            }
                        else
                            img.setRGB(ax, ay, color);

                        seed(ax + 1, ay);
                        seed(ax, ay + 1);
                        seed(ax - 1, ay);
                        seed(ax, ay - 1);
                    }

            }


    }