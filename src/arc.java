import java.awt.Point;

public class arc {
	Point point1;
	Point point2;
	Point center;
	double cx;
	double cy;
	double ap; //the original a
	double a;
	double b;
	double amplitude;
	double drawback;
	
	public arc (Point p1, Point p2) {
		point1 = p1;
		point2 = p2;
		ap = Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
		cx = (p1.x - p2.x)/2.0 + p2.x;
		cy = (p1.y - p2.y)/2.0 + p2.y;
		center = new Point((int) cx, (int) cy);
		amplitude = 0;
		drawback = 0;
		resize();
	}
	
	public void setAmp(double newAmp) {
//		if(newAmp <= 0) return;
		amplitude = newAmp;
		if(drawback < 0 && Math.abs(amplitude) <= drawback * -2.0) drawback = Math.abs(amplitude) / -2.0 + 1.0;
		resize();
	}
	
	public void setBack(double newBack) {
		if(newBack * -1 >= Math.abs(amplitude / 2.0)) return;
		drawback = newBack;
		resize();
	}
	
	public void resize() {
		b = Math.abs(amplitude) + drawback;
		double newA = (2 * ap)/(2 * Math.sqrt(1.0 - Math.pow(drawback/b, 2)));
		a = newA;
	}

}
