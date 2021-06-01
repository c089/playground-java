import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ExampleTest {
    @Example
    void testExample() {
        assertThat(1, equalTo(1));
    }

    @Property
    void additionIsAssociative(@ForAll int a, @ForAll int b) {
        assertThat(a + b, equalTo(b + a));
    }

    @Example
    void testJava15RecordsPointExample() {
        record Point(int x, int y) {
        }

        assertThat(new Point(1, 2), is(equalTo(new Point(1, 2))));
        assertThat(new Point(1, 2), is(not(equalTo(new Point(2, 1)))));
    }

    @Example
    void testMockito() {
        List mockedList = mock(List.class);
        mockedList.add("one");
        verify(mockedList).add("one");
    }
}
