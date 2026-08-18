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
            } else if (input.startsWith("todo ")) {
                String description = input.substring(5);

                tasks[itemCount] = new Todo(description);
                itemCount++;

                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[itemCount - 1]);
                System.out.println("Now you have " + itemCount + " tasks in the list.");

            } else if (input.startsWith("deadline ")) {
                String details = input.substring(9);
                String[] parts = details.split(" /by ", 2);

                String description = parts[0];
                String by = parts[1];

                tasks[itemCount] = new Deadline(description, by);
                itemCount++;

                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[itemCount - 1]);
                System.out.println("Now you have " + itemCount + " tasks in the list.");

            } else if (input.startsWith("event ")) {
                String details = input.substring(6);

                String[] fromParts = details.split(" /from ", 2);
                String description = fromParts[0];

                String[] toParts = fromParts[1].split(" /to ", 2);
                String from = toParts[0];
                String to = toParts[1];

                tasks[itemCount] = new Event(description, from, to);
                itemCount++;

                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[itemCount - 1]);
                System.out.println("Now you have " + itemCount + " tasks in the list.");
            }
        }

        scanner.close();
        System.out.println("Tung tung tung sahur... I'm outta here 💀");
    }
}
