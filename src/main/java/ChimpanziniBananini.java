import java.util.Scanner;

public class ChimpanziniBananini {
    public static void main(String[] args) {
        System.out.println("BOMBARDIRO CROCODILO!!!");
        System.out.println("ChimpanziniBananini has entered the chat.");
        System.out.println("What can I cook for you, sigma?");
        System.out.println();
        Scanner scanner = new Scanner(System.in);

        String[] items = new String[100];
        int itemCount = 0;

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                break;
            }

            if (input.equals("list")) {
                for (int i = 0; i < itemCount; i++) {
                    System.out.println((i + 1) + ". " + items[i]);
                }
            } else {
                items[itemCount] = input;
                itemCount++;
                System.out.println("added: " + input);
            }
        }

        scanner.close();
        System.out.println("Tung tung tung sahur... I'm outta here 💀");
    }
}
