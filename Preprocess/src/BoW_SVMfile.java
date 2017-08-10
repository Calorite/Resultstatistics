import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoW_SVMfile {
	DBhelper helper=new DBhelper();
	
	
	private String getlabeldata(String id) throws SQLException {
		
		String label="";
		String sql="SELECT * FROM stockdb.learning_data where newsid="+id+";";
		ResultSet rs;
		if (helper.selectSQL(sql)!=null) {
			rs=helper.selectSQL(sql);
			if (rs.next()) {
				if (rs.getDouble("label")>0) {
					label="1";
				}else {
					label="-1";
				}
			}
		}
		return label;
	}
	
	
	public String TextExpressionBow(String textid,List<String> wordlist,String label) throws SQLException {
		String text="";
		BoW_SVMfile bsf=new BoW_SVMfile();
		Text2Vector_word2vector tvwv=new Text2Vector_word2vector();
		
		List<String> words=tvwv.getWords(textid);
		List<Integer> dimension=new ArrayList<>();
		//String labeldata = bsf.getlabeldata(textid);
		
		int n=0;
		for (String string : wordlist) {
			n++;
			if (words.contains(string)) {
				dimension.add(n);
			}
		}
		int i=0;
		for (Integer integer : dimension) {
			if (i==0) {
				text=label+" ";
			}else {
				text=text+String.valueOf(integer)+":1 ";
			}
			i++;
		}
		return text;
	}
	
	
	
	public static void main(String[] args) throws SQLException {
		
		BoW_SVMfile bsf=new BoW_SVMfile();
		//bsf.helper.connSQL();
		Text2Vector_word2vector tvwv=new Text2Vector_word2vector();
		SVM_DatafileByClustering sdbc=new SVM_DatafileByClustering();
		List<String> targetwords=new ArrayList<>();
		List<String> targetnews=new ArrayList<>();		
		
		tvwv.helper.connSQL();
		targetwords=tvwv.targetWords("1");//‘S’PŒê
		tvwv.helper.deconnSQL();
		for(int j=1;j<11;j++){
		try {
			String text="";
			targetnews=sdbc.getTargetNewsID(j);
			List<String> positivelist=new ArrayList<>();
			List<String> negativelist=new ArrayList<>();
			int min=10000;			
			bsf.helper.connSQL();
			for (String id : targetnews) {
				if (bsf.getlabeldata(id).equals("1")) {
					positivelist.add(bsf.TextExpressionBow(id, targetwords,"1"));
				}else if (bsf.getlabeldata(id).equals("-1")) {
					negativelist.add(bsf.TextExpressionBow(id, targetwords,"-1"));
				}

			}
			
			bsf.helper.deconnSQL();
			if (positivelist.size()>negativelist.size()) {
				min=negativelist.size();
			}else {
				min=positivelist.size();
			}
			
		    for (int i = 0; i < min; i++) {
				text=text+positivelist.get(i)+"\r\n"+negativelist.get(i)+"\r\n";
			}
			
		    sdbc.wirteFile(text, j);
		 //   sdbc.write(text);
		//bsf.helper.deconnSQL();	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}
}
