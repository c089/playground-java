package infrastructure.http.spark;

import infrastructure.http.HTTPServerAdapterContract;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static spark.Spark.awaitStop;
import static spark.Spark.stop;

public class SparkHTTPServerAdapterTest extends HTTPServerAdapterContract {

    @BeforeEach
    void setUp() {
        SparkHTTPAdapter sparkHTTPAdapter = new SparkHTTPAdapter(useCase);
        sparkHTTPAdapter.start();
    }

    @AfterEach
    void tearDown() {
        stop();
        awaitStop();
    }
}
