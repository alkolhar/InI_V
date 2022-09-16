package basic;

import net.ensode.glassfishbook.*;

public class WebServiceClient {

	public static void main(String[] args) {
		int result;
		
		CalculatorService calcService = new CalculatorService();
		Calculator calc = calcService.getCalculatorPort();
		
		result = calc.add(2, 5);
		System.out.println("2 + 5 = " + result);
	}
}
