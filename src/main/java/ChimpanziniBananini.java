import java.io.IOException;
import java.nio.file.Path;

/** Coordinates the components of the ChimpanziniBananini task chatbot. */
public class ChimpanziniBananini {
    private static final Path DATA_FILE = Path.of("data", "duke.txt");

    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    /** Creates a chatbot that stores its tasks at the given path. */
    public ChimpanziniBananini(Path filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
    }

    /** Reads and processes commands until the input ends or the user says bye. */
    public void run() {
        ui.showWelcome();
        try {
            tasks = storage.loadTasks();
        } catch (IOException e) {
            ui.showLoadingError(e.getMessage());
            ui.close();
            return;
        }
        try (ui) {
            while (ui.hasNextCommand()) {
                try {
                    Parser.ParsedCommand command = Parser.parse(ui.readCommand());
                    if (command.type() == Parser.CommandType.BYE) {
                        break;
                    }
                    execute(command);
                } catch (ChimpanziniBananiniException e) {
                    ui.showError(e.getMessage());
                } catch (IOException e) {
                    ui.showSavingError(e.getMessage());
                    break;
                }
            }
        }
        ui.showGoodbye();
    }

    /** Executes one parsed command and saves the list after successful changes. */
    private void execute(Parser.ParsedCommand command) throws ChimpanziniBananiniException, IOException {
        switch (command.type()) {
        case LIST -> ui.showTaskList(tasks);
        case MARK -> {
            Task task = tasks.mark(command.taskNumber());
            storage.saveTasks(tasks);
            ui.showMarkedTask(task);
        }
        case ADD -> {
            tasks.add(command.task());
            storage.saveTasks(tasks);
            ui.showAddedTask(command.task(), tasks.size());
        }
        case DELETE -> {
            Task removedTask = tasks.delete(command.taskNumber());
            storage.saveTasks(tasks);
            ui.showDeletedTask(removedTask, tasks.size());
        }
        case BYE -> throw new AssertionError("Bye is handled before command execution");
        }
    }

    /** Starts the chatbot using its default data file. */
    public static void main(String[] args) {
        new ChimpanziniBananini(DATA_FILE).run();
    }
}
