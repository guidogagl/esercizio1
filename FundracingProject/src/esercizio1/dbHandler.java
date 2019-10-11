package esercizio1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class dbHandler {

	private Boolean insert(String table, Vector<String> tableField, Vector<String> values, int numRow) {
		// format: INSERT INTO tablename SET field1 = ?, field2 = ? ;
		
		String insertStr = "INSERT INTO " + table + " SET ";
		
		for(int i=0; i < tableField.capacity(); i++)
			insertStr += tableField.get(i) + " = " + values.get(i) + ", ";
		
		Connect conn = new Connect();
		
		ResultAndRows result = conn.query(insertStr, values);
		
		conn.close();
		
		if( result == null || result.nrow_ < numRow) {
			// handle error
			
			return false;
		}
		
		return true;
	}

	public dbHandler(){
		
	}

	public Boolean insertProject(Vector<String> val) {
		Vector<String> tf = new Vector<String>(5);
		
		tf.add("id");
		tf.add("nome");
		tf.add("budget");
		tf.add("descrizione");
		tf.add("azienda");
		
		int nR = val.capacity()/5;
		
		return insert("progetto", tf, val, nR);
	}

	public Boolean deleteProject(int projectId){
		String deleteStr = "DELETE FROM progetto WHERE id = " + projectId;
		
		Connect conn = new Connect();
	
		ResultAndRows result = conn.query(deleteStr);
		
		conn.close();
		
		if(result.nrow_ == 1) return true;
		
		return false;
	}	
	
	public Boolean insertStake(Vector<String> val) {
		Vector<String> tf = new Vector<String>(4);
		
		tf.add("id");
		tf.add("budget");
		tf.add("azienda");
		tf.add("progetto");
		
		int nR = val.capacity()/4;
		
		return insert("finanziamento", tf, val, nR);
	}
	
	public Boolean updateStake(int stakeId, int stakeBudget) {
		String updateStr = "UPDATE finanziamento SET budget = " + stakeBudget + " WHERE id = " + stakeId;
		
		Connect conn = new Connect();
	
		ResultAndRows result = conn.query(updateStr);
		
		conn.close();
		
		if(result.nrow_ == 1) return true;
		
		return false;
	}
	
	public Boolean deleteStake(int stakeId) {
		String deleteStr = "DELETE FROM finanziamento WHERE id = " + stakeId;
		
		Connect conn = new Connect();
	
		ResultAndRows result = conn.query(deleteStr);
		
		conn.close();
		
		if(result.nrow_ == 1) return true;
		
		return false;
	}
	
	public Boolean insertAgency(Vector<String> val) {
		Vector<String> tf = new Vector<String>(4);
		
		tf.add("nomeAzienda");
		tf.add("urlLogo");
		tf.add("urlSito");
		tf.add("indirizzo");
		tf.add("cap");
		
		int nR = val.capacity()/5;
		
		return insert("azienda", tf, val, nR);
	}

	public ResultSet getAgency(String agencyName) {
		String sqlStr = "SELECT * FROM azienda WHERE nomeAzienda = '" + agencyName+ "'";
		System.out.println(sqlStr);
		
		Connect conn = new Connect();
		
		ResultAndRows result = conn.query(sqlStr);
		
		conn.close();
		
		if(result.nrow_ == 1) return result.rs_;
		
		return null;
	}

	public List<RowTableProjects> getProject() {
		String sqlStr = "select	p.id as id_project, p.nome, p.budget, f.budget, f.azienda\r\n" + 
				"from	progetto as p\r\n" + 
				"		inner join\r\n" + 
				"        finanziamento as f\r\n" + 
				"        on p.id = f.progetto\r\n" + 
				"order by p.id;";
		
		//System.out.println(sqlStr);
		
		
		//ResultAndRows result = conn.query(sqlStr);
		
		//ResultSet results = rs.rs_;
		
		List<RowTableProjects> ret = new ArrayList<RowTableProjects>();
		
		try {
			Connection conn = null;
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/esercizio1?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=root&password=root");
			PreparedStatement pstm = conn.prepareStatement(sqlStr);
			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {
				ret.add(new RowTableProjects(rs.getInt("id_project"), rs.getString("nome"), "ciao", rs.getInt("budget"), rs.getInt("budget"), rs.getString("azienda")));
				//System.out.println(rs.getInt("id_project"));
			}
			
			conn.close();
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return ret;
		
	}
	
	/*public List<RowTableProjects> listProjects(ResultSet rs){
		
		List<RowTableProjects> ret = new ArrayList<RowTableProjects>();
		//ResultSet results = rs.rs_;
		
		try {
			while(rs.next()) {
				ret.add(new RowTableProjects(rs.getInt("id_project"), rs.getString("name_project"), "ciao", rs.getInt("total_budget"), rs.getInt("budget"), rs.getString("azienda")));
				System.out.println(rs.getInt("id_project"));
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	
		return ret;
	}*/
}
