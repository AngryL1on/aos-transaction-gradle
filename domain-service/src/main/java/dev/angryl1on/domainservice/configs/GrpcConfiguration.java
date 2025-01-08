package dev.angryl1on.domainservice.configs;

import dev.angryl1on.domainservice.services.TransactionServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Configuration class for setting up a gRPC server in the application.
 * This class implements {@link CommandLineRunner}, enabling it to execute
 * logic upon application startup.
 *
 * <p>The gRPC server is configured to listen on port 8080 and to register
 * the {@link TransactionServiceImpl} as its service.</p>
 *
 * <p>Upon startup, the server begins listening for incoming requests
 * and blocks the application from exiting until termination.</p>
 *
 * <p>Usage of this class assumes a properly implemented and
 * {@code @Component}-annotated {@link TransactionServiceImpl} bean
 * is available in the Spring application context.</p>
 *
 * @author AngryL1on
 * @version 1.0
 * @since 1.0
 */
@Component
public class GrpcConfiguration implements CommandLineRunner {
    private final TransactionServiceImpl transactionService;

    /**
     * Constructor for injecting the {@link TransactionServiceImpl}.
     *
     * @param transactionService The service implementation to be used by the gRPC server.
     */
    @Autowired
    public GrpcConfiguration(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Starts the gRPC server when the application begins running.
     *
     * <p>The server is configured to listen on port 8080 and register the provided
     * {@link TransactionServiceImpl}. Once started, the server blocks the main
     * thread and continues to handle incoming gRPC requests until termination.</p>
     *
     * @param args Command-line arguments passed to the application (not used).
     * @throws Exception If an error occurs during server startup or execution.
     */
    @Override
    public void run(String... args) throws Exception {
        Server server = ServerBuilder.forPort(8080)
                .addService(transactionService)
                .build();

        server.start();
        System.out.println("Server started, listening on " + server.getPort());
        server.awaitTermination();
    }
}
