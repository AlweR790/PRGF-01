
package main.geometry;

public class Line
    {
        private int x0;
        private int x1;
        private int y0;

        private int y1;
        private float q;
        private float k;

        public Line(Point p1, Point p2)
            {
                this.x0 = (int) p1.getX();
                this.x1 = (int) p2.getX();
                this.y0 = (int) p1.getY();
                this.y1 = (int) p2.getY();

            }

        private void swap()
            {
                int tmp = this.x0;
                this.x0 = this.x1;
                this.x1 = tmp;
                tmp = this.y1;
                this.y1 = this.y0;
                this.y0 = tmp;
            }


        public void orientate()
            {
                if (this.y1 < this.y0)
                    {
                        this.swap();
                    }

                this.getKQ();
            }

        public boolean isIntersection(float y)
            {
                return y >= (float) this.y0 && y < (float) this.y1;
            }

        public int intersection(float y)
            {
                return Math.round(this.k * y + this.q);
            }

        public boolean isntHorizontal()
            {
                return this.y0 != this.y1;
            }

        public boolean isInside(Point p)
            {
                int x = (int) p.X;
                int y = (int) p.Y;
                float f = (float) ((x - this.x0) * (this.y0 - this.y1) + (y - this.y0) * (this.x1 - this.x0));
                return f <= 0;
            }

        public Point intersection(Point v1, Point v2)
            {

                float x = ((v1.X * v2.Y - v1.Y * v2.X) * (float) (this.x0 - this.x1) - (float) (this.x0 * this.y1 - this.y0 * this.x1) * (v1.X - v2.X)) / ((v1.X - v2.X) * (float) (this.y0 - this.y1) - (float) (this.x0 - this.x1) * (v1.Y - v2.Y));
                float y = ((v1.X * v2.Y - v1.Y * v2.X) * (float) (this.y0 - this.y1) - (float) (this.x0 * this.y1 - this.y0 * this.x1) * (v1.Y - v2.Y)) / ((v1.X - v2.X) * (float) (this.y0 - this.y1) - (float) (this.x0 - this.x1) * (v1.Y - v2.Y));
                return new Point(x, y);
            }

        private void getKQ()
            {
                float dx = (float) (this.x1 - this.x0);
                float dy = (float) (this.y1 - this.y0);
                this.k = dx / dy;
                this.q = (float) this.x0 - this.k * (float) this.y0;
            }
    }
