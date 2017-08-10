import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NewsforTrainWordvec {

	
	public  void getFileList(String path) {
		//List<String> list=new ArrayList<>();
		File file=new File(path);
		TextProcessforClustring tpc=new TextProcessforClustring();
		NewsforTrainWordvec ntw=new NewsforTrainWordvec();
		File[] tempList=file.listFiles();
		for(int i=0;i<tempList.length;i++){
			if(tempList[i].isFile()){
				try {
					tpc.wirte(ntw.readfile(tempList[i].getPath())+"\r", "C:\\Users\\YI\\Desktop\\実験データ\\newsfortrainword2vec\\news\\2万news.txt");
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("wirte file failed!");
				}
				
			}
		}


		
	}

	public String readfile(String path) {
		String text="";
		
		try {
			InputStreamReader read=new InputStreamReader(new FileInputStream(path),"UTF-8");
		    BufferedReader reader=new BufferedReader(read);
		    String line="";
			while ((line=reader.readLine())!=null) {
				text=text+line;
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("can't read file!");
		}
		text.replaceAll("\\s*|\t|\r|\n|[記事全文（VIP倶楽部でログイン）]", "");
		return text;
	}
	
	
	public static void main(String[] args) {
		NewsforTrainWordvec ntw=new NewsforTrainWordvec();
		ntw.getFileList("C:\\Users\\YI\\Desktop\\実験データ\\trainingNews");
	}
}
