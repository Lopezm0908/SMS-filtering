import java.io.IOException;

public class LogisticRegressionTesting {

	public static void main(String[] args) throws IOException {
		
		LogisticRegression.Start();
		
		//Use weight to predict new spam messages
		String incomingMessage = "Alex just added to their Snapchat Story! Tap to see"
				+ " what they're up to: https://snapchat.com/t/TtyfKd1EWysR";
		
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
		
		
	}

}
