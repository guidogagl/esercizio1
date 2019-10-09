package esercizio1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Connect {
	
	private String connStr = "jdbc:mysql://localhost:3306/foundraising_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=root&password=gagliardi";
	private Connection conn = null;
	
	public Connect(){
			try {
				conn = DriverManager.getConnection(connStr);
				
				
			} catch(SQLException ex) {
				System.out.println("SQLException: " + ex.getMessage() );
				System.out.println("SQLState: " + ex.getSQLState() );
				System.out.println("VendorError: " + ex.getErrorCode() );	
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
	
	public ResultAndRows query(String queryString) {
		System.out.print("in esecuzione la query: " + queryString);
		
		try {
			Statement stmt = conn.createStatement();
			
			int numRows =  stmt.executeUpdate(queryString);
			ResultSet rs = stmt.getResultSet();
			
			stmt.close();
			
			
			return new ResultAndRows(rs, numRows);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		
	}
	
	public ResultAndRows query(String queryString, Vector<String> data) {
		System.out.print("in esecuzione la query: " + queryString);
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(queryString);
			
			for(int i=0; i < data.capacity() ; i++)
				pstmt.setString(i, data.get(i));
			
			int numRows = pstmt.executeUpdate();
			System.out.print("number of row affected: " + numRows);
			
			ResultSet rs = pstmt.getResultSet();
			
			pstmt.close();
			
			return new ResultAndRows(rs, numRows);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		return;
	}
}
