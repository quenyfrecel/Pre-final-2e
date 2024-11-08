package book;

import java.util.Scanner;

public class main {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        Book b = new Book();
        Student s = new Student();
        Borrow r = new Borrow();
        
        boolean isSelected = false;
        
        do{
            System.out.println("\nBook Borrowing:");
            System.out.println("1. Book");
            System.out.println("2. Student");
            System.out.println("3. Borrows");
            System.out.println("4. Exit");
            System.out.print("Enter selection: ");
            int select = conf.validateInt();
            
            switch(select){
                case 1:
                    b.bookCRUD();
                    break;
                case 2:
                    s.studentCRUD();
                    break;
                case 3:
                    r.borrowCRUD();
                    break;
                case 4:
                    System.out.print("Confirm exit? (yes/no): ");
                    String confirm = sc.next();
                    
                    if(confirm.contains("y"))
                        System.exit(0);
                    break;
                default:
                    System.out.println("Error, invalid selection.");
            }
        } while(!isSelected);
    }
    
}
