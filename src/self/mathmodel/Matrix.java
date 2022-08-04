package self.mathmodel;

public interface Matrix {
	void set(int x, int y, double value);
	void set(int x, int y, int value);
	double get(int x, int y);
	int width();
	int height();
	void add(Matrix m);
	void inverse();
}
