package implementation;

import book.config;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class books {
    
    public void addBook(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("\nEnter book title: ");
        String title = sc.nextLine();
        
        System.out.print("Enter category: ");
        String category = sc.nextLine();
        
        System.out.print("Enter status: ");
        String status = sc.nextLine();
        
        String addQuery = "INSERT INTO book (b_title, b_category, b_status) VALUES (?, ?, ?)";
        conf.addRecord(addQuery, title, category, status);
    }
    
    public void editBook(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("\nEnter book ID: ");
        int bid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT b_id FROM book WHERE b_id = ?", bid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            bid = conf.validateInt();
        }
        
        System.out.print("Enter new title: ");
        String newTitle = sc.nextLine();
        
        System.out.print("Enter new category: ");
        String newCateg = sc.nextLine();
        
        System.out.print("Enter new status: ");
        String newStatus = sc.nextLine();
        
        String editQuery = "UPDATE book SET b_title = ?, b_category = ?, b_status = ? WHERE b_id = ?";
        conf.updateRecord(editQuery, newTitle, newCateg, newStatus, bid);
    }
    
    public void deleteBook(){
        config conf = new config();
        
        System.out.print("\nEnter book ID: ");
        int bid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT b_id FROM book WHERE b_id = ?", bid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            bid = conf.validateInt();
        }
        
        String deleteQuery = "DELETE FROM book WHERE b_id = ?";
        conf.deleteRecord(deleteQuery, bid);
    }
    
    public void viewBook(){
        config conf = new config();
        
        String bookQuery = "SELECT * FROM book";
        String[] bookHeaders = {"ID", "Title", "Category", "Status"};
        String[] bookColumns = {"b_id", "b_title", "b_category", "b_status"};

        conf.viewRecords(bookQuery, bookHeaders, bookColumns);
    }
    
    public void selectBook(){
        config conf = new config();
        
        System.out.print("\nEnter book ID: ");
        int bid = conf.validateInt();
        
        while(conf.getSingleValue("SELECT b_id FROM book WHERE b_id = ?", bid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            bid = conf.validateInt();
        }
        
        String sql = "SELECT * FROM book WHERE b_id = ?";
        
        try{
            PreparedStatement findRow = conf.connectDB().prepareStatement(sql);
            findRow.setInt(1, bid);
            
            ResultSet rs = findRow.executeQuery();
            int getID = rs.getInt("b_id");
            String getTitle = rs.getString("b_title");
            String getCateg = rs.getString("b_category");
            String getStatus = rs.getString("b_status");
            
            System.out.println("\nBook Information: ");
            System.out.println("-------------------------------------");
            System.out.println("ID: "+getID);
            System.out.println("Title: "+getTitle);
            System.out.println("Category: "+getCateg);
            System.out.println("-------------------------------------");
            System.out.println("Status: "+getStatus);
            
        } catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
