import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class testnews_clustering_ToDB {

	DBhelper heBhelper=new DBhelper();
	
	public static List<ClusteringData> getlist() throws SQLException {
		List<ClusteringData> list=new ArrayList<>();
		DBhelper helper=new DBhelper();
		helper.connSQL();
		ResultSet rs;
		String sql="SELECT * FROM stockdb.textid_clutering;";
		if ((rs=helper.selectSQL(sql))!=null) {
			while (rs.next()) {
				ClusteringData cd = new ClusteringData();
				String textid=rs.getString("textid");
				int id=rs.getInt("clustering");
				cd.clustering=id;
				cd.textid=textid;
				list.add(cd);
			}
		}
		helper.deconnSQL();	
		
		return list;
	}
	
	public void insert(ClusteringData cData) {
		testnews_clustering_ToDB tct=new testnews_clustering_ToDB();
		DBhelper helper=new DBhelper();
		helper.connSQL();
		String sql="insert into stockdb.testnews_clustering value("+cData.clustering+",'"+cData.textid+"');";
		if (helper.insertSQL(sql)) {
			System.out.println(cData.textid);
		}else {
			System.out.println("can't insert!");
		}
		helper.deconnSQL();
	}
			 
	public List<String> getlearning() throws SQLException {
		List<String> list=new ArrayList<>();
		DBhelper helper=new DBhelper();
		helper.connSQL();
		ResultSet rs;
		String sql="SELECT * FROM stockdb.learning_data;";
		if ((rs=helper.selectSQL(sql))!=null) {
			while (rs.next()) {
				//ClusteringData cd = new ClusteringData();
				String textid=rs.getString("newsid");
				list.add(textid);
			}
		}
		helper.deconnSQL();	
		
		return list;
	}
	
	public static void main(String args[]) throws SQLException {
		testnews_clustering_ToDB tct=new testnews_clustering_ToDB();
		tct.heBhelper.connSQL();		
		List<ClusteringData> list=getlist();
		List<String> learninglist=tct.getlearning();
		for (ClusteringData cData:list) {
			if (learninglist.contains(cData.textid)) {
				
			}else {
				tct.insert(cData);	
			}
					
		}
		tct.heBhelper.deconnSQL();
	}
}
