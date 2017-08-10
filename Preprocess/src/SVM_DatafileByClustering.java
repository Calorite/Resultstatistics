import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SVM_DatafileByClustering {

	DBhelper helper=new DBhelper();
	public List<String> getTargetNewsID(int clustering) throws SQLException {//int clustering
		List<String>  list =new ArrayList<>();
		String sql="SELECT * FROM stockdb.textid_clutering where clustering="+clustering+";";//where clustering="+clustering+"
		ResultSet rs;
		DBhelper helper=new DBhelper();
		helper.connSQL();
		if (helper.selectSQL(sql)!=null) {
			rs=helper.selectSQL(sql);
			while (rs.next()) {
				String textid=rs.getString("textid");
				list.add(textid);
			}
		}
		helper.deconnSQL();
		return list;
	}
	
	public String getVectorData(String textid) throws SQLException {
		String vector = null;
		DBhelper helper=new DBhelper();
		ResultSet rs;
		String sql="SELECT * FROM news_stock.trainingnews_vector where newsid="+textid+";";
		helper.connSQL();
		if (helper.selectSQL(sql)!=null) {
			rs=helper.selectSQL(sql);
			if(rs.next()) {
				
				vector=rs.getString("vector");
			}
		}
		helper.deconnSQL();
		
		return vector;
	}
	
	public double getLabelData(String textid) throws SQLException {
		Double label = 0.0;
		DBhelper helper=new DBhelper();
		ResultSet rs;
		String sql="SELECT * FROM stockdb.learning_data where newsid="+textid+";";
		helper.connSQL();
		if (helper.selectSQL(sql)!=null) {
			rs=helper.selectSQL(sql);
			if(rs.next()) {	
				label=rs.getDouble("label");
			}
		}
		
		helper.deconnSQL();
		return label;
	}
	
	private static int adjust(double data) {
		int label=0;
		if (data>0) {
			label=1;
		}else if (data<-0) {
			label=-1;
		} 				
		return label;
		
	}
	
	public boolean wirteFile(String str,int clustering) {
		String path="C:\\Users\\YI\\Desktop\\SVM_Trainingfile\\singleword\\カイ二乗1\\"+clustering+"\\data.txt";
		boolean b=false;
		try {
			BufferedWriter writer=new BufferedWriter(new FileWriter(new File(path),true));
			writer.write(str);
			writer.close();
			b=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}
	
	public boolean write(String str) {
		String path="C:\\Users\\YI\\Desktop\\SVM_Trainingfile\\singleword\\カイ二乗1\\data.txt";
		boolean b=false;
		try {
			BufferedWriter writer=new BufferedWriter(new FileWriter(new File(path),true));
			writer.write(str);
			writer.close();
			b=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}
	
	
	public void WritefileBytextid(List<String> list,int clustering) throws SQLException {
		SVM_DatafileByClustering sdbc=new SVM_DatafileByClustering();
		List<String> file= new ArrayList<>();
		String plority=null;
		for (String textid : list) {
			if (sdbc.getLabelData(textid)<0) {
				String label=String.valueOf(adjust(sdbc.getLabelData(textid)));
			    String vector=sdbc.getVectorData(textid);
			    file.add(label+" "+vector);
			    if (label.equals("1")) {
					plority="positive";
				}else if (label.equals("0")) {
					plority="negative";
				}
			}
			
		}
		
		for(String line:file){
			if (sdbc.wirteFile(line,clustering)) {
			   System.out.println(line);				
			}
		}
		
	}
	
	
	
	public static void main(String[] args) throws SQLException {
		SVM_DatafileByClustering sdbc=new SVM_DatafileByClustering();
		
	}
	
	
}
