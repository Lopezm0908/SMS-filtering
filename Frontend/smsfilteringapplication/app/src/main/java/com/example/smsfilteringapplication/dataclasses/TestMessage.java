package com.example.smsfilteringapplication.dataclasses;

import java.io.IOException;
import java.util.regex.Pattern;

public class TestMessage {
	private double[] weight = new double[3];
	
	public TestMessage() {
		weight[0] = -0.03583110263073061;
		weight[1] = 0.5168951326337814;
		weight[2] = 0.09307722471891858;
	}

	public void Test(String a) throws IOException {
		
		
		double[] SpamScore = new double[3];
		
		
		SpamScore[0] = analyzeMessage1(a);
		SpamScore[1] = analyzeMessage2(a);
		SpamScore[2] = analyzeMessage3(a);
		
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
	
	
	
	public static double analyzeMessage1(String message) {
		double SpamScore = 0;
		
		
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
			
			
		} //end of analysis
		return SpamScore;
	}
	
	
	
	//looking for certain keywords
	public static double analyzeMessage2(String message) {
		double SpamScore = 0;
		
		String[] messageArray;
		
		messageArray = message.split(" ");
		for(String each : messageArray) {
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
			//make money
			else if(each.equalsIgnoreCase("make")) {
				String afterword = message.substring(message.toLowerCase().indexOf("make") + 5).trim();
				if(
						afterword.toLowerCase().startsWith("money")
						) {
					SpamScore = SpamScore + 3;
				}
			}
			//hot singles
			else if(each.equalsIgnoreCase("hot")) {
				String afterword = message.substring(message.toLowerCase().indexOf("hot") + 4).trim();
				if(
						afterword.toLowerCase().startsWith("singles")
						) {
					SpamScore = SpamScore + 2;
				}
			}
		}
		return SpamScore;
	}
	
	//looking at numbers
	public static double analyzeMessage3(String message) {
		double SpamScore = 0;
		
		
		String[] messageArray;
		
		//looks at each word
		messageArray = message.split(" ");
		for(String each : messageArray) {
			//does it have a number?
			if(
				each.matches(".*\\d.*")
					) {
				SpamScore = SpamScore + 1;
			}
		}
		return SpamScore;
	}

	
}
