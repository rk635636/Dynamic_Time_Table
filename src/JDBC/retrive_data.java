package JDBC;
import java.sql.*;
import java.util.ArrayList;

import TimeTable.ClassRoom;
import TimeTable.Professor;
import TimeTable.Subject;  
public class retrive_data{  

	public Object[][] resultSet = new Object[6][5];
	public Object[][] resultSet1 = new Object[25][3];
	public Object[][] resultSet2 = new Object[9][5];
	
	public void getData(int n)
	{
		try{  
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DynamicTimeTable","root","rk635636");
			
			Statement st = conn.createStatement();
			Statement st1 = conn.createStatement();
			Statement st2 = conn.createStatement();
			
			String q = "Select * from classroom";
			ResultSet rs = st.executeQuery(q);
			
			String q1 = "Select * from subject";
			ResultSet rs1 = st1.executeQuery(q1);
			
			String q2 = "Select * from professor";
			ResultSet rs2 = st2.executeQuery(q2);
			
			int row = 0;
			while (rs.next()) {
			    for (int i = 0; i < 5; i++) {
			        if(i==0||i==2||i==3)
			        {
			        	resultSet[row][i] = (rs.getObject(i+1));
			        }
			        else
			        {
			        	resultSet[row][i] = rs.getObject(i+1);
			        }
			    }
			    row++;
			}
						
						rs.close();
						
							   row = 0;
				while (rs1.next()) {
				    for (int i = 0; i < 3; i++) {
				        if(i==0)
				        {
				        	resultSet1[row][i] = (rs1.getObject(i+1));
				        }
				        else
				        {
				        	resultSet1[row][i] = rs1.getObject(i+1);
				        }
				    }
				    row++;
				}
						
						
						rs1.close();

						   row = 0;
			while (rs2.next()) {
			    for (int i = 0; i < 5; i++) {
			        if(i==0||i==2||i==3)
			        {
			        	resultSet2[row][i] = (rs2.getObject(i+1));
			        }
			        else
			        {
			        	resultSet2[row][i] = rs2.getObject(i+1);
			        }
			    }
			    row++;
			}
						rs2.close();
						
			
			st.close();
			conn.close();
			}catch(Exception e){ System.out.println(e);} 
	}
}  
