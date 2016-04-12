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
		
		String regex4 = "(.*?)(\\d{4})(.*?)";
		
		Pattern pattern1 = Pattern.compile(regex1);
		Pattern pattern2 = Pattern.compile(regex2);
		Pattern pattern3 = Pattern.compile(regex3);
		Pattern pattern4 = Pattern.compile(regex4);
		Matcher matcher;
		Matcher matcher2;
		
		while (scanner.hasNextLine()) {
			String input = scanner.nextLine();
			String type = "";
			
			if (input.trim().isEmpty()) continue;
			
			matcher = pattern1.matcher(input);
			
			if (matcher.matches()) {
				String code = matcher.group(2);
				matcher2 = pattern4.matcher(code);
				
				System.out.println(code);
				if (matcher2.matches()) {
					int level = (Integer.parseInt(matcher2.group(2))/1000)*1000;
					System.out.println(level);
				}
				
				System.out.println(matcher.group(4));
				System.out.println(matcher.group(6));
				System.out.println(matcher.group(8));
			}
			
			matcher = pattern2.matcher(input);
			
			if (matcher.matches()) {
				type = matcher.group(2);
			}
			
			matcher = pattern3.matcher(type);
			boolean isGem = false;
			
			while (matcher.find()) {
				String result = matcher.group(1);
				
				if (result.equals("GEM2015") || result.equals("GEM")) {
					isGem = true;
				} 
			}
			
			if (isGem) {
				System.out.println("GEM");
			} else {
				System.out.println("Module");
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
