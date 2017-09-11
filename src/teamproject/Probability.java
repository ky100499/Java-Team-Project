package teamproject;

public class Probability {
    public static double getProbability(double temperature, double rain) {
        return (double)Math.round(200 * (1 - Math.pow(rain / 100, 1.2)) * normalDistIntegration(0, temperature / 10) * 10) / 10;
    }

    private static double normalDistIntegration(double start, double end) {
        if (end < 0) return 0;
        double sum = 0;
        int n = 100000;
        for (int i = 0; i < n; i++) {
            sum += normalDist(start + (end - start) * i / n, 0.8, 0) * (end - start) / n;
        }
        return sum;
    }

    private static double normalDist(double x, double sigma, double mean) {
        return Math.exp(-1 * Math.pow(x - mean, 2) / (2 * sigma * sigma)) / (Math.sqrt(2 * Math.PI) * sigma);
    }
}
