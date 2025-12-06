
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
  public static void main(String[] args)
      throws FileNotFoundException, IOException, InterruptedException {
    File file = new File("data/simple.csv");
    Scanner scnr = new Scanner(System.in);
    HashMap<Integer, String> convoMap = Parse.initializeConversationMap(file);
    HashMap<String, Integer> wordmap = Parse.initializeWordMap(convoMap);
    Map<Integer, String> reverseMap = new HashMap<>();
    int[][] output = Tokenize.tokenize(wordmap, convoMap);
      

    
    for (Map.Entry<String, Integer> entry : wordmap.entrySet()) {
      reverseMap.put(entry.getValue(), entry.getKey());
    }
    
   
    MLP mlp = new MLP(25, wordmap.size(), (int) wordmap.size() / 6);
    Vectorize.checkCollisions(wordmap.size(), (int) wordmap.size() / 6, wordmap);
    System.out.println("Unique vocabulary size: " + wordmap.size());
    //MLP mlp = MLP.loadModel("story_model7.ser");
    mlp.train(100, output, wordmap, convoMap);
    
   // mlp.saveModel("story_model8.ser");
   //mlp.printNetworkMappings(wordmap);
    int chunkSize = mlp.getWordChunkSize();
    System.out.println("Model uses context window of " + chunkSize + " words\n");
    
    
    int[] input = new int[chunkSize];
    
    
    for (int i = 0; i < output.length; i++) {
      for (int j = 0; j < output[i].length; j++) {

        if (j < chunkSize) {
          System.out.print(reverseMap.get(output[i][j]) + " ");
        } else {

          for (int k = 0; k < chunkSize; k++) {
            input[k] = output[i][j - chunkSize + k];
          }

          int predictedWordIndex = mlp.feedNetwork(input);
          System.out.print(reverseMap.get(predictedWordIndex) + " ");
        }
      }
      //System.out.println();
    }
    

    
  }
}