import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TargetId_svm_out {

	
	private static HashMap<Integer, Double> getLabelData () throws SQLException {
		HashMap<Integer, Double> map=new HashMap<Integer,Double>();
		DBhelper helper=new DBhelper();
		String sql="SELECT * FROM stockdb.test_data;";
		helper.connSQL();
		int id=1;
		ResultSet rs;
		if (helper.selectSQL(sql)!=null) {
			rs=helper.selectSQL(sql);
			while (rs.next()) {
				double label=rs.getDouble("label");
				map.put(id, label);
				id++;
			}
		}
		
		
		return map;
		
	}
	
	
	
	public void getClassierData(String s) throws SQLException {
		 List<ClassificationData> readData=new ArrayList<>();
		ClassificationData cData=new ClassificationData();
		HashMap<Integer, Double> map=new HashMap<Integer,Double>();
		List<Integer> targetnewsid=new ArrayList<>();
		map=getLabelData();
		try {
			InputStreamReader read=new InputStreamReader(new FileInputStream(s),"UTF-8");
	    	BufferedReader bufferedReader=new BufferedReader(read);
			String line;
			int id=1;
			while ((line = bufferedReader.readLine()) != null) {
				String[] info=line.split(" ");
				cData.classification=Integer.valueOf(info[0]);
				cData.p1=Double.valueOf(info[1]);
				cData.p2=Double.valueOf(info[2]);
				cData.id=id;
				readData.add(cData);
				id++;
			}
			read.close();	
						
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		for (ClassificationData Data : readData) {
			if (Data.classification==1) {
				targetnewsid.add(Data.id);
			}
		}				
	}
	
	
	
	
	public static void main(String[] args) throws SQLException {
		TargetId_svm_out tso=new TargetId_svm_out();
		tso.getClassierData("C:\\Users\\YI\\Desktop\\é¿å±ÉfÅ[É^\\åãâ \\SingleWord\\test.txt");
		
	}
	
	
}
