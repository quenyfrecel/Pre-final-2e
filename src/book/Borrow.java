package book;

import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Borrow {
    public void borrowCRUD(){
        Scanner sc = new Scanner(System.in);
        
        boolean isSelected = false;
        
        do{
            System.out.println("\nBorrows:");
            System.out.println("1. Add Report");
            System.out.println("2. Edit Report");
            System.out.println("3. Delete Report");
            System.out.println("4. View Report");
            System.out.println("5. Select Report");
            System.out.println("6. Exit");
            System.out.print("Enter selection: ");
            int select = sc.nextInt();
            
            switch(select){
                case 1:
                    addReport();
                    break;
                case 2:
                    editReport();
                    break;
                case 3:
                    deleteReport();
                    break;
                case 4:
                    viewReport();
                    break;
                case 5:
                    selectReport();
                    break;
                case 6:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error, invalid selection.");
            }
        } while(!isSelected);
    }
    
    public void addReport(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("\nEnter student ID: ");
        int sid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT s_id FROM student WHERE s_id = ?", sid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            sid = sc.nextInt();
        }
        
        System.out.print("Enter book ID borrowed: ");
        int bid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT b_id FROM book WHERE b_id = ?", bid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            bid = sc.nextInt();
        }
        
        System.out.print("Enter date borrowed: ");
        sc.nextLine();
        String date_borrowed = sc.nextLine();
        
        String addQuery = "INSERT INTO borrow (s_id, b_id, status, date_borrowed) VALUES (?, ?, 'Borrowed' , ?)";
        conf.addRecord(addQuery, sid, bid, date_borrowed);
        conf.updateRecord("UPDATE book SET b_status = 'Unavailable' WHERE b_id = ?", bid);
    }
    
    public void editReport(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter ID: ");
        int getID = sc.nextInt();
        
        while(conf.getSingleValue("SELECT borrow_id from borrow WHERE borrow_id = ?", getID) == 0){
            System.out.print("ID doesn't exist, try again: ");
            getID = sc.nextInt();
        }
        
        try{
            PreparedStatement state = conf.connectDB().prepareStatement("SELECT b_id FROM borrow WHERE borrow_id = ?");
            state.setInt(1, getID);
            
            ResultSet rs = state.executeQuery();
            
            System.out.print("Enter new status (Borrowed/Returned): ");
            String newStatus = sc.next();
            
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
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter ID: ");
        int getID = sc.nextInt();
        
        while(conf.getSingleValue("SELECT borrow_id from borrow WHERE borrow_id = ?", getID) == 0){
            System.out.print("ID doesn't exist, try again: ");
            getID = sc.nextInt();
        }
        
        String deleteQuery = "DELETE FROM borrow WHERE borrow_id = ?";
        conf.deleteRecord(deleteQuery, getID);
    }
    
    public void viewReport(){
        config conf = new config();
        
        String borrowQuery = "SELECT borrow_id, b.b_title, date_borrowed"
                + " FROM borrow "
                + "INNER JOIN student s ON borrow.s_id = s.s_id INNER JOIN book b ON borrow.b_id = b.b_id";
        String[] borrowHeaders = {"ID", "Book", "Date Borrowed"};
        String[] boorrowColumns = {"borrow_id", "b_title", "date_borrowed"};

        conf.viewRecords(borrowQuery, borrowHeaders, boorrowColumns);
    }
    
    public void selectReport(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("Enter ID: ");
        int getID = sc.nextInt();
        
        while(conf.getSingleValue("SELECT borrow_id from borrow WHERE borrow_id = ?", getID) == 0){
            System.out.print("ID doesn't exist, try again: ");
            getID = sc.nextInt();
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
