
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;



public class MLP implements Serializable {
  Perceptron[] hiddenLayer;
  Perceptron[] outputLayer;
  double learnrate;
  int wordChunkSize;
  int hashDimension;
 
  int binaryLength;

  public MLP(int hiddenlayersize, int vocabularySize, int hashDimension) {
    for(int i = 0; i < 16; i++){
      if(Math.pow(2, i) > vocabularySize){
        this.binaryLength = i;
        System.out.println("Binary vector input length: " + i);
        break;
        
      }
    }
    this.wordChunkSize = 5;
    this.hiddenLayer = new Perceptron[hiddenlayersize];
    for (int i = 0; i < hiddenLayer.length; i++) {
      hiddenLayer[i] = new Perceptron(wordChunkSize * hashDimension);
     // hiddenLayer[i] = new Perceptron(binaryLength);

    }
    this.outputLayer = new Perceptron[vocabularySize];
    for (int i = 0; i < outputLayer.length; i++) {
      outputLayer[i] = new Perceptron(hiddenLayer.length);
    }
    this.learnrate = .01;
    this.hashDimension = hashDimension;
    

    

  }
  

  public int getWordChunkSize() {
    return wordChunkSize;
  }

  public void saveModel(String filename) {
    try (ObjectOutputStream out =
      new ObjectOutputStream(new FileOutputStream(filename))) {
      out.writeObject(this); 
      System.out.println("Model saved" + filename);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static MLP loadModel(String filename) {
    try (ObjectInputStream in =
             new ObjectInputStream(new FileInputStream(filename))) {
      return (MLP) in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  

  public int feedNetwork(int[] inputs) {
    if (inputs.length != wordChunkSize) {
      return -1;
    }
    for (int h = 0; h < hiddenLayer.length; h++) {
    int offset = 0;
    for (int i = 0; i < inputs.length; i++) {
        
      
      //int[] vector = Vectorize.toBinary(inputs[i], binaryLength);
      int[] vector = Vectorize.vectorize(inputs[i], outputLayer.length , hashDimension);
      
      for (int j = 0; j < vector.length; j++) {
        hiddenLayer[h].x[j + offset] = vector[j];

      }
      offset += hashDimension;
          
      }
       
        //System.out.println(idx);
        
    }

  
    

    
    
    

    
    
    
    

    
    

    for (int i = 0; i < outputLayer.length; i++) {
      for (int j = 0; j < hiddenLayer.length; j++) {
        outputLayer[i].x[j] = (float) hiddenLayer[j].activate();
      }
    }

    return getNetworkDecision();
  }

  public int getNetworkDecision() {
    int index = 0;
    double[] softmax = softmaxOutput();
    double max = softmax[0];

    for (int i = 0; i < softmax.length; i++) {
      if (max < softmax[i]) {
        max = softmax[i];
        index = i;
      }
    }

    return index;
  }

  public double[] softmaxOutput() {
    double[] softmax = new double[outputLayer.length];

    double total = 0;
    for (int i = 0; i < outputLayer.length; i++) {
      total += Math.pow(Math.E, outputLayer[i].getWeightedSum());
    }

    for (int i = 0; i < outputLayer.length; i++) {
      softmax[i] = Math.pow(Math.E, outputLayer[i].getWeightedSum()) / total;
    }
    return softmax;
  }

  public double SSE(
      HashMap<String, Integer> wordMap, HashMap<Integer, String> convoMap) {
    int[][] wordToNums = Tokenize.tokenize(wordMap, convoMap);
    Map<Integer, String> reverseMap = new HashMap<>();
    for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
      reverseMap.put(entry.getValue(), entry.getKey());
    }

    int[] input = new int[2];
    double sum = 0;
    for (int i = 0; i < wordToNums.length; i++) {
      for (int j = 0; j < wordToNums[i].length; j++) {
        if (j < 2) {
        } else {
          if (i % 10 == 0) {
            input[0] = wordToNums[i][j - 2];
            input[1] = wordToNums[i][j - 1];
            

            feedNetwork(input);

            
            String target = reverseMap.get(wordToNums[i][j]);

            

            int targetIndex = wordMap.get(target);

           
            double error = 1 - outputLayer[targetIndex].activateOutput();

            sum += (error * error);
          }
        }
      }
    }
    return sum;
  }

  public double crossEntropyLoss(double[] softmaxOutputs, int targetIndex) {
    return -Math.log(softmaxOutputs[targetIndex] + 1e-12);
  }

  public void printNetworkMappings(HashMap<String, Integer> wordMap) {
    Map<Integer, String> reverseMap = new HashMap<>();
    for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
      reverseMap.put(entry.getValue(), entry.getKey());
    }
    for (int i = 0; i < outputLayer.length; i++) {
      System.out.println("Output node: " + i + " mapped to word "
          + reverseMap.get(i) + " -> " + outputLayer[i].activateOutput());
    }
  }

 
  public void backpropagate(int targetWordIndex) {
    double[] outputDeltas = new double[outputLayer.length];
    double[] softmaxOutputs = softmaxOutput();
    for (int i = 0; i < outputLayer.length; i++) {
      double target = (targetWordIndex == i)
          ? 1.0
          : 0.0; 

      
      double deltaOutput = target - softmaxOutputs[i];
      outputDeltas[i] = deltaOutput;

      for (int j = 0; j < outputLayer[i].w.length; j++) {
        outputLayer[i].w[j] += learnrate * deltaOutput * outputLayer[i].x[j];
      }
      outputLayer[i].bias += learnrate * deltaOutput;
    }

    for (int j = 0; j < hiddenLayer.length; j++) {
      double output = hiddenLayer[j].activate();
      //double hiddenSum = hiddenLayer[j].getWeightedSum();

      double errorSignal = 0;
      for (int k = 0; k < outputLayer.length; k++) {
        errorSignal += outputDeltas[k] * outputLayer[k].w[j];
      }

      double hiddenGrad = 1- (output * output) ;
      
      

      double deltaHidden = errorSignal * hiddenGrad;

      for (int k = 0; k < hiddenLayer[j].w.length; k++) {
        hiddenLayer[j].w[k] += learnrate * deltaHidden * hiddenLayer[j].x[k];
      }

      hiddenLayer[j].bias += learnrate * deltaHidden;
    }
  }

  
  public void train(int epochs, int[][] wordToNums,
      HashMap<String, Integer> wordMap, HashMap<Integer, String> convoMap) {
    Map<Integer, String> reverseMap = new HashMap<>();
    for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
      reverseMap.put(entry.getValue(), entry.getKey());
    }

    for (double epoch = 0; epoch < epochs; epoch++) {
      long startTime = System.nanoTime();
      double totalLoss = 0;
      int numSamples = 0;

      if (epoch % 10 == 0 && epoch > 0) {
        //saveModel("model_checkpoint.ser");
      }

      // saveModel("mlp_model_checkpoint.ser");
      // System.out.println("Iteration: " + epoch + " Error: " +SSE(wordMap,
      // convoMap) );
      if (epoch % 100 == 0 && epoch > 0) {
        // System.out.println(SSE(wordMap, convoMap));
      }

      // long startTime = System.nanoTime();

      for (int i = 0; i < wordToNums.length; i++) {
        //System.out.println(i + "/" + wordToNums.length);
        int[] context = new int[wordChunkSize];
        int index = 0;

        for (int j = 0; j < wordToNums[i].length; j++) {

          if (j < wordChunkSize) {
            context[j] = wordToNums[i][j];

            if (j == wordChunkSize - 1) {

              index = wordChunkSize;
            }
          }

          else if (j >= wordChunkSize) {
            int negIdx = -wordChunkSize + 1;
            for (int w = 0; w < wordChunkSize; w++) {
              context[w] = wordToNums[i][j + negIdx];
              negIdx++;
            }

            //context[0] = wordToNums[i][j - 1];

            //context[1] = wordToNums[i][j];
            index = wordChunkSize;
          }

          if (index == wordChunkSize) {
            // System.out.println("----------------START --------------------");

            // System.out.println();

            //System.out.println("Context: ");
            for (int g = 0; g < context.length; g++) {
              //System.out.print(reverseMap.get(context[g]) + " ");
              // System.out.print(context[g] + " ");
            }

           // if (Math.random() < .1) {
              feedNetwork(context);

              if (j + 1 < wordToNums[i].length) {
                //System.out.println("Trained on: " +
                 //reverseMap.get(wordToNums[i][j + 1]));

                if (epoch % 1 == 0) {
                  double[] softmaxOutputs = softmaxOutput();
                  totalLoss += crossEntropyLoss(softmaxOutputs, wordToNums[i][j + 1]);
                  numSamples++;
                }

                backpropagate(wordToNums[i][j + 1]);

                // System.out.println("----------------END --------------------");
              }
            }

            for (int k = index - 1; k > 0; k--) {
              context[k] = context[k - 1];
            }
            index = wordChunkSize - 1;
          }
        //}
      }

      if (epoch % 10 == 0) {
        double avgLoss = totalLoss / numSamples;
        System.out.printf(
            "Epoch %d/%d - Avg Loss: %.4f%n", (int) epoch, epochs, avgLoss);
      }

      long endTime = System.nanoTime();
      long elapsedNanos = endTime - startTime;
      double elapsedSeconds = elapsedNanos / 1_000_000_000.0;

      int remainingEpochs = epochs - ((int) epoch + 1);
      double estimatedTimeLeftSeconds = remainingEpochs * elapsedSeconds;
      double hoursLeft = estimatedTimeLeftSeconds / 3600.0;
      if (epoch % 10 == 0) {
        System.out.println(
            String.format("Estimated time remaining: %.2f hours (%.2f minutes)",
                hoursLeft, hoursLeft * 60));

        
      }
    }

  }
  


  
  
  
}