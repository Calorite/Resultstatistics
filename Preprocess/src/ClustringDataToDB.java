import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ClustringDataToDB {

	
	public boolean insertClusteringData(int id,int clustering) {
		boolean f=false;
		DBhelper helper=new DBhelper();
		String sql="update stockdb.textid_clutering set clustering="+clustering+" where id="+id+"";
		helper.connSQL();
		if (helper.insertSQL(sql)) {
			f=true;
		}
		helper.deconnSQL();
		
		
		return f;
	}
	
	
	
	
	public void getClustringData(String s) {
		List<String> textLines = new ArrayList<String>();
		ClustringDataToDB cdDb=new ClustringDataToDB();
		try {
			InputStreamReader read=new InputStreamReader(new FileInputStream(s),"UTF-8");
	    	BufferedReader bufferedReader=new BufferedReader(read);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				textLines.add(line);
			}
			read.close();	
			int id=1;
			for (String line1 : textLines) {
				String[] data=line1.split(" ");
				double max=0;
				int clustering = -1;
				for (int i = 0; i < data.length; i++) {
					if (Double.valueOf(data[i])>max) {
						max=Double.valueOf(data[i]);
						clustering=i+1;
					}
				}
				System.out.println(clustering);
				cdDb.insertClusteringData(id, clustering);
				id++;
				
				
			}
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	public static void main(String[] args) {
		ClustringDataToDB cdDb=new ClustringDataToDB();
		cdDb.getClustringData("J:\\WebCrawlerSpace\\newsClustering\\models\\casestudy-en\\model-final.theta");
		
	}
}
