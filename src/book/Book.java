package book;

import implementation.books;

public class Book {
    public void bookCRUD(){
        config conf = new config();
        books b = new books();
        
        boolean isSelected = false;
        
        do{
            b.viewBook();
            
            System.out.println("\nBooks:");
            System.out.println("1. Add Book");
            System.out.println("2. Edit Book");
            System.out.println("3. Delete Book");
            System.out.println("4. Select Book");
            System.out.println("5. Exit");
            System.out.print("Enter selection: ");
            int select = conf.validateInt();
            
            switch(select){
                case 1:
                    b.addBook();
                    break;
                case 2:
                    b.editBook();
                    break;
                case 3:
                    b.deleteBook();
                    break;
                case 4:
                    b.selectBook();
                    break;
                case 5:
                    isSelected = true;
                    break;
                default:
                    System.out.println("Error, invalid selection.");
            }
        } while(!isSelected);
    }
}
