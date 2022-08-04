package self.mathmodel;

import java.util.Arrays;

public class GeneralMatrix implements Matrix {
	protected double[][] data;
	public GeneralMatrix(int[][] matrix) {
		if (matrix.length == 0 || matrix[0].length == 0)
			throw new UnsupportedOperationException("don't use 0d Matrix");
		data = new double[matrix.length][matrix[0].length];
		init(matrix);
	}
	public GeneralMatrix(double[][] matrix) {
		if (matrix.length == 0 || matrix[0].length == 0)
			throw new UnsupportedOperationException("don't use 0d Matrix");
		data = new double[matrix.length][matrix[0].length];
		init(matrix);
	}
	public GeneralMatrix(int width, int height) {
		if (width == 0 || height == 0)
			throw new UnsupportedOperationException("don't use 0d Matrix");
		else
			data = new double[height][width];
	}
	private void init(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			System.arraycopy(matrix[i], 0, data[i], 0, matrix[0].length);
		}
	}
	private void init(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				data[i][j] = matrix[i][j];
			}
		}
	}
	public void set(int x, int y, double value) {
		data[y][x] = value;
	}
	public void set(int x, int y, int value) {
		data[y][x] = value;
	}
	public double get(int x, int y) {
		return data[y][x];
	}
	public int width() {
		if (data.length == 0)
			return 0;
		return data[0].length;
	}
	public int height() {
		return data.length;
	}
	public int hashCode() {
		return Arrays.deepHashCode(data);
	}
	public boolean equals(Object obj) {
		if (obj instanceof GeneralMatrix) {
			if (data.length == ((GeneralMatrix) obj).data.length) {
				if (data.length != 0) {
					for (int i = 0; i < data.length; i++) {
						for (int j = 0; j < data[0].length; j++) {
							if (Double.compare(data[i][j], ((GeneralMatrix) obj).data[i][j]) != 0) {
								return false;
							}
						}
					}
				}
				return true;
			}
		}
		return false;
	}
	public void add(Matrix m) {
		if (width() != m.width() || height() != m.height())
			throw new UnsupportedOperationException("(" + width() + " * " + height() + ") + (" + m.width() + " * " + m.height() + ")");
		for (int i = 0; i < height(); i++) {
			for (int j = 0; j < width(); j++) {
				data[i][j] += m.get(i, j);
			}
		}
	}
	public void inverse() {
		for (int i = 0; i < height(); i++) {
			for (int j = 0; j < width(); j++) {
				data[i][j] = -data[i][j];
			}
		}
	}
}
