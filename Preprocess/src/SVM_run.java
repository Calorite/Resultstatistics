import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import libsvm.*;

public class SVM_run {

	private double getAccuracy(String str) {
		double out=0.0;
		String regex="Cross Validation Accuracy = [^ ]*%";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(str);
		if (matcher.find()) {
			String tString=matcher.group();
			String[] data=tString.split("=");
			String accu=data[1].substring(1, data[1].length()-1);
		    out=Double.valueOf(accu); 
		}
		return out;
	}

	public static void main(String[] args) throws IOException {
		List<String> thresholdlist=new ArrayList<>();
		thresholdlist.add("‚È‚µ");
		thresholdlist.add("0.6");thresholdlist.add("0.65");
		thresholdlist.add("0.7");thresholdlist.add("0.75");thresholdlist.add("0.8");thresholdlist.add("0.85");thresholdlist.add("0.9");thresholdlist.add("0.95");
		thresholdlist.add("1");thresholdlist.add("1.1");thresholdlist.add("1.2");
		//for (String threshold : thresholdlist) {
			double accuracycount=0.0; 
			for (int i = 1; i < 11; i++) {
				String[] trainArgs={"-v","10","C:\\Users\\YI\\Desktop\\SVM_Trainingfile\\singleword\\ƒJƒC“ñæ1\\"+i+"\\data.txt"};
				SVM_run run=new SVM_run();

				ByteArrayOutputStream outputStream=new ByteArrayOutputStream(1024);
				PrintStream cacheStream=new PrintStream(outputStream);
				PrintStream oldStream=System.out;
				System.setOut(cacheStream);
				svm_train.main(trainArgs);
				String message=outputStream.toString();
				System.setOut(oldStream);
				double accuracy=run.getAccuracy(message);
				accuracycount=accuracycount+accuracy;
				//System.out.println(accuracy/10);

			}
			System.out.println("•½‹Ï¸“x:"+accuracycount/10);
		//}
	}
}
