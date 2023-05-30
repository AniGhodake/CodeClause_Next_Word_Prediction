import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NextWordPrediction {
    private static final int N_GRAM_SIZE = 2; // Adjust this value to change the n-gram size
    
    private Map<List<String>, List<String>> nGrams;

    public NextWordPrediction() {
        nGrams = new HashMap<>();
    }

    public void train(String[] words) {
        for (int i = 0; i < words.length - N_GRAM_SIZE; i++) {
            List<String> prefix = new ArrayList<>();
            for (int j = 0; j < N_GRAM_SIZE; j++) {
                prefix.add(words[i + j]);
            }

            String nextWord = words[i + N_GRAM_SIZE];
            if (nGrams.containsKey(prefix)) {
                nGrams.get(prefix).add(nextWord);
            } else {
                List<String> suffixes = new ArrayList<>();
                suffixes.add(nextWord);
                nGrams.put(prefix, suffixes);
            }
        }
    }

    public List<String> predictNextWords(String prefix) {
        String[] words = prefix.split("\\s+");
        List<String> prediction = new ArrayList<>();

        if (words.length >= N_GRAM_SIZE) {
            List<String> prefixList = new ArrayList<>();
            for (int i = words.length - N_GRAM_SIZE; i < words.length; i++) {
                prefixList.add(words[i]);
            }

            if (nGrams.containsKey(prefixList)) {
                prediction.addAll(nGrams.get(prefixList));
            }
        }

        return prediction;
    }

    public static void main(String[] args) {
        NextWordPrediction predictor = new NextWordPrediction();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the training set: ");
        String trainingSet = scanner.nextLine();
        String[] words = trainingSet.split("\\s+");
        predictor.train(words);

        while (true) {
            System.out.print("Enter a prefix (or 'exit' to quit): ");
            String prefix = scanner.nextLine();

            if (prefix.equalsIgnoreCase("exit")) {
                break;
            }

            List<String> predictions = predictor.predictNextWords(prefix);
            if (predictions.isEmpty()) {
                System.out.println("No predictions found for the given prefix.");
            } else {
                System.out.println("Predictions:");
                for (String prediction : predictions) {
                    System.out.println(prediction);
                }
            }
        }
    }
}
