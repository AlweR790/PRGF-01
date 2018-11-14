package main.image;

import java.awt.image.BufferedImage;

public class Pattern implements IPattern
    {
        private int r = 0xFF0000;
        private int g = 0x00FF00;
        private int b = 0x0000FF;

        private int lengthX;
        private int lengthY;
        private BufferedImage bImage;

        public void getImageFromPattern()
            {
                BufferedImage image = new BufferedImage(lengthX, lengthY, BufferedImage.TYPE_INT_RGB);
                for (int i = 0; i < lengthX; i++)
                    {
                        for (int j = 0; j < lengthY; j++)
                            {
                                image.setRGB(i, j, pattern[j][i]);
                            }
                    }
                bImage = image;
            }

        private int[][] pattern =
                {
                        {b, b, b, b, b, b},
                        {b, b, b, b, b, b},
                        {b, r, r, r, r, b},
                        {b, g, g, g, g, b},
                        {b, r, r, r, r, b},
                        {b, g, g, g, g, b}
                };


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
            }

    }
