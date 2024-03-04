import java.io.IOException;
import java.util.*;

public class LogisticRegressionTesting {

	public static void main(String[] args) throws IOException {
		
		
		//runs the training but i'm gonna comment that for now
		//LogisticRegression.Start();
		
		//probably gonna make a voting classifier class and shove all the testing stuff in there
		//decision tree
		
		
		
		//voting classifier
		
		
		
		
		Scanner input = new Scanner(System.in);
		String incomingMessage = "Alex just added to their Snapchat Story! Tap to see"
				+ " what they're up to: https://snapchat.com/t/TtyfKo1EWysR";
		System.out.print("Input message: ");
		incomingMessage = input.nextLine();
		
		while(!incomingMessage.equalsIgnoreCase("stop")) {
			//Use weight to predict new spam messages
			double[] array = new double[1];
			
			
			//using functions to determine new message's spam score, which will determine if spam or not
			//weight is -0.6459973752594709
			
			double[] SpamScore = new double[1];
			double[] weight = new double[1];
			
			weight[0] = -0.6459973752594709;
			//weight[0] = LogisticRegression.weight;
			SpamScore[0]= LogisticRegression.analyzeMessage(incomingMessage);
			System.out.println("Weight: \t" + weight[0]);
			System.out.println("Spam score: \t" + SpamScore[0]);
			
			//arrays because functions accepts array
			//put spamscore and weight into dotproduct
			double dotproduct = LogisticRegression.dotProduct(SpamScore, weight);
			System.out.println("dot product: \t" + dotproduct);
			
			//put result of dotproduct into sigmoid
			array[0] = LogisticRegression.sigmoid(dotproduct);
			System.out.println("sigmoid: \t" + array[0]);
			
			//put that into classification
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
		
		

		input.close();
	}

}
