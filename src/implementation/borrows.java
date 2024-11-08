package implementation;

import book.config;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class borrows {
    
    public void addReport(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("\nEnter student ID: ");
        int sid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT s_id FROM student WHERE s_id = ?", sid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            sid = conf.validateInt();
        }
        
        System.out.print("Enter book ID borrowed: ");
        int bid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT b_id FROM book WHERE b_id = ?", bid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            bid = conf.validateInt();
        }
        
        System.out.print("Enter date borrowed: ");
        String date_borrowed = sc.nextLine();
        
        String addQuery = "INSERT INTO borrow (s_id, b_id, status, date_borrowed) VALUES (?, ?, 'Borrowed' , ?)";
        conf.addRecord(addQuery, sid, bid, date_borrowed);
        conf.updateRecord("UPDATE book SET b_status = 'Unavailable' WHERE b_id = ?", bid);
    }
    
    public void editReport(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter ID: ");
        int getID = conf.validateInt();
        
        while(conf.getSingleValue("SELECT borrow_id from borrow WHERE borrow_id = ?", getID) == 0){
            System.out.print("ID doesn't exist, try again: ");
            getID = conf.validateInt();
        }
        
        try{
            PreparedStatement state = conf.connectDB().prepareStatement("SELECT b_id FROM borrow WHERE borrow_id = ?");
            state.setInt(1, getID);
            
            ResultSet rs = state.executeQuery();
            
            System.out.print("Enter new status (Borrowed/Returned): ");
            String newStatus = sc.nextLine();
            
            int book_id = rs.getInt("b_id");
            rs.close();

            switch(newStatus){
                case "Borrowed":
                    conf.updateRecord("UPDATE borrow SET status = 'Borrowed' WHERE borrow_id = ?", getID);
                    conf.updateRecord("UPDATE book SET b_status = 'Unavailable WHERE b_id = ?", book_id);
                    break;
                case "Returned":
                    conf.updateRecord("UPDATE borrow SET status = 'Returned' WHERE borrow_id = ?", getID);
                    conf.updateRecord("UPDATE book SET b_status = 'Available' WHERE b_id = ?", book_id);
                    break;
                default:
                    System.out.println("Error: Invalid status.");
            }
            
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void deleteReport(){
        config conf = new config();
        
        System.out.print("Enter ID: ");
        int getID = conf.validateInt();
        
        while(conf.getSingleValue("SELECT borrow_id from borrow WHERE borrow_id = ?", getID) == 0){
            System.out.print("ID doesn't exist, try again: ");
            getID = conf.validateInt();
        }
        
        String deleteQuery = "DELETE FROM borrow WHERE borrow_id = ?";
        conf.deleteRecord(deleteQuery, getID);
    }
    
    public void viewReport(){
        config conf = new config();
        
        String borrowQuery = "SELECT borrow_id, b.b_title, date_borrowed, status"
                + " FROM borrow "
                + "INNER JOIN student s ON borrow.s_id = s.s_id INNER JOIN book b ON borrow.b_id = b.b_id";
        String[] borrowHeaders = {"ID", "Book", "Date Borrowed", "Status"};
        String[] boorrowColumns = {"borrow_id", "b_title", "date_borrowed", "status"};

        conf.viewRecords(borrowQuery, borrowHeaders, boorrowColumns);
    }
    
    public void selectReport(){
        config conf = new config();
        
        System.out.print("Enter ID: ");
        int getID = conf.validateInt();
        
        while(conf.getSingleValue("SELECT borrow_id from borrow WHERE borrow_id = ?", getID) == 0){
            System.out.print("ID doesn't exist, try again: ");
            getID = conf.validateInt();
        }
        
        String borrowQuery = "SELECT borrow_id, s.s_name, b.b_title, date_borrowed, status"
                + " FROM borrow "
                + "INNER JOIN student s ON borrow.s_id = s.s_id INNER JOIN book b ON borrow.b_id = b.b_id WHERE borrow_id = ?";
        
        try{
            PreparedStatement state = conf.connectDB().prepareStatement(borrowQuery);
            state.setInt(1, getID);
            
            ResultSet rs = state.executeQuery();
            
            int bid = rs.getInt("borrow_id");
            String sname = rs.getString("s_name");
            String btitle = rs.getString("b_title");
            String date = rs.getString("date_borrowed");
            String status = rs.getString("status");
            
            System.out.println("\nBorrow Information: ");
            System.out.println("-------------------------------------");
            System.out.println("ID: "+bid);
            System.out.println("Book Title: "+btitle);
            System.out.println("Borrowing student: "+sname);
            System.out.println("Date Borrowed: "+date);
            System.out.println("-------------------------------------");
            System.out.println("Status: "+status);
            
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
