package esercizio1;

import java.sql.ResultSet;

public class ResultAndRows {
	public ResultSet rs_ = null;
	public int nrow_ = -1;
	
	public ResultAndRows(ResultSet rs, int nrow) {
		rs_ = rs;
		nrow_ = nrow;
	}
}
