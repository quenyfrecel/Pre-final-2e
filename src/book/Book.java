package book;

import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Book {
    public void bookCRUD(){
        Scanner sc = new Scanner(System.in);
        
        boolean isSelected = false;
        
        do{
            System.out.println("\nBooks:");
            System.out.println("1. Add Book");
            System.out.println("2. Edit Book");
            System.out.println("3. Delete Book");
            System.out.println("4. View Book");
            System.out.println("5. Select Book");
            System.out.println("6. Exit");
            System.out.print("Enter selection: ");
            int select = sc.nextInt();
            
            switch(select){
                case 1:
                    addBook();
                    break;
                case 2:
                    editBook();
                    break;
                case 3:
                    deleteBook();
                    break;
                case 4:
                    viewBook();
                    break;
                case 5:
                    selectBook();
                    break;
                case 6:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error, invalid selection.");
            }
        } while(!isSelected);
    }
    
    public void addBook(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("\nEnter book title: ");
        String title = sc.nextLine();
        
        System.out.print("Enter category: ");
        String category = sc.nextLine();
        
        System.out.print("Enter status: ");
        String status = sc.next();
        
        String addQuery = "INSERT INTO book (b_title, b_category, b_status) VALUES (?, ?, ?)";
        conf.addRecord(addQuery, title, category, status);
    }
    
    public void editBook(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("\nEnter book ID: ");
        int bid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT b_id FROM book WHERE b_id = ?", bid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            bid = sc.nextInt();
        }
        
        System.out.print("Enter new title: ");
        sc.nextLine();
        String newTitle = sc.nextLine();
        
        System.out.print("Enter new category: ");
        String newCateg = sc.nextLine();
        
        System.out.print("Enter new status: ");
        String newStatus = sc.next();
        
        String editQuery = "UPDATE book SET b_title = ?, b_category = ?, b_status = ? WHERE b_id = ?";
        conf.updateRecord(editQuery, newTitle, newCateg, newStatus, bid);
    }
    
    public void deleteBook(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("\nEnter book ID: ");
        int bid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT b_id FROM book WHERE b_id = ?", bid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            bid = sc.nextInt();
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
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        
        System.out.print("\nEnter book ID: ");
        int bid = sc.nextInt();
        
        while(conf.getSingleValue("SELECT b_id FROM book WHERE b_id = ?", bid) == 0){
            System.out.print("ID doesn't exist, try again: ");
            bid = sc.nextInt();
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
