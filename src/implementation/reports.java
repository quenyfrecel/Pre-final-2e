package implementation;

import book.config;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class reports {
    public void viewReport(){
        config conf = new config();
        students s = new students();
        
        s.viewStudent();
        System.out.print("\nEnter student ID: ");
        int sid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT s_id FROM student WHERE s_id = ?", sid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            sid = conf.validateInt();
        }
        
        String sql = "SELECT * FROM student WHERE s_id = ?";
        
        try{
            PreparedStatement findRow = conf.connectDB().prepareStatement(sql);
            findRow.setInt(1, sid);
            
            ResultSet rs = findRow.executeQuery();
            int getID = rs.getInt("s_id");
            String getName = rs.getString("s_name");
            String getProgram = rs.getString("s_program");
            String getYear = rs.getString("s_year");
            String getSection = rs.getString("s_section");
            
            System.out.println("\nStudent Information: ");
            System.out.println("-------------------------------------");
            System.out.println("ID: "+getID);
            System.out.println("Name: "+getName);
            System.out.println("Program: "+getProgram);
            System.out.println("Year: "+getYear);
            System.out.println("Section: "+getSection);
            
            rs.close();
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
        
        System.out.println("\nBooks borrowed:");
        
        try{
            PreparedStatement findRow = conf.connectDB().prepareStatement("SELECT borrow_id, s_name, b_title, date_borrowed, status "
                    + "FROM borrow "
                    + "INNER JOIN student ON borrow.s_id = student.s_id "
                    + "INNER JOIN book ON borrow.b_id = book.b_id "
                    + "WHERE student.s_id = ?");
            findRow.setInt(1, sid);
            
            try(ResultSet rs = findRow.executeQuery()){
                System.out.printf("%-10s %-20s %-20s %-20s %-20s\n", "ID", "Student", "Book", "Date Borrowed", "Status");
                System.out.println("----------------------------------------------------------------------------------");
                while(rs.next()){
                    int bid = rs.getInt("borrow_id");
                    String sname = rs.getString("s_name");
                    String btitle = rs.getString("b_title");
                    String date = rs.getString("date_borrowed");
                    String status = rs.getString("status");
                    
                    System.out.printf("%-10d %-20s %-20s %-20s %-20s\n", bid, sname, btitle, date, status);
                }
                System.out.println("----------------------------------------------------------------------------------");
            }
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
