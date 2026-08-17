import java.util.Scanner;

public class ChimpanziniBananini {
    public static void main(String[] args) {
        System.out.println("BOMBARDIRO CROCODILO!!!");
        System.out.println("ChimpanziniBananini has entered the chat.");
        System.out.println("What can I cook for you, sigma?");
        System.out.println();
        Scanner scanner = new Scanner(System.in);

        Task[] tasks = new Task[100];
        int itemCount = 0;

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                break;
            }

            if (input.equals("list")) {
                for (int i = 0; i < itemCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(input.substring(5));
                tasks[taskNumber - 1].markAsDone();

                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks[taskNumber - 1]);
            } else {
                tasks[itemCount] = new Task(input);
                itemCount++;
                System.out.println("added: " + input);
            }
        }

        scanner.close();
        System.out.println("Tung tung tung sahur... I'm outta here 💀");
    }
}
