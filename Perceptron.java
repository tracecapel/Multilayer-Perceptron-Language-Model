
import java.io.Serializable;

public class Perceptron implements Serializable {
  float x[]; 
  double w[]; 
  double bias; 

  public Perceptron(int numInputs) {
    this.x = new float[numInputs];
    this.w = new double[numInputs];
    for (int i = 0; i < w.length; i++) {
      w[i] = (Math.random() - 0.5) * Math.sqrt(2.0 / numInputs);

    }
    this.bias = 0;
  }

  // Tanh activation
  public double activate() {
    double weightedSum = 0;

    for (int i = 0; i < x.length; i++) {
      weightedSum += x[i] * w[i];
    }

    weightedSum += bias;

    //return 1 / (1 + Math.exp(-weightedSum)); sigmoid activation
    //return Math.max(0, weightedSum); // RELU activation
    return Math.tanh(weightedSum);
  }

  // Sigmoid activation for output layer
  public double activateOutput() {
    double weightedSum = 0;

    for (int i = 0; i < x.length; i++) {
      weightedSum += x[i] * w[i];
    }

    weightedSum += bias;

    return 1 / (1 + Math.exp(-weightedSum));
  }

  // Weighted sum
  public double getWeightedSum() {
    double weightedSum = 0;

    for (int i = 0; i < x.length; i++) {
      weightedSum += x[i] * w[i];
    }

    weightedSum += bias;

    return weightedSum;
  }
}