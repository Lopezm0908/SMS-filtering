import java.util.Random;

public class Mixer {
	
	//mixing the string
	private static String[] mix(String[] original) {
		//create copy of original array
		String[] mixed = new String[original.length];
		for(int i = 0; i < original.length; i++) {
			mixed[i] = original[i];
		}
		
		//mix up (or scramble) the copy of the original array
		Random randomNum = new Random();
		for(int i = 0; i < mixed.length; i++) {
			int newIndex = randomNum.nextInt(mixed.length);
			//swap
			String temp = mixed[i];
			mixed[i] = mixed[newIndex];
			mixed[newIndex] = temp;
		}
		return mixed;
	}
	
	//keep mixing the string
	public static String[] mixing(String[] original) {
		String[] mixed = new String[original.length];
		for(int i = 0; i < 10; i++) {
			mixed = mix(original);
		}
		return mixed;
	}
}
