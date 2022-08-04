package self.mathmodel;

import java.util.Iterator;

public class RowVector extends GeneralMatrix implements Vector {
	public RowVector(double[] vector) {
		this(vector.length);
		System.arraycopy(vector, 0, data[0], 0, vector.length);
	}
	
	public RowVector(int[] vector) {
		this(vector.length);
		for (int i = 0; i < vector.length; i++) {
			data[0][i] = vector[i];
		}
	}

	public RowVector(int len) {
		super(len, 1);
		if (len <= 1)
			throw new UnsupportedOperationException("don't use 0d or 1d Vector, use 1d SquareMatrix instead");
	}
	
	public void set(int index, int value) {
		set(index, 0, value);
	}

	public void set(int index, double value) {
		set(index, 0, value);
	}

	public double get(int index) {
		return get(index, 0);
	}

	public int length() {
		return width();
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
