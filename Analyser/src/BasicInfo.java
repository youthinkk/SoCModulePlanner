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
			boolean isGem = false;
			boolean isSS = false;
			boolean isScience = false;
			
			if (input.trim().isEmpty()) continue;
			
			matcher = pattern1.matcher(input);
			
			if (matcher.matches()) {
				String code = matcher.group(2);
				matcher2 = pattern4.matcher(code);
				
				System.out.println(code);
				if (matcher2.matches()) {
					int level = (Integer.parseInt(matcher2.group(2))/1000)*1000;
					System.out.println(level);
					
					if (matcher2.group(1).contains("SS")) {
						isSS = true;
					} else if (isScience(code)) {
						isScience = true;
					}
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
			
			while (matcher.find()) {
				String result = matcher.group(1);
				
				if (result.equals("GEM2015") || result.equals("GEM")) {
					isGem = true;
				} 
			}
			
			if (isGem) {
				System.out.println("GEM");
			} else if (isSS) {
				System.out.println("SS");
			} else if (isScience) {
				System.out.println("Science");
			} else {
				System.out.println("Module");
			}
			
			System.out.println();
		}
		
		scanner.close();
	}
	
	private boolean isScience(String input) {
		return input.equals("CM1121") ||
				input.equals("CM1131") ||
				input.equals("CM1417") ||
				input.equals("LSM1301") ||
				input.equals("LSM1302") ||
				input.equals("PC1141") ||
				input.equals("PC1142") ||
				input.equals("PC1143") ||
				input.equals("PC1144") ||
				input.equals("PC1221") ||
				input.equals("PC1222") ||
				input.equals("PC1432") ||
				input.equals("MA2213") ||
				input.equals("MA2214") ||
				input.equals("CM1101") ||
				input.equals("CM1111") ||
				input.equals("CM1161") ||
				input.equals("CM1191") ||
				input.equals("CM1401") ||
				input.equals("CM1402") ||
				input.equals("CM1501") ||
				input.equals("CM1502") ||
				input.equals("LSM1303") ||
				input.equals("PC1421") ||
				input.equals("PC1431") ||
				input.equals("PC1433") ||
				input.equals("MA1104") ||
				input.equals("MA2101") ||
				input.equals("MA2108") ||
				input.equals("MA2501") ||
				input.equals("ST2132") ||
				input.equals("ST2137");
	}
	

	public static void main(String[] args) {
		BasicInfo algo = new BasicInfo();
		algo.analyse();
	}

}
