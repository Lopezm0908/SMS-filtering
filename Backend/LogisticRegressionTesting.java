import java.io.IOException;
import java.util.*;

public class LogisticRegressionTesting {

	public static void main(String[] args) throws IOException {
		
		
		//runs the training but i'm gonna comment that for now
		//LogisticRegression.Start();
		
		
		Scanner input = new Scanner(System.in);
		String incomingMessage = "Alex just added to their Snapchat Story! Tap to see"
				+ " what they're up to: https://snapchat.com/t/TtyfKo1EWysR";
		System.out.print("Input message: ");
		incomingMessage = input.nextLine();
		
		while(!incomingMessage.equalsIgnoreCase("stop")) {
			//Use weight to predict new spam messages
			double[] array = new double[1];
			
			
			//using functions to determine new message's spam score, which will determine if spam or not
			//weight is 0.06006918629539748
			
			double[] SpamScore = new double[1];
			double[] weight = new double[1];
			
			weight[0] = 0.06006918629539748;
			//weight[0] = LogisticRegression.weight;
			SpamScore[0]= LogisticRegression.analyzeMessage(incomingMessage);
			System.out.println("Weight: \t" + weight[0]);
			System.out.println("Spam score: \t" + SpamScore[0]);
			
			double dotproduct = LogisticRegression.dotProduct(SpamScore, weight);
			System.out.println("dot product: \t" + dotproduct);
			
			array[0] = LogisticRegression.sigmoid(dotproduct);
			System.out.println("sigmoid: \t" + array[0]);
			
			int predTarget = LogisticRegression.predictClassify(array[0]);
			
			if(predTarget == 1) {
				System.out.println("Result: \tspam");
			}
			else {
				System.out.println("Result: \tham");
			}
			
			System.out.print("\nInput message: ");
			incomingMessage = input.nextLine();
		}
		
		
		//analysis: if it's a spam score of 3 or more, it's going to be considered spam
		//i dont have a way to reduce spam score. right now, the algorithm looks at keywords
		//if there are keywords that spam would never use, please tell me
		
		

		input.close();
	}

}
