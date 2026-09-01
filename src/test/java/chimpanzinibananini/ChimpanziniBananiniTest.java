package chimpanzinibananini;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ChimpanziniBananiniTest {
    @TempDir
    private Path temporaryDirectory;

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
