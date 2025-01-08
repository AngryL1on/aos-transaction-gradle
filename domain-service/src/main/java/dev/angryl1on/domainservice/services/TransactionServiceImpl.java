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

@Service
public class TransactionServiceImpl extends DomainServiceGrpc.DomainServiceImplBase {

    private final TransactionRepository transactionRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, RabbitTemplate rabbitTemplate) {
        this.transactionRepository = transactionRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

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
                            "CREATE"  // <- явное указание операции
                    )
            );
            rabbitTemplate.convertAndSend(
                    RabbitMqConfiguration.TRANSACTION_EXCHANGE,
                    RabbitMqConfiguration.TRANSACTION_ROUTING_KEY,
                    jsonMessage
            );

            // Возвращаем synch-ответ, что всё ок
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
