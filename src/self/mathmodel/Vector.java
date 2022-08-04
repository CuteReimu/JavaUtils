package self.mathmodel;

public interface Vector extends Matrix, Iterable<Double> {
	void set(int index, int value);
	void set(int index, double value);
	double get(int index);
	int length();
}
