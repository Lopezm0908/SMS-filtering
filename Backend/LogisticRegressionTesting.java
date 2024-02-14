import java.io.IOException;
import java.util.*;

public class LogisticRegressionTesting {

	public static void main(String[] args) throws IOException {
		
		
		Scanner input = new Scanner(System.in);
		String incomingMessage = "Alex just added to their Snapchat Story! Tap to see"
				+ " what they're up to: https://snapchat.com/t/TtyfKo1EWysR";
		System.out.print("Input message: ");
		incomingMessage = input.nextLine();
		
		
		
		
		//runs the training but i'm gonna comment that for now
		//LogisticRegression.Start();
		
		
		
		//Use weight to predict new spam messages
		
		double[] array = new double[1];
		
		//weight is 0.0813087002900611
		double[] weight = new double[1];
		weight[0] = LogisticRegression.weight;
		
		double[] SpamScore = new double[1];
		SpamScore[0]= LogisticRegression.analyzeMessage(incomingMessage);
		
		
		
		double dotproduct = LogisticRegression.dotProduct(SpamScore, weight);
		
		array[0] = LogisticRegression.sigmoid(dotproduct);
		
		int predTarget = LogisticRegression.predictClassify(array[0]);
		
		if(predTarget == 1) {
			System.out.println("spam");
		}
		else {
			System.out.println("ham");
		}
		
		input.close();
	}

}
