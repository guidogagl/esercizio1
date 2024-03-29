package esercizio1;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DepositoDati {
	
	private Connection conn;
	private String connStr = "jdbc:mysql://localhost:3306/esercizio1?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=root&password=root";
	
	public DepositoDati() {
		try {
			conn=DriverManager.getConnection(connStr);
		}catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<RowTableProjects> getProjects(String agencyName){

		String sqlStr = "select	f.progetto as id_project, p.nome, p.budget, sum(f.budget) as stake, f.azienda, (sum(f.budget)/p.budget)*100 as progress\r\n" + 
				"from	progetto as p\r\n" + 
				"		inner join\r\n" + 
				"        finanziamento as f\r\n" + 
				"        on p.id = f.progetto\r\n" +
				"where p.azienda = (?)\r\n" +
				"group by f.progetto;";
		
		String sqlStr2 = "select	f.progetto as id_project, p.nome, p.budget, sum(f.budget) as stake, f.azienda, (sum(f.budget)/p.budget)*100 as progress\r\n" + 
				"from	progetto as p\r\n" + 
				"		inner join\r\n" + 
				"        finanziamento as f\r\n" + 
				"        on p.id = f.progetto\r\n" +
				"where p.azienda != (?)\r\n" +
				"and f.azienda=(?)\r\n"+
				"group by f.progetto;";
		
		String sqlStr3 = "select	f.progetto as id_project, p.nome as nome, (sum(f.budget)/p.budget)*100 as progress, p.budget, 0 as stake, p.azienda\r\n" + 
				"				from	progetto as p\r\n" + 
				"						inner join\r\n" +
				"                        finanziamento as f\r\n" + 
				"                        on p.id = f.progetto\r\n" + 
				"				where	p.id not in (\r\n" + 
				"					select	p.id\r\n" + 
				"					from	progetto as p\r\n" + 
				"							inner join \r\n" + 
				"					        finanziamento as f\r\n" + 
				"					        on p.id = f.progetto\r\n" + 
				"					where p.azienda = (?)\r\n" + 
				"                ) and p.id not in (\r\n" + 
				"					select	p.id\r\n" + 
				"					from	progetto as p\r\n" + 
				"							inner join \r\n" + 
				"					        finanziamento as f\r\n" + 
				"					        on p.id = f.progetto\r\n" + 
				"					where f.azienda = (?)\r\n" + 
				"						and p.azienda != (?)\r\n" + 
				"                )\r\n" + 
				"group by	f.progetto, f.azienda;";
		
		List<RowTableProjects> ret = new ArrayList<RowTableProjects>();
		
		try {
			PreparedStatement pstm = conn.prepareStatement(sqlStr);
			pstm.setString(1, agencyName);
			ResultSet rs = pstm.executeQuery();
			
			PreparedStatement pstm2 = conn.prepareStatement(sqlStr2);
			pstm2.setString(1, agencyName);
			pstm2.setString(2, agencyName);
			ResultSet rs2 = pstm2.executeQuery();
			PreparedStatement pstm3 = conn.prepareStatement(sqlStr3);
			pstm3.setString(1, agencyName);
			pstm3.setString(2, agencyName);
			pstm3.setString(3, agencyName);
			ResultSet rs3 = pstm3.executeQuery();
			
			while(rs.next()) {
				ret.add(new RowTableProjects(rs.getInt("id_project"), rs.getString("nome"), rs.getString("progress"), rs.getInt("budget"), rs.getInt("stake"), rs.getString("azienda")));
			}
			
			while(rs2.next()) {
				ret.add(new RowTableProjects(rs2.getInt("id_project"), rs2.getString("nome"), rs2.getString("progress"), rs2.getInt("budget"), rs2.getInt("stake"), rs2.getString("azienda")));
			}
			
			
			while(rs3.next()) {
				ret.add(new RowTableProjects(rs3.getInt("id_project"), rs3.getString("nome"), rs3.getString("progress"), rs3.getInt("budget"), rs3.getInt("stake"), rs3.getString("azienda")));
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}
	
	public List<RowTableProjects> getProjectsWithoutStake(){
		String sqlStr = "select	f.progetto as id_project, f.azienda as nome, (sum(f.budget)/p.budget)*100 as progress, p.budget, p.azienda\r\n" + 
				"from	progetto as p\r\n" + 
				"		inner join\r\n" + 
				"        finanziamento as f\r\n" + 
				"        on p.id = f.progetto\r\n" + 
				"group by f.progetto;";
		List<RowTableProjects> ret = new ArrayList<RowTableProjects>();
		try {
			PreparedStatement pstm = conn.prepareStatement(sqlStr);
			ResultSet rs = pstm.executeQuery();
			
			while(rs.next()) {
				ret.add(new RowTableProjects(rs.getInt("id_project"), rs.getString("nome"), rs.getString("progress"), rs.getInt("budget"), 0, rs.getString("azienda")));
			}
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}
	
	
	public Integer getIdLastProject() {
		String str = "select	p.id\r\n" + 
				"from	progetto as p\r\n" + 
				"order by p.id desc\r\n" + 
				"limit 1;";
		Integer id = 0;
		
		try {
			PreparedStatement pstm=conn.prepareStatement(str);
			ResultSet res = pstm.executeQuery();
			
			while(res.next()) {
				id = res.getInt("id");
			}
			
		  }catch(SQLException e) {
			  System.out.println(e.getMessage());
		  }
		
		return id;
	}
	
	
	
	public void insertProject(Vector<String>val) {
	  String insertProject="INSERT INTO progetto (nome,budget,descrizione,azienda) values ((?),(?),(?),(?))";
	  String insertFinanziamento =  "INSERT INTO finanziamento (budget,azienda,progetto) values ((?),(?),(?))";
	  
	  
	  try {
		  PreparedStatement pstm=conn.prepareStatement(insertProject);
		  pstm.setString(1,val.get(0)); 
		  pstm.setInt(2, Integer.parseInt(val.get(1)));
		  pstm.setString(3, val.get(2));
		  pstm.setString(4,val.get(3));
		  pstm.execute();
		  
		  int id_project = getIdLastProject();
		  
		  PreparedStatement pstm2=conn.prepareStatement(insertFinanziamento);
		  pstm2.setInt(1, 0);
		  pstm2.setString(2, val.get(3));
		  pstm2.setInt(3, id_project);
		  pstm2.execute();
		  
		  System.out.println("agency: "+val.get(3)+ " id: "+id_project);
		  
	  }catch(SQLException e) {
		  System.out.println(e.getMessage());
	  }
	}
	
	
	public boolean sonoProprietario(int projectId,String agencyName) {
		boolean check=false;
		int numeroOccorrenze=0;
		String checkProprietarioQuery="SELECT COUNT(*) as conta FROM progetto WHERE id=(?) and azienda=(?)";
		try 
		{
			PreparedStatement pstm=conn.prepareStatement(checkProprietarioQuery);
			pstm.setInt(1, projectId);
			pstm.setString(2, agencyName);
			ResultSet res = pstm.executeQuery();
			
			while(res.next()) {
				numeroOccorrenze=res.getInt("conta");
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		if(numeroOccorrenze>0)
			check=true;
		return check;
	}
	
	
	public void deleteProject(int projectId) {
		String deleteProjectQuery="DELETE FROM progetto WHERE id =(?)";
		String deleteFinanziamentoQuery = "DELETE FROM finanziamento WHERE progetto = (?)";
		
		try {
			PreparedStatement pstm=conn.prepareStatement(deleteProjectQuery);
			pstm.setInt(1, projectId);
			pstm.execute();
			
			PreparedStatement pstm2=conn.prepareStatement(deleteFinanziamentoQuery);
			pstm2.setInt(1, projectId);
			pstm.execute();
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void updateStake(int stakeBudget, String agencyName, int idProgetto) {
		String updateStr="INSERT INTO finanziamento (budget, azienda, progetto) VALUES ((?), (?), (?))";
		try {
			PreparedStatement pstm=conn.prepareStatement(updateStr);
			pstm.setInt(1,stakeBudget);
			pstm.setString(2,agencyName);
			pstm.setInt(3,idProgetto);
			pstm.executeUpdate();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*public void deleteStake(int stakeId) {
		String deleteStr="DELETE FROM finanziamento WHERE id =(?)";
		try {
			PreparedStatement pstm=conn.prepareStatement(deleteStr);
			pstm.setInt(1, stakeId);
			pstm.execute();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}*/
	
	
	public Vector<String> getAgency(String agencyName) {
		
		Vector<String> vector = new Vector<String>();
		String str = "SELECT * FROM azienda WHERE nomeAzienda = (?)";
		
		try {
			
			PreparedStatement pstm=conn.prepareStatement(str);
			pstm.setString(1, agencyName);
			ResultSet res = pstm.executeQuery();
			
			
			vector.clear();
			
			while(res.next()) {
				vector.add(res.getString("nomeAzienda"));
				vector.add(res.getString("urlLogo"));
				vector.add(res.getString("urlSito"));
				vector.add(res.getString("indirizzo"));
				vector.add(res.getString("cap"));
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return vector;
	}
	
	public String getDescriptionProject(int id_project) {
		String str = "SELECT descrizione FROM progetto WHERE id = (?)";
		String desc = "";

		try {
					
					PreparedStatement pstm=conn.prepareStatement(str);
					pstm.setInt(1, id_project);
					ResultSet res = pstm.executeQuery();
					
					while(res.next()) {
						desc = res.getString("descrizione");
					}
					
				}catch(SQLException e) {
					System.out.println(e.getMessage());
				}
		return desc;
	}
	
	public Boolean myStake(String agencyName, int id_project) {
		
		int numeroOccorrenze = 0;
		Boolean check = false;
		
		String str = "SELECT count(*) as conta FROM finanziamento WHERE progetto = (?) and azienda = (?);";
		
		try {
			
			PreparedStatement pstm=conn.prepareStatement(str);
			pstm.setInt(1, id_project);
			pstm.setString(2, agencyName);
			ResultSet res = pstm.executeQuery();
			
			while(res.next()) {
				numeroOccorrenze=res.getInt("conta");
			}
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		if(numeroOccorrenze>0)
			check=true;
		return check;
	}
	
	
	public void deleteMyStakes(int projectId,String agencyName) {

		String deleteMyStakesQuery = "DELETE FROM finanziamento WHERE progetto = (?) and azienda = (?)";
		
		try {
			PreparedStatement pstm=conn.prepareStatement(deleteMyStakesQuery);
			pstm.setInt(1, projectId);
			pstm.setString(2,agencyName);
			pstm.execute();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	/*public String getUrl(String agencyName) {
		
		String urlLogo = "";
		
		String strQuery = "SELECT urlLogo FROM azienda WHERE nomeAzienda = (?);";
		
		try {
			PreparedStatement pstm=conn.prepareStatement(strQuery);
			pstm.setString(1, agencyName);
			ResultSet res = pstm.executeQuery();
			
			while(res.next()) {
				urlLogo = res.getString("urlLogo");
			}
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return urlLogo;
	}*/
}
