package dev.angryl1on.domainservice.configs;

import dev.angryl1on.domainservice.services.TransactionServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GrpcConfiguration implements CommandLineRunner {
    private final TransactionServiceImpl transactionService;

    @Autowired
    public GrpcConfiguration(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }

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
