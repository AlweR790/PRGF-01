package main;

public class Point
    {
        private float X, Y;

        public Point(float x, float y)
            {
                X = x;
                Y = y;
            }
        public Point(int x, int y)
            {
                X = (float)x;
                Y = (float)y;
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
    }
