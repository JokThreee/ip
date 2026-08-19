import java.util.Scanner;
import java.util.ArrayList;

public class ChimpanziniBananini {
    public static void main(String[] args) {
        System.out.println("BOMBARDIRO CROCODILO!!!");
        System.out.println("ChimpanziniBananini has entered the chat.");
        System.out.println("What can I cook for you, sigma?");
        System.out.println();
        Scanner scanner = new Scanner(System.in);

        ArrayList<Task> tasks = new ArrayList<>();

        while (true) {
            String input = scanner.nextLine();

            try {
                if (input.equals("bye")) {
                    break;
                }

                if (input.equals("list")) {
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + ". " + tasks.get(i));
                    }
                } else if (input.startsWith("mark ")) {
                    int taskNumber = Integer.parseInt(input.substring(5));
                    tasks.get(taskNumber - 1).markAsDone();

                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks.get(taskNumber - 1));
                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    String description = input.length() > 4
                            ? input.substring(5)
                            : "";

                    if (description.isBlank()) {
                        throw new ChimpanziniBananiniException(
                                "BROTHER WHERE IS THE TASK 💀 Todo cannot be empty.");
                    }

                    tasks.add(new Todo(description));

                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks.get(tasks.size() - 1));
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (input.startsWith("deadline ")) {
                    String details = input.substring(9);
                    String[] parts = details.split(" /by ", 2);

                    String description = parts[0];
                    String by = parts[1];

                    tasks.add(new Deadline(description, by));

                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks.get(tasks.size() - 1));
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");

                } else if (input.startsWith("event ")) {
                    String details = input.substring(6);

                    String[] fromParts = details.split(" /from ", 2);
                    String description = fromParts[0];

                    String[] toParts = fromParts[1].split(" /to ", 2);
                    String from = toParts[0];
                    String to = toParts[1];

                    tasks.add(new Event(description, from, to));

                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks.get(tasks.size() - 1));
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (input.startsWith("delete ")) {
                    int taskNumber = Integer.parseInt(input.substring(7));

                    Task removedTask = tasks.remove(taskNumber - 1);

                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else {
                    throw new ChimpanziniBananiniException(
                            "Bro is speaking enchantment table 💀 I don't know that command.");
                }
            } catch (ChimpanziniBananiniException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Bro 💀 give me an actual task number.");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("That task number does not exist 💀");
            }
        }

        scanner.close();
        System.out.println("Tung tung tung sahur... I'm outta here 💀");
    }
}
