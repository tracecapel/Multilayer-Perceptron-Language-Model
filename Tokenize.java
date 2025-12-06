
import java.util.HashMap;
import java.util.Map;

public class Tokenize {
  public static int[][] tokenize(
      HashMap<String, Integer> wordMap, HashMap<Integer, String> convoMap) {
    int[][] output = new int[convoMap.size()][];
    int index = 0;
    for (Map.Entry<Integer, String> entry : convoMap.entrySet()) {
      String convo = entry.getValue();
      
      
      String[] convoWords = convo.split(" ");
      int[] tokenizedConvo = new int[convoWords.length];

      for (int i = 0; i < convoWords.length; i++) {
        if (wordMap.get(convoWords[i]) != null) {
          tokenizedConvo[i] = wordMap.get(convoWords[i]);
        }

      }
      output[index] = tokenizedConvo;
      index++;
    }
   
    return output;
  }

 
}
