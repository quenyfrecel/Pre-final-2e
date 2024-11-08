package book;

import implementation.students;

public class Student {
    public void studentCRUD(){
        config conf = new config();
        students s = new students();
        
        boolean isSelected = false;
        
        do{
            s.viewStudent();
            
            System.out.println("\nStudents:");
            System.out.println("1. Add Student");
            System.out.println("2. Edit Student");
            System.out.println("3. Delete Student");
            System.out.println("4. Select Student");
            System.out.println("5. Exit");
            System.out.print("Enter selection: ");
            int select = conf.validateInt();
            
            switch(select){
                case 1:
                    s.addStudent();
                    break;
                case 2:
                    s.editStudent();
                    break;
                case 3:
                    s.deleteStudent();
                    break;
                case 4:
                    s.selectStudent();
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
