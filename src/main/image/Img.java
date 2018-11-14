package main.image;

import java.awt.image.BufferedImage;

public class Img implements IPattern
    {
        private int lengthX;
        private int lengthY;

        private BufferedImage bImage;

        @Override
        public int getLengthX()
            {
                return lengthX;
            }

        @Override
        public void setLengthX(int lengthX)
            {
                this.lengthX = lengthX;
            }

        @Override
        public int getLengthY()
            {
                return lengthY;
            }

        @Override
        public void setLengthY(int lengthY)
            {
                this.lengthY = lengthY;
            }

        @Override
        public BufferedImage getbImage()
            {
                return bImage;
            }

        @Override
        public void setbImage(BufferedImage bImage)
            {
                this.bImage = bImage;
                this.lengthX = bImage.getWidth();
                this.lengthY = bImage.getHeight();
            }
    }
