package main.geometryObjects;

public class Point
    {
        private float X, Y, brightness;

        public Point(float x, float y)
            {
                X = x;
                Y = y;
            }

        public Point(float x, float y, float brightness)
            {
                X = x;
                Y = y;
                this.brightness = brightness;
            }

        public Point(int x, int y)
            {
                X = (float) x;
                Y = (float) y;
            }

        public Point()
            {

            }

        public float getX()
            {
                return X;
            }

        public void setX(float x)
            {
                X = x;
            }

        public float getY()
            {
                return Y;
            }

        public void setY(float y)
            {
                Y = y;
            }

        public boolean equals(Point p2)
            {
                return (X == p2.getX() && Y == p2.getY());
            }

        @Override
        public String toString()
            {
                return "Point{" +
                        "X=" + X +
                        ", Y=" + Y +
                        '}';
            }

        public float getBrightness()
            {
                return brightness;
            }

        public void setBrightness(float brightness)
            {
                this.brightness = brightness;
            }
    }
