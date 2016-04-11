import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Prerequisite {
	
	public void analyse() {
		Scanner scanner = new Scanner(System.in);
		String regex1 = "(.*?)\"ModuleCode\":\"(.*?)\"(.*?)";
		String regex2 = "(.*?)\"Prerequisite\":\"(.*?)\"(.*?)";
		String regex3 = "\\d{4}";
		Pattern pattern1 = Pattern.compile(regex1);
		Pattern pattern2 = Pattern.compile(regex2);
		Pattern pattern3 = Pattern.compile(regex3);
		Matcher matcher1;
		Matcher matcher2;
		
		while (scanner.hasNextLine()) {
			String input = scanner.nextLine();
			if (input.trim().isEmpty()) continue;
			
			matcher1 = pattern1.matcher(input);	
			if (matcher1.matches()) {
				System.out.println(matcher1.group(2));
			}
			
			matcher1 = pattern2.matcher(input);
			if (matcher1.matches()) {
				String prereq = matcher1.group(2);
				matcher2 = pattern3.matcher(prereq);
				
				if (matcher2.find()) {
					System.out.println(prereq);
				} else {
					System.out.println("NIL");
				}
			} else {
				System.out.println("NIL");
			}
			System.out.println();
		}
		
		scanner.close();
	}
	
	public static void main(String[] args) {
		Prerequisite algo = new Prerequisite();
		algo.analyse();
	}
}
