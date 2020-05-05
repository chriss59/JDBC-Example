package jdbcExample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Example {

	//https://github.com/chriss59/JDBC-Example

	// JDBC driver name und URL der Datenbank angeben
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/EMP";

	//Passwort und Benutzername angeben
	static final String USER = "username";
	static final String PASS = "password";

	public static void main(String[] args) {

		Connection conn = null;
		Statement stmt = null;
		try{
			//Registrieren JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			//eine connection öffnen
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			//Ein query ausführen
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT id, first, last, age FROM Employees";
			ResultSet rs = stmt.executeQuery(sql);

			//Daten aus rs(ResultSet) ausgeben in einer while schleife.
			while(rs.next()){
				//Retrieve by column name
				int id  = rs.getInt("id");
				int age = rs.getInt("age");
				String first = rs.getString("first");
				String last = rs.getString("last");

				//Display values
				System.out.print("ID: " + id);
				System.out.print(", Age: " + age);
				System.out.print(", First: " + first);
				System.out.println(", Last: " + last);
			}

			//mysql insert statement
			String query1 = " insert into Employees (age, first, last)"
					+ " values (?, ?, ?)";

			//Erstelle den mysql insert preparedstatement
			java.sql.PreparedStatement preparedStmt = conn.prepareStatement(query1);
			//preparedStmt.setInt (1, 22); //set id
			preparedStmt.setInt (1, 43); //set Age
			preparedStmt.setString (2, "Max"); //set first
			preparedStmt.setString (3, "Mustermann"); //set last

			//Erstelle eine Tabelle und führe es aus
			String sqlTable = "CREATE TABLE REGISTRATION " +
					"(id INTEGER not NULL, " +
					" first VARCHAR(255), " + 
					" last VARCHAR(255), " + 
					" age INTEGER, " + 
					" PRIMARY KEY ( id ))"; 

			stmt.executeUpdate(sqlTable);

			//preparedstatement wird ausgeführt
			preparedStmt.execute();

			//Verbindungen etc. wieder beenden bzw. schließen.
			rs.close();
			stmt.close();
			conn.close();
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block um nochmal sicher zu gehen das die Verbindung getrennt wurde. Falls nicht,
			//wird die Verbindung nochmal versucht zu trennen
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try
		System.out.println("Goodbye!");
	}//end main

}

//Quelle: https://www.tutorialspoint.com/jdbc/jdbc-sample-code.htm
