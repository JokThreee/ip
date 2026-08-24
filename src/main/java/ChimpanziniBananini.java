import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/** Runs the ChimpanziniBananini task chatbot. */
public class ChimpanziniBananini {
    private static final Path DATA_FILE = Path.of("data", "duke.txt");
    private static final Storage STORAGE = new Storage(DATA_FILE);

    public static void main(String[] args) {
        System.out.println("BOMBARDIRO CROCODILO!!!");
        System.out.println("ChimpanziniBananini has entered the chat.");
        System.out.println("What can I cook for you, sigma?");
        System.out.println();

        ArrayList<Task> tasks;
        try {
            tasks = STORAGE.loadTasks();
        } catch (IOException e) {
            System.out.println("I couldn't load the task file: " + e.getMessage());
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine().strip();
                if (input.equals("bye")) {
                    break;
                }
                try {
                    processCommand(input, tasks);
                } catch (ChimpanziniBananiniException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    System.out.println("I couldn't save your tasks: " + e.getMessage());
                    break;
                }
            }
        }
        System.out.println("Tung tung tung sahur... I'm outta here 💀");
    }

    /** Processes one user command and saves the list after successful changes. */
    private static void processCommand(String input, ArrayList<Task> tasks)
            throws ChimpanziniBananiniException, IOException {
        if (input.isEmpty()) {
            throw new ChimpanziniBananiniException("Please enter a command.");
        }
        String[] commandParts = input.split("\\s+", 2);
        String command = commandParts[0];
        String arguments = commandParts.length == 2 ? commandParts[1].strip() : "";

        switch (command) {
        case "list" -> {
            requireNoArguments(arguments, "list");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
        case "mark" -> {
            int taskIndex = parseTaskIndex(arguments, tasks.size());
            tasks.get(taskIndex).markAsDone();
            STORAGE.saveTasks(tasks);
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + tasks.get(taskIndex));
        }
        case "todo" -> {
            requireValue(arguments, "BROTHER WHERE IS THE TASK 💀 Todo cannot be empty.");
            Task task = new Todo(arguments);
            tasks.add(task);
            STORAGE.saveTasks(tasks);
            printAddedTask(task, tasks.size());
        }
        case "deadline" -> {
            String[] fields = splitRequired(arguments, " /by ",
                    "Use: deadline DESCRIPTION /by DATE (for example, 2/12/2019 1800)");
            final Task task;
            try {
                task = new Deadline(fields[0], fields[1]);
            } catch (DateTimeParseException e) {
                throw new ChimpanziniBananiniException("Invalid deadline date. " + e.getMessage());
            }
            tasks.add(task);
            STORAGE.saveTasks(tasks);
            printAddedTask(task, tasks.size());
        }
        case "event" -> {
            String[] fromFields = splitRequired(arguments, " /from ",
                    "Use: event DESCRIPTION /from START /to END");
            String[] toFields = splitRequired(fromFields[1], " /to ",
                    "Use: event DESCRIPTION /from START /to END");
            Task task = new Event(fromFields[0], toFields[0], toFields[1]);
            tasks.add(task);
            STORAGE.saveTasks(tasks);
            printAddedTask(task, tasks.size());
        }
        case "delete" -> {
            int taskIndex = parseTaskIndex(arguments, tasks.size());
            Task removedTask = tasks.remove(taskIndex);
            STORAGE.saveTasks(tasks);
            System.out.println("Noted. I've removed this task:");
            System.out.println("  " + removedTask);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        }
        default -> throw new ChimpanziniBananiniException(
                "Bro is speaking enchantment table 💀 I don't know that command.");
        }
    }

    /** Splits a command argument and ensures values exist on both sides. */
    private static String[] splitRequired(String value, String delimiter, String usageMessage)
            throws ChimpanziniBananiniException {
        String[] fields = value.split(java.util.regex.Pattern.quote(delimiter), 2);
        if (fields.length != 2 || fields[0].isBlank() || fields[1].isBlank()) {
            throw new ChimpanziniBananiniException(usageMessage);
        }
        fields[0] = fields[0].strip();
        fields[1] = fields[1].strip();
        return fields;
    }

    /** Converts a one-based task number into a validated list index. */
    private static int parseTaskIndex(String value, int taskCount)
            throws ChimpanziniBananiniException {
        requireValue(value, "Please provide a task number.");
        final int taskNumber;
        try {
            taskNumber = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ChimpanziniBananiniException("Bro 💀 give me an actual task number.");
        }
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new ChimpanziniBananiniException("That task number does not exist 💀");
        }
        return taskNumber - 1;
    }

    /** Ensures a command argument is present. */
    private static void requireValue(String value, String message)
            throws ChimpanziniBananiniException {
        if (value.isBlank()) {
            throw new ChimpanziniBananiniException(message);
        }
    }

    /** Rejects unexpected arguments for commands that do not accept them. */
    private static void requireNoArguments(String value, String command)
            throws ChimpanziniBananiniException {
        if (!value.isEmpty()) {
            throw new ChimpanziniBananiniException("The " + command + " command takes no arguments.");
        }
    }

    /** Prints the standard response after adding a task. */
    private static void printAddedTask(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

}
