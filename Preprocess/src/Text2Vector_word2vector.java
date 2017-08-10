import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class Text2Vector_word2vector {

	
	DBhelper helper=new DBhelper();
	public List<String> getWords(String newsid) throws SQLException {
		List<String> words=new ArrayList<>();
		//DBhelper helper=new DBhelper();
		String sql="SELECT * FROM stockdb.textdata_final where textid='"+newsid+"';";
		ResultSet rs;
		helper.connSQL();
		if ((rs=helper.selectSQL(sql))!=null) {
			while (rs.next()) {
				String word=rs.getString("word");
				words.add(word);
			}
		}
		helper.deconnSQL();
		return words;
	}

	public String getWordVectorData(String word) throws SQLException {
		String vector = null;
		//DBhelper helper=new DBhelper();
		ResultSet rs;
		String sql="SELECT * FROM stockdb.word_vector where word='"+word+"';";
		//helper.connSQL();
		if (helper.selectSQL(sql)!=null) {
			rs=helper.selectSQL(sql);
			if(rs.next()) {

				vector=rs.getString("vector");
			}
		}
		//helper.deconnSQL();

		return vector;
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

	private boolean getPlority(String word) throws SQLException {
		Boolean f=false;
		//DBhelper helper=new DBhelper();
		//helper.connSQL();
		String sql="SELECT * FROM stockdb.words_polarity where word='"+word+"';";
		ResultSet rs;

		if (helper.selectSQL(sql)!=null) {
			rs=helper.selectSQL(sql);
			if (rs.next()) {
				String plority=rs.getString("polarity");
				if (plority.equals("p")|plority.equals("n")) {
					f=true;
				}
			}

		}

		//helper.deconnSQL();
		return f;

	}

	public List<String> targetWords(String thString) throws SQLException {
		List<String>  list=new ArrayList<>();
		//DBhelper helper=new DBhelper();
		if (thString.equals("Ç»Çµ")) {
			thString="0";
		}
		String sql="SELECT * FROM stockdb.words_chi where chi>"+thString+";";
		//helper.connSQL();
		ResultSet rs;
		if (helper.selectSQL(sql)!=null) {
			rs=helper.selectSQL(sql);
			while (rs.next()) {
				String word=rs.getString("word");
				list.add(word);
			}
		}
		//helper.deconnSQL();
		return list;
		
 	}
	
	
	
	
	

	public static void main(String[] args) throws SQLException {
		SVM_DatafileByClustering sdbc=new SVM_DatafileByClustering();
		Text2Vector_word2vector tvwv=new Text2Vector_word2vector();
		
		List<String> thresholdlist=new ArrayList<>();
        thresholdlist.add("0");
		for (String three : thresholdlist) {
			tvwv.helper.connSQL();
			List<String> targetwords=tvwv.targetWords(three);
			List<String> vector=new ArrayList<>();
			
			for (int i = 7; i < 11; i++) {
				List<String> newslist=sdbc.getTargetNewsID(i);
				List<NewsEntity> labellist=new ArrayList<>();
				int nsum=0;
				int psum=0;
				int sum=0;
				List<NewsEntity> nStrings=new ArrayList<>();
				List<NewsEntity> pStrings =new ArrayList<>();
				String text="";
				int lineid=0;
				for (String id : newslist) {
					NewsEntity newsEntity=new NewsEntity();
					int N=0;
					lineid++;
					List<String> wordsvector=new ArrayList<>();
					List<String> words =tvwv.getWords(id);
					List textvector = null;
					if (words!=null) {
						for (String string : words) {
							//ëŒè€äOíPåÍÇçÌèú
							if (targetwords.contains(string)) {
								String wordvector=tvwv.getWordVectorData(string);
								if (wordvector!=null) {
									N++;
									wordsvector.add(wordvector);
									System.out.println(string);
								}
							}

						}

						if (N==0) {
							System.out.println("no word!");
						}
						int vecsize = 0;
						Double[] newsvec=new Double[200];//
						if (wordsvector!=null) {
							int index=0;
							for (String vec : wordsvector) {
								index++;
								String[] wordvec=vec.split(",");
								vecsize=wordvec.length;
								if (index==1) {//àÍå¬ñ⁄ÇÃíPåÍ
									for (int j = 0; j < vecsize; j++) {
										newsvec[j]=Double.valueOf(wordvec[j]);
									}
								}else{
									for (int j = 0; j < vecsize; j++) {								
										newsvec[j]=newsvec[j]+Double.valueOf(wordvec[j]);											
									}
								}
							}
							textvector=Arrays.asList(newsvec);
						}
					}
					String newsvector=null;
					int T=0;
					if (textvector!=null) {
						for (Object object : textvector) {
							if (object!=null) {
								T++;
								//int n=wordsvector.size();
								Double value=(Double)object/Double.valueOf(N);
								if (T==1) {
									newsvector="1:"+String.valueOf(value);
								}else {
									newsvector=newsvector+" "+String.valueOf(T)+":"+String.valueOf(value);
								}
							}
						}
						int label=adjust(sdbc.getLabelData(id));
						String writestr=String.valueOf(label)+" "+newsvector;
						newsEntity.newsstring=writestr;
						newsEntity.label=sdbc.getLabelData(id);
						newsEntity.newsid=id;
						if (label==-1) {
							nsum++;

							nStrings.add(newsEntity);
							//sdbc.wirteFile(writestr, i,"negative");
						}else if (label==1) {
							psum++;
							pStrings.add(newsEntity);
							//sdbc.wirteFile(writestr, i,"positive");
						}
					}
				}
				NewsEntity[] negativeStr=new NewsEntity[nStrings.size()];
				nStrings.toArray(negativeStr);
				NewsEntity[] positiveStr=new NewsEntity[pStrings.size()];
				pStrings.toArray(positiveStr);
				if (nsum<psum) {
					sum=nsum;		
				}else {
					sum=psum;
				}
				for (int j = 0; j < sum; j++) {
					NewsEntity nenegative=new NewsEntity();
					NewsEntity nepositive=new NewsEntity();
					if (j==0) {
						text=negativeStr[j].newsstring+"\r\n"+positiveStr[j].newsstring;
					}else {
						text=text+"\r\n"+negativeStr[j].newsstring+"\r\n"+positiveStr[j].newsstring;
					}

					nenegative.label=negativeStr[j].label;
					nenegative.newsid=negativeStr[j].newsid;
					nepositive.label=positiveStr[j].label;
					nepositive.newsid=positiveStr[j].newsid;
					labellist.add(nenegative);
					labellist.add(nepositive);
				}
				batch in=new batch();
				in.insert(i, labellist);
				sdbc.write(text);
			}
		   tvwv.helper.deconnSQL();	
		}

		
	}
}

