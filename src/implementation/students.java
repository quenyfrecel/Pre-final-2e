package implementation;

import book.config;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class students {
    public void addStudent(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("\nEnter student name: ");
        String name = sc.nextLine();
        
        System.out.print("Enter student program: ");
        String program = sc.nextLine();
        
        System.out.print("Enter student school year: ");
        String year = sc.nextLine();
        
        System.out.print("Enter student section: ");
        String section = sc.nextLine();
        
        String addQuery = "INSERT INTO student (s_name, s_program, s_year, s_section) VALUES (?, ? ,? ,?)";
        conf.addRecord(addQuery, name, program, year, section);
    }
    
    public void editStudent(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("\nEnter student ID: ");
        int sid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT s_id FROM student WHERE s_id = ?", sid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            sid = conf.validateInt();
        }
        
        System.out.print("\nEnter new student name: ");
        String newName = sc.nextLine();
        
        System.out.print("Enter new student program: ");
        String newProgram = sc.nextLine();
        
        System.out.print("Enter new student school year: ");
        String newYear = sc.nextLine();
        
        System.out.print("Enter new student section: ");
        String newSection = sc.nextLine();
        
        String editQuery = "UPDATE student SET s_name = ?, s_program = ?, s_year = ?, s_section = ? WHERE s_id = ?";
        conf.updateRecord(editQuery, newName, newProgram, newYear, newSection, sid);
    }
    
     public void deleteStudent(){
        config conf = new config();
        
        System.out.print("\nEnter student ID: ");
        int sid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT s_id FROM student WHERE s_id = ?", sid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            sid = conf.validateInt();
        }
        
        String deleteQuery = "DELETE FROM student WHERE s_id = ?";
        conf.deleteRecord(deleteQuery, sid);
    }
     
    public void viewStudent(){
        config conf = new config();
        
        String studentQuery = "SELECT * FROM student";
        String[] studentHeaders = {"ID", "Name", "Year", "Program"};
        String[] studentColumns = {"s_id", "s_name", "s_year", "s_program"};

        conf.viewRecords(studentQuery, studentHeaders, studentColumns);
    }
    
    public void selectStudent(){
        config conf = new config();
        
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
    }
}
