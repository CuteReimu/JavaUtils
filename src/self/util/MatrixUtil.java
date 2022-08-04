package self.util;

import self.mathmodel.ColumnVector;
import self.mathmodel.GeneralMatrix;
import self.mathmodel.Matrix;
import self.mathmodel.RowVector;
import self.mathmodel.SquareMatrix;
import self.mathmodel.Vector;

// TODO 待改为泛型
public class MatrixUtil {

    private MatrixUtil() {
    }

    public static Matrix plus(Matrix a, Matrix b) {
        Matrix result = getMatrixType(a, b, '+');
        for (int i = 0; i < result.width(); i++) {
            for (int j = 0; j < result.height(); i++) {
                result.set(i, j, a.get(i, j) + b.get(i, j));
            }
        }
        return result;
    }

    public static Matrix plus(Matrix a, double n) {
        Matrix result = getMatrixType(a, a, '+');
        for (int i = 0; i < result.width(); i++) {
            for (int j = 0; j < result.height(); i++) {
                result.set(i, j, a.get(i, j) + n);
            }
        }
        return result;
    }

    public static Matrix minus(Matrix a, Matrix b) {
        Matrix result = getMatrixType(a, b, '-');
        for (int i = 0; i < result.width(); i++) {
            for (int j = 0; j < result.height(); i++) {
                result.set(i, j, a.get(i, j) - b.get(i, j));
            }
        }
        return result;
    }

    public static Matrix minus(Matrix a, double n) {
        Matrix result = getMatrixType(a, a, '-');
        for (int i = 0; i < result.width(); i++) {
            for (int j = 0; j < result.height(); i++) {
                result.set(i, j, a.get(i, j) - n);
            }
        }
        return result;
    }

    public static Matrix multiply(Matrix a, Matrix b) {
        Matrix result = getMatrixType(a, b, '*');
        for (int i = 0; i < result.width(); i++) {
            for (int j = 0; j < result.height(); i++) {
                double value = 0;
                for (int k = 0; k < a.width(); k++)
                    value += a.get(k, j) * b.get(i, k);
                result.set(i, j, value);
            }
        }
        return result;
    }

    public static Matrix multiply(Matrix a, double n) {
        Matrix result = getMatrixType(a, a, '*');
        for (int i = 0; i < result.width(); i++) {
            for (int j = 0; j < result.height(); i++) {
                result.set(i, j, a.get(i, j) * n);
            }
        }
        return result;
    }

    public static double innerProduct(Vector a, Vector b) {
        if (a.length() != b.length())
            throw IllegalArgumentError(a, b, '·');
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result += a.get(i) * b.get(i);
        }
        return result;
    }

    public static Vector outerProduct(Vector a, Vector b) {
        if (a.length() == 2 && b.length() == 2) {
            Vector result = new RowVector(3);
            result.set(2, a.get(0) * b.get(1) - a.get(1) * b.get(0));
            return result;
        } else if (a.length() == 3 && b.length() == 3) {
            Vector result = new RowVector(3);
            result.set(0, a.get(1) * b.get(2) - a.get(2) * b.get(1));
            result.set(1, a.get(2) * b.get(0) - a.get(0) * b.get(2));
            result.set(2, a.get(0) * b.get(1) - a.get(1) * b.get(0));
            return result;
        } else {
            throw IllegalArgumentError(a, b, 'x');
        }
    }

    public static Matrix subMatrix(Matrix a, int x, int y, int width, int height) {
        Matrix b;
        if (width == height) {
            b = new SquareMatrix(width);
        } else if (width == 1) {
            b = new ColumnVector(height);
        } else if (height == 1) {
            b = new RowVector(width);
        } else {
            throw new UnsupportedOperationException();
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                b.set(i, j, a.get(x + i, y + j));
            }
        }
        return b;
    }

    public static RowVector getRow(Matrix a, int y) {
        RowVector v = new RowVector(a.width());
        for (int i = 0; i < v.length(); i++) {
            v.set(i, a.get(i, y));
        }
        return v;
    }

    public static ColumnVector getColumn(Matrix a, int x) {
        ColumnVector v = new ColumnVector(a.height());
        for (int i = 0; i < v.length(); i++) {
            v.set(i, a.get(x, i));
        }
        return v;
    }

    private static Matrix getMatrixType(Matrix a, Matrix b, char opr) {
        if (opr == '+' || opr == '-') {
            if (a.width() != b.width() || a.height() != b.height())
                throw IllegalArgumentError(a, b, opr);
            if (a instanceof SquareMatrix && b instanceof SquareMatrix)
                return new SquareMatrix(((SquareMatrix) a).sideLength());
            if (a instanceof RowVector && b instanceof RowVector)
                return new RowVector(((RowVector) a).length());
            if (a instanceof ColumnVector && b instanceof ColumnVector)
                return new ColumnVector(((ColumnVector) a).length());
            return new GeneralMatrix(a.width(), a.height());
        } else if (opr == '*') {
            if (a.width() != b.height())
                throw IllegalArgumentError(a, b, opr);
            if (b.width() == a.height())
                return new SquareMatrix(a.height());
            if (b.width() == 1)
                return new ColumnVector(a.height());
            if (a.height() == 1)
                return new RowVector(b.width());
            return new GeneralMatrix(b.width(), a.height());
        }
        throw new UnsupportedOperationException();
    }

    private static String sideLength(Matrix m) {
        return "(" + m.width() + " x " + m.height() + ")";
    }

    private static IllegalArgumentException IllegalArgumentError(Matrix a, Matrix b, char opr) {
        return new IllegalArgumentException(sideLength(a) + ' ' + opr + ' ' + sideLength(b));
    }
}
