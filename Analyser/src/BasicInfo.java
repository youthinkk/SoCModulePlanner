import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicInfo {
	
	public void analyse() {
		Scanner scanner = new Scanner(System.in);
		String regex1 = "(.*?)\"ModuleCode\":\"(.*?)\""
				+ "(.*?)\"ModuleTitle\":\"(.*?)\""
				+ "(.*?)\"Department\":\"(.*?)\""
				+ "(.*?)\"ModuleCredit\":\"(.*?)\"(.*?)";
		
		String regex2 = "(.*?)\"Types\":\\[(.*?)\\](.*?)";
		
		String regex3 = "\"(.*?)\"";
		
		Pattern pattern1 = Pattern.compile(regex1);
		Pattern pattern2 = Pattern.compile(regex2);
		Pattern pattern3 = Pattern.compile(regex3);
		Matcher matcher;
		
		while (scanner.hasNextLine()) {
			String input = scanner.nextLine();
			String type = "";
			
			if (input.trim().isEmpty()) continue;
			
			matcher = pattern1.matcher(input);
			
			if (matcher.matches()) {
				System.out.println(matcher.group(2));
				System.out.println(matcher.group(4));
				System.out.println(matcher.group(6));
				System.out.println(matcher.group(8));
			}
			
			matcher = pattern2.matcher(input);
			
			if (matcher.matches()) {
				type = matcher.group(2);
			}
			
			matcher = pattern3.matcher(type);
			
			while (matcher.find()) {
				String result = matcher.group(1);
				
				if (result.equals("GEM2015")) {
					System.out.println("GEM");
				} else {
					System.out.println(result);
				}
			}
			
			System.out.println();
		}
		
		scanner.close();
	}
	

	public static void main(String[] args) {
		BasicInfo algo = new BasicInfo();
		algo.analyse();
	}

}
