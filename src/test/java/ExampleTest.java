import net.jqwik.api.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ExampleTest {
    @Example
    void testExample() {
        assertThat(1, equalTo(1));
    }

    @Property
    void additionIsAssociative(@ForAll int a, @ForAll int b) {
        assertThat(a+b, equalTo(b+a));
    }
}
