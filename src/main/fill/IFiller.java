package main.fill;

import java.awt.image.BufferedImage;

public interface IFiller
    {
        void setBufferedImage(BufferedImage img);

        void fill();
    }
