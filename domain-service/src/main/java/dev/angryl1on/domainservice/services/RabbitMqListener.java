package dev.angryl1on.domainservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.angryl1on.domainservice.configs.RabbitMqConfiguration;
import dev.angryl1on.domainservice.models.dtos.TransactionDTO;
import dev.angryl1on.domainservice.models.entity.TransactionEntity;
import dev.angryl1on.domainservice.repositories.TransactionRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RabbitMqListener {
    private final TransactionRepository transactionRepository;

    @Autowired
    public RabbitMqListener(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @RabbitListener(queues = RabbitMqConfiguration.TRANSACTION_QUEUE)
    public void handleMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TransactionDTO transactionDTO = objectMapper.readValue(message, TransactionDTO.class);

            System.out.println("Message received: " + transactionDTO);

            switch (transactionDTO.getOperation()) {
                case "CREATE" -> {
                    TransactionEntity transaction = new TransactionEntity(
                            transactionDTO.getAmount(),
                            transactionDTO.getDate(),
                            transactionDTO.getType()
                    );
                    transactionRepository.save(transaction);
                    System.out.println("Transaction created: " + transaction);
                }
                case "UPDATE" -> {
                    Optional<TransactionEntity> transactionOpt = transactionRepository.findById(transactionDTO.getId());
                    if (transactionOpt.isPresent()) {
                        TransactionEntity transaction = transactionOpt.get();
                        transaction.setAmount(transactionDTO.getAmount());
                        transaction.setDate(transactionDTO.getDate());
                        transaction.setType(transactionDTO.getType());
                        transactionRepository.save(transaction);
                        System.out.println("Transaction updated: " + transaction);
                    } else {
                        System.out.println("Transaction with ID " + transactionDTO.getId() + " not found for update");
                    }
                }
                case "DELETE" -> {
                    transactionRepository.deleteById(transactionDTO.getId());
                    System.out.println("Transaction deleted with ID: " + transactionDTO.getId());
                }
                default -> {
                    System.out.println("Unknown operation: " + transactionDTO.getOperation());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
