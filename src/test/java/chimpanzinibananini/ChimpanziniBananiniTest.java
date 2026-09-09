package chimpanzinibananini;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ChimpanziniBananiniTest {
    private static final Clock REMINDER_CLOCK = Clock.fixed(
            Instant.parse("2026-09-09T07:00:00Z"), ZoneId.of("Asia/Singapore"));

    @TempDir
    private Path temporaryDirectory;

    @Test
    void getResponse_reminders_usesClockAndLeavesTasksAndFileUnchanged() throws IOException {
        Path dataFile = temporaryDirectory.resolve("duke.txt");
        Files.writeString(dataFile, "D | 0 | later | 2026-09-16T15:00:00\n"
                + "\nD | 0 | now | 2026-09-09T15:00:00\n"
                + "D | 1 | completed | 2026-09-10T15:00:00\n"
                + "D | 0 | overdue | 2026-09-09T14:59:59\n"
                + "D | 0 | beyond | 2026-09-16T15:00:01\n");
        Files.setLastModifiedTime(dataFile, FileTime.from(Instant.parse("2020-01-01T00:00:00Z")));
        byte[] originalContents = Files.readAllBytes(dataFile);
        FileTime originalModifiedTime = Files.getLastModifiedTime(dataFile);
        ChimpanziniBananini chatbot = new ChimpanziniBananini(dataFile, REMINDER_CLOCK);
        String originalList = chatbot.getResponse("list");

        assertEquals("Here are your upcoming deadlines:\n"
                + "1. [D][ ] now (by: Sep 9 2026, 3:00PM)\n"
                + "2. [D][ ] later (by: Sep 16 2026, 3:00PM)", chatbot.getResponse("reminders"));
        assertEquals(originalList, chatbot.getResponse("list"));
        assertArrayEquals(originalContents, Files.readAllBytes(dataFile));
        assertEquals(originalModifiedTime, Files.getLastModifiedTime(dataFile));
    }

    @Test
    void getResponse_remindersWithoutTasks_returnsEmptyMessageWithoutCreatingFile() {
        Path dataFile = temporaryDirectory.resolve("missing.txt");
        ChimpanziniBananini chatbot = new ChimpanziniBananini(dataFile, REMINDER_CLOCK);

        assertEquals("You have no upcoming deadlines in the next 7 days.", chatbot.getResponse("reminders"));
        assertEquals("The reminders command takes no arguments.", chatbot.getResponse("reminders 7"));
        assertFalse(Files.exists(dataFile));
    }

    @Test
    void getResponse_remindersWithLoadingError_preservesLoadingError() throws IOException {
        Path dataFile = temporaryDirectory.resolve("invalid.txt");
        Files.writeString(dataFile, "invalid task data");
        ChimpanziniBananini chatbot = new ChimpanziniBananini(dataFile, REMINDER_CLOCK);

        assertEquals(chatbot.getResponse("list"), chatbot.getResponse("reminders"));
        assertTrue(chatbot.getResponse("reminders").startsWith("I couldn't load the task file:"));
    }

    @Test
    void getResponse_addThenList_returnsResponsesAndPersistsTask() {
        Path dataFile = temporaryDirectory.resolve("data").resolve("duke.txt");
        ChimpanziniBananini chatbot = new ChimpanziniBananini(dataFile);

        String addResponse = chatbot.getResponse("todo read book");
        String listResponse = chatbot.getResponse("list");

        assertTrue(addResponse.contains("read book"));
        assertEquals("1. [T][ ] read book", listResponse);
        assertEquals("1. [T][ ] read book", new ChimpanziniBananini(dataFile).getResponse("list"));
    }

    @Test
    void getResponse_invalidCommand_returnsParserError() {
        ChimpanziniBananini chatbot = new ChimpanziniBananini(
                temporaryDirectory.resolve("data").resolve("duke.txt"));

        String response = chatbot.getResponse("dance");

        assertTrue(response.contains("I don't know that command"));
    }
}
