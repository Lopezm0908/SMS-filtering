import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class LogisticRegression {
	
	static double[] features = new double[1];
	static int target = 0;
	static double weight = 0;
	
	public static void Start() throws IOException {
		//2 features, 1 element is the label (target) 
		
		
		String filepath = "C:\\Users\\andyl\\Documents\\College Things\\senior project\\SMSSpamCollection";
		
		
		
		//for how many times the gradient descent will iterate
		//rows/instances
		//rows in half is 2787
		int rows = 5574;								
		int iterations = 90;
		
		//format: result	message
		//separated by \t
		
		BufferedReader buffer = new BufferedReader(new FileReader(filepath));
        String line = null;
        String[] resultmessagearray;
        String[] result = new String[rows/2];
        String[] message = new String[rows/2];
        

        //even is the result/classification
        //odd  is the message
        //separate elements into respective arrays
        int counter = 0;
        for(int i = 0; i < rows; i++) {
        	line = buffer.readLine();
            resultmessagearray = line.split("\t");
        	
        	result[counter] = resultmessagearray[0];
        	message[counter] = resultmessagearray[1];
        	if(counter < rows/2 - 1) {
            	counter = counter + 1;
        	}
        }
        
        
        buffer.close();
		
        
        
        int numFeats = 1;
		int misclassified = 0;
		
		double[] weights = new double[numFeats];			
		double learnRate = 0.001;
		
		//starting at 0
		int current = 0;
		String currentMessage = message[current];
		double SpamScore = 0;
		
		
		//start gradient descent
		for(int i = 0; i < iterations; i++) {
			System.out.println("iteration: " + (i+1));
			double[] predicted = new double[rows];
			double[] error = new double[rows];
			double[] updowndelta = new double[numFeats];
			misclassified = 0;
			
			
			
			//start logistic regression
			for(int j = 0; j < rows/2 - 1; j++) {
				//analyze the message from file and put that into the features array
				//put result (0 for ham, 1 for spam) into target
				SpamScore = analyzeMessage(currentMessage);
				features[0] = SpamScore;
				if(result[current].equalsIgnoreCase("ham") == true) {
					target = 0;
				}
				else {
					target = 1;
				}
				
				
				
				//calculate predicted
				double dotproduct = dotProduct(features, weights);
				
				predicted[j] = sigmoid(dotproduct);
				
				int predTarget = predictClassify(predicted[j]);
				
				
				//compare predicted classification with the target
				if(predTarget != target) {
					misclassified = misclassified + 1;
				}
				
				//calculate error
				error[j] = predicted[j] - target;
				
				
				//calculate up down delta
				for (int k = 0; k < numFeats; k++) {
		            updowndelta[k] = updowndelta[k] + (error[j] * features[k]);
		        }
				
				//get next message
				current = current + 1;
				currentMessage = message[current];
			} //end of logistic regression
			
			//reset and go back to first message
			current = 0;
			currentMessage = message[current];
			SpamScore = 0;
			
			//updating the weights
			for(int h = 0; h < numFeats; h++) {
				weights[h] = weights[h] - (learnRate * updowndelta[h]);
			}
			
		}
		double accuracy = (1 - ((double) misclassified) / ((double) rows)) * 100;
		
		System.out.println("\nAccuracy is: " + accuracy + "%");
		System.out.println("weights: " + weights[0]);
		weight = weights[0];
		
		
	} //end of main
	
	
	public static double analyzeMessage(String message) {
		//making own features
        //does it have a link?
        //does it include a number?
        //does it contain text, free, win, call, prize, cash, claim, etc?
		//does click have a certain follow up word?
		double SpamScore = 0;
		
		
		
		//original message was not changed by this point, dw i tested it
		
		
		String[] messageArray;
		
		//looks at each word
		messageArray = message.split(" ");
		for(String each : messageArray) {
			//does it have a link?
			//score increases based on phishing chances from 
			//https://www.cybercrimeinfocenter.org/top-20-tlds-by-malicious-phishing-domains
			
			if(
				Pattern.compile(Pattern.quote(".com"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true  ||
				Pattern.compile(Pattern.quote(".net"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true  ||
				Pattern.compile(Pattern.quote(".org"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true
					) {
				SpamScore = SpamScore + 2;
			}
			else if(
				Pattern.compile(Pattern.quote(".xyz"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true  ||
				Pattern.compile(Pattern.quote(".cn"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true  ||
				Pattern.compile(Pattern.quote(".online"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true  ||
				Pattern.compile(Pattern.quote(".us"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true
					) {
				SpamScore = SpamScore + 3;
			}
			else if(
				Pattern.compile(Pattern.quote(".tk"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true  ||
				Pattern.compile(Pattern.quote(".buzz"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true  ||
				Pattern.compile(Pattern.quote(".top"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true  ||
				Pattern.compile(Pattern.quote(".ga"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true  ||
				Pattern.compile(Pattern.quote(".ml"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true  ||
				Pattern.compile(Pattern.quote(".nl"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true  ||
				Pattern.compile(Pattern.quote(".info"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true  ||
				Pattern.compile(Pattern.quote(".cf"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true  ||
				Pattern.compile(Pattern.quote(".gq"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true  ||
				Pattern.compile(Pattern.quote(".live"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true  ||
				Pattern.compile(Pattern.quote(".host"), Pattern.CASE_INSENSITIVE).matcher(each).find() == true
					) {
				SpamScore = SpamScore + 4;
			}
			
			//does it have a number?
			if(
				each.matches(".*\\d.*")
					) {
				SpamScore = SpamScore + 1;
			}
			
			//does it contain text, free, win, won, call, prize, cash, claim, or reward?
			if(
				each.equalsIgnoreCase("text")  ||
				each.equalsIgnoreCase("free")  ||
				each.equalsIgnoreCase("win")   ||
				each.equalsIgnoreCase("won")   ||
				each.equalsIgnoreCase("call")  ||
				each.equalsIgnoreCase("prize") ||
				each.equalsIgnoreCase("cash")  ||
				each.equalsIgnoreCase("claim") ||
				each.equalsIgnoreCase("reward")||
				each.equalsIgnoreCase("refund")||
				each.equalsIgnoreCase("winner")||
				each.equalsIgnoreCase("text")  ||
				each.equalsIgnoreCase("txt")   ||
				each.equalsIgnoreCase("congratulations")
					) {
				SpamScore = SpamScore + 1;
			}
			
			//does click have a certain word after it?
			if(each.equalsIgnoreCase("click")) {
				SpamScore = SpamScore + 1;
				
				//makes a substring using the original message to see what's after click
				String afterclick = message.substring(message.toLowerCase().indexOf("click") + 6).trim();
				if(
						afterclick.startsWith("here") == true ||
						afterclick.startsWith("on")   == true ||
						afterclick.startsWith("now")  == true ||
						afterclick.startsWith("&")	  == true
							) {
					SpamScore = SpamScore + 3;
				}
			}
			
				
		}
		return SpamScore;
	}
	
	
	
	//does the dot product for me lol
	public static double dotProduct(double[] a, double[] b) {
	    double result = 0.0;
	    for (int i = 0; i < a.length; i++) {
	        result = result + (a[i] * b[i]);
	    }
	    return result;
	}

	//sigmoid function because it's logistic regression
	public static double sigmoid(double dotproduct) {
		double answer = 1.0 / (1.0 + Math.exp(-dotproduct));
	    return answer;
	}
	
	//classify the prediction
	public static int predictClassify(double predictedProb) {
		
		if(predictedProb >= 0.54780) {
			return 1;
		}
		//normal
		else {
			return 0;
		}
	}
	
}
