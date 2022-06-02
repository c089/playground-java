package infrastructure.http.jdk;

import infrastructure.http.HTTPServerAdapterContract;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class JavaHTTPServerAdapterTest extends HTTPServerAdapterContract {

    private JavaHTTPServerAdapter httpApplication;

    @BeforeEach
    void setUp() {
        httpApplication = new JavaHTTPServerAdapter(useCase);
    }

    @AfterEach
    void stop() {
        httpApplication.stop();
    }

}
