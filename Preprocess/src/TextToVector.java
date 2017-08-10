import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

public class TextToVector {
	
	
	public static String[] listToArray(List<String> features) {
		String [] array=new String[features.size()];
		features.toArray(array);
		return array;
	}

	public static String ArrayToString(int[] array) {
		String vector=null;
		for(int i=0;i<array.length;i++){
			if (i==0) {
				if (array[i]!=0){
				vector=String.valueOf(i)+":"+String.valueOf(array[i]);
				}else {
					vector="";
				}
			}else if (array[i]!=0) {
				if (vector.equals("")) {
					vector=String.valueOf(i)+":"+String.valueOf(array[i]);
				}else {
					vector=vector+' '+String.valueOf(i)+":"+String.valueOf(array[i]);
				}
				
			}		
		}		
		return vector;
	}
	
	public int[] getVector(String textid,List<String> features) throws SQLException{
		//String vector=null;
		DBhelper helper=new DBhelper();
		helper.connSQL();		
		String sql="select * from stockdb.combination_data where textid='"+textid+"'";
		ResultSet rs;
		List<String> words=new ArrayList();
		if(helper.selectSQL(sql)!=null){
			rs=helper.selectSQL(sql);
			while(rs.next()){
				String word=new String();
				word=rs.getString("word");
				//word.times=rs.getInt("times");
				words.add(word);
			}		
		}
		int n=0;
		String[] array= new String[features.size()];
		array=listToArray(features);
		int[] vector=new int[features.size()];
		for(String word:words){
		for(int i=0;i<features.size();i++){
			if (word.equals(array[i])) {
				vector[i]=1;
				System.out.println("!!!!!!!!!!");
			}
		}
		}		
		helper.deconnSQL();
		return vector;
		
	}
	
		
	
	public List<String> getFeatures() throws SQLException {
		String sql="SELECT * FROM stockdb.combination;";
		List<String> list=new ArrayList<String>();
		DBhelper helper=new DBhelper();
		helper.connSQL();
		ResultSet rs;
		if(helper.selectSQL(sql)!=null){
			rs=helper.selectSQL(sql);
				while (rs.next()){			
				String fword=rs.getString("combination");
				list.add(fword);
				
				}
				return list;			
		}
		helper.deconnSQL();
		return null;
		
	}
	
	public String getCompanyID(String path) {
		String id=null;
		Pattern pattern=Pattern.compile("[0-9]{4}");
		Matcher matcher=pattern.matcher(path);
		if(matcher.find()){
			return matcher.group(0);
		}		
		return id;
	}
	
	public static String getNewsID(String news) {
		String id=null;
		Pattern pattern=Pattern.compile("[0-9]{12}");
		Matcher matcher=pattern.matcher(news);
		if(matcher.find()){
			return matcher.group(0);
		}		
		return id;
	}
	
	
	public boolean insertTextVector(String textid,String  vector) {
		DBhelper helper=new DBhelper();
		helper.connSQL();
		boolean f=false;
		String sql="insert into stockdb.testnews_vector_combination value('"+textid+"','"+vector+"')";
		System.out.println("!!!");
		if(helper.insertSQL(sql)){
			f=true;
		}		
		helper.deconnSQL();
		return f;
	}
	
	public List<String> getTextIDList(String sql) throws SQLException {//by table
		List<String> list=new ArrayList<String>();
		DBhelper helper=new DBhelper();
		helper.connSQL();
		ResultSet rs;
		if (helper.selectSQL(sql)!=null) {
			rs=helper.selectSQL(sql);
			while (rs.next()) {
				String newsid=getNewsID(rs.getString("newsid"));
				list.add(newsid);
			}
		}
		helper.deconnSQL();
		return list;
	}
	
	
	
	public static void main(String[] arg) throws SQLException{
		String vector;
		String sql="SELECT * FROM stockdb.test_data;";//news path		
		TextToVector ttv=new TextToVector();
		List<String> combinations=new ArrayList<String>();
		combinations=ttv.getFeatures();
		List<String> list=new ArrayList<String>();
		list=ttv.getTextIDList(sql);
		File file=new File("C:\\Users\\YI\\Desktop\\é¿å±ÉfÅ[É^\\newTrainingNews");
		File[] tempList=file.listFiles();
	 	   for(int i=0;i<tempList.length;i++){
	 	       if(tempList[i].isFile()){
	 	    	  String filePath=tempList[i].getPath();
	 		      String id=filePath.substring(42,54);
	 		      if (list.contains(id)) {
	 		    	 try {
	 					int[] combination=ttv.getVector(id, combinations);
	 					 String vector1=ArrayToString(combination);
	 					//int[] word=ttv.getVectorfromword(textid, words); 
	 					// String vector2=ArrayToString(word);
	 					 vector=vector1;
	 					if (ttv.insertTextVector(id,vector)) {
	 						System.out.println("insert featrue vector suceed!");
	 					}else{
	 						System.out.println("insert feature vector failed");
	 					}
	 				} catch (SQLException e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 				}
				}
	 	    }
	 	}
	}
}
