import java.util.Scanner;

public class Main {
    static void greed() {
        String statement = "Hello Java! This is me fucking around \n";
        System.out.println(statement);
    }

    static void info(Scanner scanner) {
        System.out.print("Enter your name: ");
        String readString = scanner.nextLine();
        scanner.nextLine();

        System.out.println("\n");

        System.out.print("Enter your age: ");
        int readint = scanner.nextInt();

        System.out.println("Hi your name is " + readString + ", Nice to meet you!\nYour Age is: " + readint + "\n");
        scanner.nextLine();        
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            
            System.out.println("+---------------------------------------+\n|\tWelcome to Java Program!\t|\n+---------------------------------------+");
            System.out.println("1. Scanner Info \n2.Loop \n3.Array \n4.Exit \n");
            greed();


            int userInput;

            System.out.print("Select number 1 - 4: ");
            userInput = scanner.nextInt();

            if (userInput == 1) {
                System.out.println("Scanner Info");
                info(scanner);
            } else if (userInput == 2) {
                System.out.println("Loop");
            } else if (userInput == 3) {
                System.out.println("Array");
            } else if (userInput == 4) {
                System.out.println("Exit");
                break;
            }
            for (int i = 0; i <= 10; i++) {
                System.out.print(i + " ");
            }

            int [] numbers = {1,2,3,4,5,6,7,8,9,10};
            for (int number: numbers) {
                System.out.println(number + " ");
            }
        }
        scanner.close();

    }
}

