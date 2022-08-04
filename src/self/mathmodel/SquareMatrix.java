package self.mathmodel;

public class SquareMatrix extends GeneralMatrix {
	public SquareMatrix(double[][] matrix) {
		super(matrix);
		if (width() != height())
			throw new IllegalArgumentException("(" + matrix[0].length + " x " + matrix.length + ")");
	}
	public SquareMatrix(int[][] matrix) {
		super(matrix);
		if (width() != height())
			throw new IllegalArgumentException("(" + matrix[0].length + " x " + matrix.length + ")");
	}
	
	public SquareMatrix(int sideLength) {
		super(sideLength, sideLength);
	}
	
	public int sideLength() {
		return width();
	}
	
	public double det() {
		return det(new int[data.length], data.length);
	}
	
	private double det(int[] nowValue, int x) {
		int nowDet = 0;
		if (x == 1) {
			for (int i = 0; i < nowValue.length; i++) {
				if (nowValue[i] == 0) {
					nowValue[i] = 1;
					nowDet = inverseNum(nowValue) % 2 == 0 ? 1 : -1;
					for (int j = 0; j < nowValue.length; j++) {
						nowDet *= data[j][nowValue[j] - 1];
					}
					nowValue[i] = 0;
					break;
				}
			}
		} else {
			for (int i = 0; i < nowValue.length; i++) {
				if (nowValue[i] == 0) {
					nowValue[i] = x;
					nowDet += det(nowValue, x - 1);
					nowValue[i] = 0;
				}
			}
		}
		return nowDet;
	}

	private int inverseNum(int[] index) {
		if (index.length <= 1)
			return 0;
		int num = 0;
		for (int i = 0; i < index.length - 1; i++) {
			for (int j = i + 1; j < index.length; j++) {
				if (Double.compare(index[i], index[j]) == 0) {
					throw new IllegalArgumentException(String.valueOf(index[j]));
				} else if (Double.compare(index[i], index[j]) > 0) {
					num++;
				}
			}
		}
		return num;
	}
}
