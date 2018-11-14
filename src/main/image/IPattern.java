package main.image;

import java.awt.image.BufferedImage;

public interface IPattern
    {
        int getLengthX();

        void setLengthX(int lengthX);

        int getLengthY();

        void setLengthY(int lengthY);

        BufferedImage getbImage();

        void setbImage(BufferedImage bImage);
    }
