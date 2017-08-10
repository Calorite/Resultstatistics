import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class VectorDataToDB {

	public String insertVectorData(String path) {
		//String word=null;
		//NewsforTrainWordvec ntw=new NewsforTrainWordvec();
		try {
			InputStreamReader read=new InputStreamReader(new FileInputStream(path),"UTF-8");
			BufferedReader reader=new BufferedReader(read);
			String line="";
			String word=null;
			String vector=null;
			DBhelper helper=new DBhelper();

			helper.connSQL();
			int sum=0;
			while ((line=reader.readLine())!=null) {
				
				if (sum>0) {
					String[] arry=line.split(" ");
					String[] wordarry=arry[0].split(":");
					word=wordarry[0];
					for (int i = 1; i < arry.length; i++) {
						if (i==1) {
							vector=arry[i];
						}else {
							vector=vector+","+arry[i];
						}
					}
					if (word!=null&&vector!=null) { 
						if (word.length()>20) {
							System.out.println(word);
						}else {
							String sql="insert into stockdb.word_vector_30000 value ('"+word+"','"+vector+"');";
						    helper.insertSQL(sql);
						    System.out.println(word);
						}
						
						
						
					}
				}
				sum++;
			}
			helper.deconnSQL();
			return word;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
	
	
	
	public static void main(String[] args) {
		VectorDataToDB vdtdb=new VectorDataToDB();
		vdtdb.insertVectorData("C:\\Users\\YI\\Desktop\\é¿å±ÉfÅ[É^\\newsfortrainword2vec\\word2vec\\30000_word2vec_200d.txt");
	}
}
