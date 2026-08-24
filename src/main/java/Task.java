public class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public String getDescription() {
        return description;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    public String getTypeIcon() {
        return "T";
    }

    /** Returns this task in the format used by the data file. */
    public String toDataString() {
        String doneValue = isDone ? "1" : "0";
        return getTypeIcon() + " | " + doneValue + " | " + encodeField(description);
    }

    /** Escapes characters that have a special meaning in the data file. */
    protected static String encodeField(String value) {
        return value.replace("\\", "\\\\")
                .replace("|", "\\|")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
