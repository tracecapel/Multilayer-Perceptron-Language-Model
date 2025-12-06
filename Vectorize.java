
import java.util.Arrays;
import java.util.HashMap;

public class Vectorize

{
    
    
    
    public static int[] vectorize(int index, int vocabSize, int dimension) {
        int idx = index % dimension;
        int idx2 = (index / dimension) % dimension;
       
        
        
        
        int[] wordVector = new int[dimension];

        
        wordVector[idx] = 1;
        wordVector[idx2] = 1;

        return wordVector;
    }

    public static int[] toBinary(int index, int binaryLength) {
        
       
        
        int[] binaryVector = new int[binaryLength];

        
        for (int i = 0; i < binaryLength; i++) {
            binaryVector[binaryLength - 1 - i] = (index >> i) & 1;
        }

      
        for(int i = 0; i < binaryVector.length; i++){
            
        }
        

      
        
        return binaryVector;
    }


    public static void checkCollisions( int vocabSize, int dimension, HashMap<String, Integer> wordMap ){
        int collisions = 0;
    
    for(int i = 0; i < wordMap.size(); i++){
        
        int[] wordVector = vectorize(i, vocabSize, dimension);

        for(int j = i + 1; j < wordMap.size(); j++){
            int[] tempVector = vectorize(j, vocabSize, dimension);

            if(Arrays.equals(wordVector, tempVector)){
                collisions++;
            }
        }

        

    }

    System.out.println("Collisions: " + collisions);

        
        
    }
    

    
}
