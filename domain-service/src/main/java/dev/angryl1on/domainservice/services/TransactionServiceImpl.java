package dev.angryl1on.domainservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.angryl1on.domainservice.configs.RabbitMqConfiguration;
import dev.angryl1on.domainservice.models.dtos.TransactionDTO;
import dev.angryl1on.domainservice.models.entity.TransactionEntity;
import dev.angryl1on.domainservice.repositories.TransactionRepository;
import dev.angryl1on.grpc.*;
import dev.angryl1on.grpc.DomainServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the gRPC DomainService for managing transactions.
 *
 * <p>This service provides the following operations:
 * <ul>
 *   <li>Create a new transaction</li>
 *   <li>Retrieve a transaction by its ID</li>
 *   <li>Retrieve all transactions</li>
 *   <li>Update an existing transaction</li>
 *   <li>Delete a transaction</li>
 * </ul>
 *
 * <p>The service interacts with RabbitMQ for message-driven processing and MongoDB
 * for transaction data persistence. All methods use gRPC request-response patterns.</p>
 *
 * <p>Usage of this service assumes proper configuration of RabbitMQ, MongoDB,
 * and gRPC dependencies in the application.</p>
 *
 * @author AngryL1on
 * @version 1.0
 * @since 1.0
 */
@Service
public class TransactionServiceImpl extends DomainServiceGrpc.DomainServiceImplBase {

    /**
     * Repository for accessing and managing transaction data in MongoDB.
     */
    private final TransactionRepository transactionRepository;

    /**
     * RabbitMQ template for sending messages to RabbitMQ exchanges.
     */
    private final RabbitTemplate rabbitTemplate;

    /**
     * Constructs the TransactionServiceImpl with required dependencies.
     *
     * @param transactionRepository The repository to manage transaction data.
     * @param rabbitTemplate        The RabbitMQ template for sending messages.
     */
    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, RabbitTemplate rabbitTemplate) {
        this.transactionRepository = transactionRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Handles the creation of a new transaction.
     *
     * <p>Sends a "CREATE" operation message to RabbitMQ and returns a success
     * response to the client if the operation is successful.</p>
     *
     * @param request          The gRPC request containing transaction details.
     * @param responseObserver The gRPC observer to send the response.
     */
    @Override
    public void createTransaction(CreateTransactionRequest request, StreamObserver<TransactionResponse> responseObserver) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(
                    new TransactionDTO(
                            null,
                            request.getAmount(),
                            request.getDate(),
                            request.getType(),
                            "CREATE"
                    )
            );
            rabbitTemplate.convertAndSend(
                    RabbitMqConfiguration.TRANSACTION_EXCHANGE,
                    RabbitMqConfiguration.TRANSACTION_ROUTING_KEY,
                    jsonMessage
            );

            TransactionResponse response = TransactionResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Transaction creation request sent successfully")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * @param request          The gRPC request containing the transaction ID.
     * @param responseObserver The gRPC observer to send the response.
     */
    @Override
    public void getTransactionById(TransactionRequest request,
                                   StreamObserver<TransactionResponse> responseObserver) {
        Optional<TransactionEntity> transactionOpt = transactionRepository.findById(request.getId());
        if (transactionOpt.isPresent()) {
            TransactionEntity entity = transactionOpt.get();

            TransactionResponse response = TransactionResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Transaction found")
                    .setId(entity.getId())
                    .setAmount(entity.getAmount())
                    .setDate(entity.getDate())
                    .setType(entity.getType())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new RuntimeException("Transaction not found"));
        }
    }

    /**
     * Retrieves all transactions.
     *
     * @param request          The gRPC request (no parameters required).
     * @param responseObserver The gRPC observer to send the response containing all transactions.
     */
    @Override
    public void getAllTransactions(TransactionListRequest request,
                                   StreamObserver<TransactionListResponse> responseObserver) {
        List<TransactionEntity> transactions = transactionRepository.findAll();

        TransactionListResponse.Builder responseBuilder = TransactionListResponse.newBuilder();

        for (TransactionEntity entity : transactions) {
            TransactionResponse transactionResponse = TransactionResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("OK")
                    .setId(entity.getId())
                    .setAmount(entity.getAmount())
                    .setDate(entity.getDate())
                    .setType(entity.getType())
                    .build();

            responseBuilder.addTransactions(transactionResponse);
        }

        TransactionListResponse response = responseBuilder.build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * Updates an existing transaction.
     *
     * <p>Sends an "UPDATE" operation message to RabbitMQ and returns a success
     * response to the client if the operation is successful.</p>
     *
     * @param request          The gRPC request containing updated transaction details.
     * @param responseObserver The gRPC observer to send the response.
     */
    @Override
    public void updateTransaction(UpdateTransactionRequest request,
                                  StreamObserver<TransactionResponse> responseObserver) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            TransactionDTO dto = new TransactionDTO(
                    request.getId(),
                    request.getAmount(),
                    request.getDate(),
                    request.getType()
            );
            dto.setOperation("UPDATE");

            String jsonMessage = objectMapper.writeValueAsString(dto);

            System.out.println("Sending JSON update message: " + jsonMessage);

            rabbitTemplate.convertAndSend(
                    RabbitMqConfiguration.TRANSACTION_EXCHANGE,
                    RabbitMqConfiguration.TRANSACTION_ROUTING_KEY,
                    jsonMessage
            );

            TransactionResponse response = TransactionResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Transaction update request sent successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }

    /**
     * Deletes a transaction by its ID.
     *
     * <p>Sends a "DELETE" operation message to RabbitMQ and returns a success
     * response to the client if the operation is successful.</p>
     *
     * @param request          The gRPC request containing the transaction ID.
     * @param responseObserver The gRPC observer to send the response.
     */
    @Override
    public void deleteTransaction(DeleteTransactionRequest request, StreamObserver<TransactionResponse> responseObserver) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(
                    new TransactionDTO(
                            request.getId(),
                            null,
                            null,
                            null,
                            "DELETE"
                    )
            );
            rabbitTemplate.convertAndSend(RabbitMqConfiguration.TRANSACTION_EXCHANGE, RabbitMqConfiguration.TRANSACTION_ROUTING_KEY, jsonMessage);

            TransactionResponse response = TransactionResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Transaction deletion request sent successfully")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }
}
