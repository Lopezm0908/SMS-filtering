import java.io.IOException;
import java.util.regex.Pattern;

public class TestMessage {
	private double[] weight = new double[1];
	
	public TestMessage() {
		weight[0] = 0.060766536750990724;
	}

	public void Test(String a) throws IOException {
		
		
		double[] SpamScore = new double[1];
		
		
		SpamScore[0] = analyzeMessage(a);
		
		int result = Process(SpamScore, weight);
		
		//print the result
		if(result == 1) {
			System.out.println("Result: spam");
		}
		else {
			System.out.println("Result: ham ");
		}
			
	}
	
	//does the calculations
	private static int Process(double[] SpamScore, double[] weight) {
		
		double dotproduct = 0.0;
		for (int i = 0; i < SpamScore.length; i++) {
	        dotproduct = dotproduct + (SpamScore[i] * weight[i]);
		}
		
		double[] sigmoid = new double[1];
		sigmoid[0] = 1.0 / (1.0 + Math.exp(-dotproduct));
		
		//takes a double
		int predTarget;
		if(sigmoid[0] >= 0.54780) {
			predTarget = 1;
		}
		else {
			predTarget = 0;
		}
		return predTarget;
	}
	
	//calculates a score
	private static double analyzeMessage(String message) {
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
				each.equalsIgnoreCase("bonus") ||
				each.equalsIgnoreCase("prize") ||
				each.equalsIgnoreCase("cash")  ||
				each.equalsIgnoreCase("claim") ||
				each.equalsIgnoreCase("reward")||
				each.equalsIgnoreCase("refund")||
				each.equalsIgnoreCase("winner")||
				each.equalsIgnoreCase("txt")   ||
				each.equalsIgnoreCase("congratulations")
					) {
				SpamScore = SpamScore + 0.8;
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

}
