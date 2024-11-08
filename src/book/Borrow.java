package book;

import implementation.borrows;

public class Borrow {
    public void borrowCRUD(){
        borrows b = new borrows();
        config conf = new config();
        
        boolean isSelected = false;
        
        do{
            b.viewReport();
            
            System.out.println("\nBorrows:");
            System.out.println("1. Add Report");
            System.out.println("2. Edit Report");
            System.out.println("3. Delete Report");
            System.out.println("4. Select Report");
            System.out.println("5. Exit");
            System.out.print("Enter selection: ");
            int select = conf.validateInt();
            
            switch(select){
                case 1:
                    b.addReport();
                    break;
                case 2:
                    b.editReport();
                    break;
                case 3:
                    b.deleteReport();
                    break;
                case 4:
                    b.selectReport();
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
