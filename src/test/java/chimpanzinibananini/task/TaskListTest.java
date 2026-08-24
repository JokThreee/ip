package chimpanzinibananini.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import chimpanzinibananini.exception.ChimpanziniBananiniException;

/** Tests task-list ownership and one-based task operations. */
class TaskListTest {

    @Test
    void constructor_sourceListLaterModified_keepsIndependentCopy() {
        ArrayList<Task> source = new ArrayList<>(List.of(new Todo("read book")));
        TaskList tasks = new TaskList(source);

        source.clear();

        assertEquals(1, tasks.size());
    }

    @Test
    void add_task_appendsTask() {
        TaskList tasks = new TaskList();
        Todo todo = new Todo("read book");

        tasks.add(todo);

        assertEquals(1, tasks.size());
        assertSame(todo, tasks.get(0));
    }

    @Test
    void mark_validOneBasedNumber_marksAndReturnsSelectedTask()
            throws ChimpanziniBananiniException {
        Todo first = new Todo("first");
        Todo second = new Todo("second");
        TaskList tasks = new TaskList(List.of(first, second));

        Task marked = tasks.mark(2);

        assertSame(second, marked);
        assertEquals(" ", first.getStatusIcon());
        assertEquals("X", second.getStatusIcon());
    }

    @Test
    void mark_numberOutsideList_throwsChatbotException() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));

        assertThrows(ChimpanziniBananiniException.class, () -> tasks.mark(0));
        assertThrows(ChimpanziniBananiniException.class, () -> tasks.mark(2));
    }

    @Test
    void delete_validOneBasedNumber_removesAndReturnsSelectedTask()
            throws ChimpanziniBananiniException {
        Todo first = new Todo("first");
        Todo second = new Todo("second");
        TaskList tasks = new TaskList(List.of(first, second));

        Task deleted = tasks.delete(1);

        assertSame(first, deleted);
        assertEquals(1, tasks.size());
        assertSame(second, tasks.get(0));
    }

    @Test
    void delete_numberOutsideList_throwsChatbotException() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));

        assertThrows(ChimpanziniBananiniException.class, () -> tasks.delete(0));
        assertThrows(ChimpanziniBananiniException.class, () -> tasks.delete(2));
    }

    @Test
    void asList_callerAttemptsModification_throwsUnsupportedOperationException() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));

        assertThrows(UnsupportedOperationException.class,
                () -> tasks.asList().add(new Todo("write book")));
        assertEquals(1, tasks.size());
    }
}
