package book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Student {
    public void studentCRUD(){
        Scanner sc = new Scanner(System.in);
        
        boolean isSelected = false;
        
        do{
            System.out.println("\nStudents:");
            System.out.println("1. Add Student");
            System.out.println("2. Edit Student");
            System.out.println("3. Delete Student");
            System.out.println("4. View Student");
            System.out.println("5. Select Student");
            System.out.println("6. Exit");
            System.out.print("Enter selection: ");
            int select = sc.nextInt();
            
            switch(select){
                case 1:
                    addStudent();
                    break;
                case 2:
                    editStudent();
                    break;
                case 3:
                    deleteStudent();
                    break;
                case 4:
                    viewStudent();
                    break;
                case 5:
                    selectStudent();
                    break;
                case 6:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error, invalid selection.");
            }
        } while(!isSelected);
    }
    
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
        int sid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT s_id FROM student WHERE s_id = ?", sid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            sid = sc.nextInt();
        }
        
        System.out.print("\nEnter new student name: ");
        sc.nextLine();
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
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("\nEnter student ID: ");
        int sid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT s_id FROM student WHERE s_id = ?", sid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            sid = sc.nextInt();
        }
        
        String deleteQuery = "DELETE FROM student WHERE s_id = ?";
        conf.deleteRecord(deleteQuery, sid);
    }
    
    public void viewStudent(){
        config conf = new config();
        
        String studentQuery = "SELECT * FROM student";
        String[] studentHeaders = {"ID", "Name"};
        String[] studentColumns = {"s_id", "s_name"};

        conf.viewRecords(studentQuery, studentHeaders, studentColumns);
    }
    
    public void selectStudent(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("\nEnter student ID: ");
        int sid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT s_id FROM student WHERE s_id = ?", sid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            sid = sc.nextInt();
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
