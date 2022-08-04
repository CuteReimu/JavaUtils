package self.mathmodel;

import java.util.Iterator;

public class ColumnVector extends GeneralMatrix implements Vector {
	public ColumnVector(double[] vector) {
		this(vector.length);
		for (int i = 0; i < vector.length; i++) {
			data[i][0] = vector[i];
		}
	}
	
	public ColumnVector(int[] vector) {
		this(vector.length);
		for (int i = 0; i < vector.length; i++) {
			data[i][0] = vector[i];
		}
	}

	public ColumnVector(int len) {
		super(1, len);
		if (len <= 1)
			throw new UnsupportedOperationException("don't use 0d or 1d Vector, use 1d SquareMatrix instead");
	}
	
	public void set(int index, int value) {
		set(0, index, value);
	}

	public void set(int index, double value) {
		set(0, index, value);
	}

	public double get(int index) {
		return get(0, index);
	}

	public int length() {
		return height();
	}
	
	public Iterator<Double> iterator() {
		return new Iterator<>() {
			private int cursor;

			public boolean hasNext() {
				return cursor < length();
			}

			public Double next() {
				double d = get(cursor);
				cursor++;
				return d;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

}
