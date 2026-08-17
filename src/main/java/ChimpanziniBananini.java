import java.util.Scanner;

public class ChimpanziniBananini {
    public static void main(String[] args) {
        System.out.println("BOMBARDIRO CROCODILO!!!");
        System.out.println("ChimpanziniBananini has entered the chat.");
        System.out.println("What can I cook for you, sigma?");
        System.out.println();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                break;
            }

            System.out.println(input);
        }

        scanner.close();
        System.out.println("Tung tung tung sahur... I'm outta here 💀");
    }
}
