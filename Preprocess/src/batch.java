import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class batch {

	public void insert(Integer id,List<NewsEntity> list) throws SQLException{
		int count = 0;
     String insert_sql = "INSERT INTO stockdb.clustring_textid_label(clustering,id,newsid,label) VALUES (?,?,?,?)";
     String url="jdbc:mysql://127.0.0.1/classdata?characterEncoding=utf8&useSSL=true";
     String username="root";
     String password="474950494";
     Connection connection=java.sql.DriverManager.getConnection(url,username,password);
     connection.setAutoCommit(false);
     try{
    	 PreparedStatement statement=connection.prepareStatement(insert_sql);
     //PreparedStatement psts = conn.prepareStatement(insert_sql); 
     if (list!=null) {
    	int sum=0;
		for (NewsEntity nEntity:list) { 
			sum++;
			//statement.setString(1, null);  
			statement.setInt(1, id);  
			statement.setInt(2, sum);
			statement.setString(3, nEntity.newsid);
			statement.setDouble(4, nEntity.label);		
			statement.addBatch();           
         count++;              
     }  
		statement.executeBatch();  
     connection.commit();   
     System.out.println("All down : " + count);  
     connection.close();  
     }
	   }catch(SQLException e){
		   System.out.println("connect DB fail!");
		   e.printStackTrace();
	   }
	}
}
