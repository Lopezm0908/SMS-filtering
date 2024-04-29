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
		//5574 total
		//4460 is 80%
		//1114 is 20%
		int totalrows = 5574;
		int trainingrows = 4460;	
		int testingrows = 1114;
		int iterations = 200;	//was 75
		
		//format: result	message
		//separated by \t
		BufferedReader buffer = new BufferedReader(new FileReader(filepath));
        String line = null;
        String[] resultmessagearray;
        String[] trainingresult = new String[trainingrows/2];
        String[] trainingmessage = new String[trainingrows/2];
        
        
        //mix (or scramble) before the split
        //the document shouldn't be affected by this
        resultmessagearray = new String[totalrows];
        int index = 0;
        while((line = buffer.readLine()) != null) {
        	resultmessagearray[index] = line;
        	index = index + 1;
        }
        
        resultmessagearray = Mixer.mixing(resultmessagearray);
        
        
        
        //even is the result/classification
        //odd  is the message
        //separate elements into respective arrays
        int counter = 0;
        for(int i = 0; i < trainingrows; i++) {
        	String[] parts = resultmessagearray[i].split("\t");
        	
        	trainingresult[counter] = parts[0];
        	trainingmessage[counter] = parts[1];
        	if(counter < trainingrows/2 - 1) {
            	counter = counter + 1;
        	}
        }
        
        
        
        //preparing the testing data
        //put testing samples into different array
        //last 1114 of the resultmessagearray, which is 5574 total
        String[] testing = new String[testingrows];
        String[] testingresult = new String[testingrows/2];
        String[] testingmessage = new String[testingrows/2];
        
        //putting resultmessagearray into testing array
        int testingIndex = 0;
        for(int i = 4460; i < 5574; i++) {
        	testing[testingIndex] = resultmessagearray[i];
        	testingIndex = testingIndex + 1;
        }
        
        //separate testing array into testing result and message
        counter = 0;
        for(int i = 0; i < testing.length; i++) {
        	String[] parts = testing[i].split("\t");
        	
        	testingresult[counter] = parts[0];
        	testingmessage[counter] = parts[1];
        	if(counter < testingrows/2 - 1) {
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
		String currentMessage = trainingmessage[current];
		double SpamScore = 0;
		
		
		//start gradient descent
		for(int i = 0; i < iterations; i++) {
			System.out.println("iteration: " + (i+1));
			double[] predicted = new double[trainingrows];
			double[] error = new double[trainingrows];
			double[] updowndelta = new double[numFeats];
			misclassified = 0;
			
			
			
			//start logistic regression
			for(int j = 0; j < trainingrows/2 - 1; j++) {
				//analyze the message from file and put that into the features array
				//put result (0 for ham, 1 for spam) into target
				SpamScore = analyzeMessage(currentMessage);
				features[0] = SpamScore;
				if(trainingresult[current].equalsIgnoreCase("ham") == true) {
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
				currentMessage = trainingmessage[current];
			} //end of logistic regression
			
			//reset and go back to first message
			current = 0;
			currentMessage = trainingmessage[current];
			SpamScore = 0;
			
			//updating the weights
			for(int h = 0; h < numFeats; h++) {
				weights[h] = weights[h] - (learnRate * updowndelta[h]);
			}
			
		}
		double accuracy = (1 - ((double) misclassified) / ((double) trainingrows)) * 100;
		
		System.out.println("\nAccuracy with training set: " + accuracy + "%");
		System.out.println("weight: " + weights[0]);
		weight = weights[0];
		
		
		System.out.println("\nPost-Training Testing Starts: ");
		PostTrainingTest(testingresult, testingmessage, weights);
		
		
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
				SpamScore = SpamScore + 1.5;
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
					each.equalsIgnoreCase("bonus") ||
					each.equalsIgnoreCase("call")  ||
					each.equalsIgnoreCase("cash")  ||
					each.equalsIgnoreCase("claim") ||
					each.equalsIgnoreCase("congratulations") ||
					each.equalsIgnoreCase("free")  ||
					each.equalsIgnoreCase("limited-time") ||
					each.equalsIgnoreCase("prize") ||
					each.equalsIgnoreCase("refund")||
					each.equalsIgnoreCase("reward")||
					each.equalsIgnoreCase("selected")  ||
					each.equalsIgnoreCase("text")  ||
					each.equalsIgnoreCase("txt")   ||
					each.equalsIgnoreCase("unclaimed") ||
					each.equalsIgnoreCase("win")   ||
					each.equalsIgnoreCase("winner")||
					each.equalsIgnoreCase("won")   ||
					each.equalsIgnoreCase("XXX")   ||
					each.equalsIgnoreCase("18+")   ||
					each.equalsIgnoreCase("$$$")
					) {
				SpamScore = SpamScore + 0.8;
			}
			
			//act now
			if(each.equalsIgnoreCase("act")) {
				String afterword = message.substring(message.toLowerCase().indexOf("act") + 4).trim();
				if(
						afterword.toLowerCase().startsWith("now")
						) {
					SpamScore = SpamScore + 3;
				}
			}
			//adults only
			else if(each.equalsIgnoreCase("adults")) {
				String afterword = message.substring(message.toLowerCase().indexOf("adults") + 7).trim();
				if(
						afterword.toLowerCase().startsWith("only")
						) {
					SpamScore = SpamScore + 3;
				}
			}
			//click here||on||now||&
			else if(each.equalsIgnoreCase("click")) {
				SpamScore = SpamScore + 1;
				//makes a substring using the original message to see what's after click
				String afterword = message.substring(message.toLowerCase().indexOf("click") + 6).trim();
				if(
						afterword.toLowerCase().startsWith("here") == true ||
						afterword.toLowerCase().startsWith("on")   == true ||
						afterword.toLowerCase().startsWith("now")  == true ||
						afterword.startsWith("&")	 == true
							) {
					SpamScore = SpamScore + 3;
				}
			}
			else if(each.equalsIgnoreCase("hot")) {
				String afterword = message.substring(message.toLowerCase().indexOf("hot") + 4).trim();
				if(
						afterword.toLowerCase().startsWith("singles")
						) {
					SpamScore = SpamScore + 2;
				}
			}
			
				
		} //end of analysis
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
	
	//testing the last 1140
	public static void PostTrainingTest(String[] result, String[] message, double[] weight) {
		double temp;
		double[] temp2 = new double[1];
		String temp3;
		int classification;
		int misclassified = 0;
		for(int i = 0; i < result.length; i++) {
			temp = analyzeMessage(message[i]);
			temp2[0] = temp;
			classification = tested(temp2, weight);
			
			if(classification == 1) {
				temp3 = "spam";
			}
			else {
				temp3 = "ham";
			}
			
			if(!temp3.equalsIgnoreCase(result[i])) {
				misclassified = misclassified + 1;
			}
		}
		
		double accuracy = (1.0 - ((double) misclassified) / (1114.0)) * 100;
		System.out.println("Misclassified: " + misclassified + "/1114");
		System.out.println("Accuracy: " + (accuracy));
	}
	
	//combine dotproduct and sigmoid and classify
	//spamscore, weight
	public static int tested(double[] a, double[] b) {
		//takes two double arrays
		double dotproduct = dotProduct(a, b);
		
		//takes a double
		double sigmoid = sigmoid(dotproduct);
		
		//takes a double
		int classification = predictClassify(sigmoid);
		return classification;
	}
	
}
