package nhn.breakoutt;

public abstract class Vector {
    public abstract double get(int index);
    public abstract void set(int index, double value);
    public abstract int getDimension();
    protected abstract Vector createNew(double... values);

    public double magnitude() {
        double sumOfSquares = 0.0;
        for (int i = 0; i < getDimension(); i++) {
            double value = get(i);
            sumOfSquares += value * value;
        }
        return Math.sqrt(sumOfSquares);
    }

    public Vector normalize() {
        double mag = magnitude();
        if (mag == 0.0) {
            throw new ArithmeticException("C1annot normalize zero vector");
        }

        double[] normalizedValues = new double[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            normalizedValues[i] = get(i) / mag;
        }

        return createNew(normalizedValues);
    }

    public double dot(Vector other) {
        if (other == null) {
            throw new NullPointerException("Other vector cannot be null");
        }
        if (getDimension() != other.getDimension()) {
            throw new IllegalArgumentException("Vector dimensions must match for dot product");
        }

        double result = 0.0;
        for (int i = 0; i < getDimension(); i++) {
            result += get(i) * other.get(i);
        }
        return result;
    }
}
