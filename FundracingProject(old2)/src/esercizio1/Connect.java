package esercizio1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Connect {
	
	private String connStr = "jdbc:mysql://localhost:3306/esercizio1?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&user=root&password=root";
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
			int numRows = 0;
			
			Statement stmt = conn.createStatement();
			
			//int numRows =  stmt.executeUpdate(queryString);
			stmt.execute(queryString);
			
			ResultSet rs = stmt.getResultSet();
			
			while(rs.next()) {
				numRows++;
			}
		
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
			
			System.out.println("Vector capacity: " + data.capacity());
			
			for(int i=0; i < data.capacity() ; i++) {
				System.out.println("i: "+ i);
				System.out.println("data: " + data.get(i));
				pstmt.setString(i, data.get(i));
			}
			
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
