package chimpanzinibananini.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import chimpanzinibananini.exception.ChimpanziniBananiniException;
import chimpanzinibananini.parser.Parser.CommandType;
import chimpanzinibananini.parser.Parser.ParsedCommand;
import chimpanzinibananini.task.Deadline;
import chimpanzinibananini.task.Event;
import chimpanzinibananini.task.Todo;

/** Tests conversion and validation of user commands. */
class ParserTest {

    @Test
    void parse_simpleCommands_returnsExpectedCommandTypes() throws ChimpanziniBananiniException {
        assertEquals(CommandType.LIST, Parser.parse("list").type());
        assertEquals(CommandType.BYE, Parser.parse("bye").type());
    }

    @Test
    void parse_numberedCommands_returnsOneBasedTaskNumber() throws ChimpanziniBananiniException {
        ParsedCommand mark = Parser.parse("  mark   2  ");
        ParsedCommand delete = Parser.parse("delete 3");

        assertEquals(CommandType.MARK, mark.type());
        assertEquals(2, mark.taskNumber());
        assertEquals(CommandType.DELETE, delete.type());
        assertEquals(3, delete.taskNumber());
    }

    @Test
    void parse_taskCommands_returnsCorrectTaskTypes() throws ChimpanziniBananiniException {
        assertInstanceOf(Todo.class, Parser.parse("todo read book").task());
        assertInstanceOf(Deadline.class,
                Parser.parse("deadline return book /by 2/12/2019 1800").task());
        assertInstanceOf(Event.class,
                Parser.parse("event project meeting /from 2pm /to 4pm").task());
    }

    @Test
    void parse_taskCommands_preservesValidatedTaskDetails() throws ChimpanziniBananiniException {
        assertEquals("[T][ ] read book", Parser.parse("todo read book").task().toString());
        assertEquals("[D][ ] return book (by: Dec 2 2019, 6:00PM)",
                Parser.parse("deadline return book /by 2/12/2019 1800").task().toString());
        assertEquals("[E][ ] project meeting (from: 2pm to: 4pm)",
                Parser.parse("event project meeting /from 2pm /to 4pm").task().toString());
    }

    @Test
    void parse_blankOrUnknownCommand_throwsChatbotException() {
        assertThrows(ChimpanziniBananiniException.class, () -> Parser.parse("   "));
        assertThrows(ChimpanziniBananiniException.class, () -> Parser.parse("dance"));
    }

    @Test
    void parse_simpleCommandWithArguments_throwsChatbotException() {
        assertThrows(ChimpanziniBananiniException.class, () -> Parser.parse("list now"));
    }

    @Test
    void parse_numberedCommandWithMissingOrInvalidNumber_throwsChatbotException() {
        assertThrows(ChimpanziniBananiniException.class, () -> Parser.parse("mark"));
        assertThrows(ChimpanziniBananiniException.class, () -> Parser.parse("delete first"));
    }

    @Test
    void parse_taskCommandWithMissingFields_throwsChatbotException() {
        assertThrows(ChimpanziniBananiniException.class, () -> Parser.parse("todo"));
        assertThrows(ChimpanziniBananiniException.class,
                () -> Parser.parse("deadline return book"));
        assertThrows(ChimpanziniBananiniException.class,
                () -> Parser.parse("event meeting /from 2pm"));
    }

    @Test
    void parse_deadlineWithInvalidDate_throwsChatbotException() {
        assertThrows(ChimpanziniBananiniException.class,
                () -> Parser.parse("deadline return book /by tomorrow"));
    }
}
