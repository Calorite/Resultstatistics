import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TextProcessforClustring {

	
	public boolean wirte(String text,String path){
		boolean f=false;
		try{	 		    
	 		   FileOutputStream fos=new FileOutputStream(path, true);
	 		   OutputStreamWriter writer=new OutputStreamWriter(fos,"UTF-8");
	 		   writer.write(text);
	 		   writer.flush();
	 		   f=true;
	 	   }catch(IOException e1){
	 		   e1.printStackTrace();
	 		   System.out.println("write fail");   		   
	 	   }		
			return f;
		
		
	}
	
	public String Getwordsinfo(String path){		
		List<String> text = new ArrayList<String>();
		//WordCounter wc =new WordCounter();
	    List<String> words=new ArrayList<>();
	    String textwords = null;
		try {
			//BufferedReader br = new BufferedReader(new FileReader(path));
			InputStreamReader re=new InputStreamReader(new FileInputStream(path),"UTF-8");
	    	BufferedReader bufferedReader=new BufferedReader(re);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				text.add(line);
			}
			re.close();			
			 Process process = Runtime.getRuntime().exec(new String[] {"I:\\Applications\\Tools\\MeCab\\bin\\mecab.exe", "-Ochasen"});
			 BufferedReader br1 = new BufferedReader(new InputStreamReader(
						process.getInputStream()));
				PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
						process.getOutputStream())));

				for (String line1 : text) {
					//String line4 = new String(line1.getBytes(), "UTF-8"); 
					//System.out.println(line1.getBytes());
					pw.println(line1);  // MeCab へ送信
					pw.flush();
                    String line2;
					while ((line2 = br1.readLine()) != null) {  
						if (line2.equals("EOS")) {  // EOS は解析結果の終了を表す
							break;
						}
					Morpheme m=new Morpheme();	
	                String[] split =line2.split("\t",4);        
					m.a=split[0];
					m.yomi=split[1];
					m.hinshi=split[3];
					m.kihonkei=split[2];   
					
					if(m.hinshi.contains("記号")||m.a.equals("、")||m.a.equals("。")||m.a.equals("null")||m.a.equals(" ")||m.a.equals("<")||m.a.equals(">")||m.a.equals("（")||m.a.equals("）")||m.a.equals(".")||m.a.equals("[")||m.a.equals("）]")||m.a.equals(" ")){
					//、と。をaddしない
					}else{
	                        words.add(m.kihonkei);																						
					}
				  }
					for(String w:words){
						if (textwords==null) {
							textwords="\r\n"+w;
						}else {
							textwords=textwords+" "+w;
						}
						
					}
					
				}  
				br1.close();			
				pw.close();
				process.destroy();
				
		} catch (IOException ex) {
			ex.printStackTrace();
		}
				
		return textwords;
		
	}
	
	public void insertId(String textid) {
		DBhelper helper=new DBhelper();
		String sql="insert into stockdb.textid_clutering value (null,'"+textid+"',null)";
		helper.connSQL();
		if (helper.insertSQL(sql)) {
			System.out.println(textid);
		}
		helper.deconnSQL();		 
	}
	
	public List<String> getId() throws SQLException {
		List<String> list=new ArrayList<>();
		DBhelper helper=new DBhelper();
		String sql="select *  from stockdb.textid_clutering ";
		helper.connSQL();
		ResultSet rs;
		if (helper.selectSQL(sql)!=null) {
			rs=helper.selectSQL(sql);
			while (rs.next()) {
				String id=rs.getString("textid");
				list.add(id);
			}
		}
		helper.deconnSQL();	
		return list;
	}
	
	
	public void procesing(String path) throws IOException, SQLException{
		TextProcessforClustring tp=new TextProcessforClustring();
		File file=new File(path);
		List<String> idlist=new ArrayList<>();
		idlist=tp.getId();
		File[] tempList=file.listFiles();
	 	   for(int i=0;i<tempList.length;i++){
	 	       if(tempList[i].isFile()){
	 	    	    String pathname=tempList[i].getPath();
	 	    	    String textid=pathname.substring(42, 54);
	 	    	    String text=tp.Getwordsinfo(pathname);
	 	    	    if (idlist.contains(textid)) {
	 	    	    					 
					    }else {
							if (tp.wirte(text,"C:\\Users\\YI\\Desktop\\実験データ\\clusteringNews\\news.txt")) {
						        tp.insertId(textid);
						}
					}
	 	    	   
	 	        }
	 	      }
	}
	
	public static void main(String[] args) throws IOException, SQLException {
		//TextProcessforClustring tp=new TextProcessforClustring();
		//tp.procesing("C:\\Users\\YI\\Desktop\\実験データ\\newTrainingNews");
	}
}
