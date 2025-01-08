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

/**
 * RabbitMQ Listener for handling messages from the transaction queue.
 *
 * <p>This class listens for messages on the queue defined in
 * {@link RabbitMqConfiguration#TRANSACTION_QUEUE} and processes them
 * based on the specified operation in the message.</p>
 *
 * <p>The listener expects messages in JSON format representing a
 * {@link TransactionDTO}, which it deserializes and processes to
 * perform create, update, or delete operations on {@link TransactionEntity}
 * in the MongoDB database.</p>
 *
 * <p>Usage of this class assumes a properly configured RabbitMQ setup
 * and a functional {@link TransactionRepository} for database operations.</p>
 *
 * @author AngryL1on
 * @version 1.0
 * @since 1.0
 */
@Component
public class RabbitMqListener {

    /**
     * Repository for performing CRUD operations on transactions.
     */
    private final TransactionRepository transactionRepository;

    /**
     * Constructs a new {@code RabbitMqListener} with the provided repository.
     *
     * @param transactionRepository The repository to use for database operations.
     */
    @Autowired
    public RabbitMqListener(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Handles incoming messages from the transaction queue.
     *
     * <p>This method listens to the queue defined in {@link RabbitMqConfiguration#TRANSACTION_QUEUE}.
     * Messages are expected to be in JSON format and represent a {@link TransactionDTO}.
     * Based on the {@code operation} field in the message, this method performs the following:
     * <ul>
     *   <li>Create: Creates a new transaction and saves it to the database.</li>
     *   <li>Update: Updates an existing transaction if it exists in the database.</li>
     *   <li>Delete: Deletes a transaction by its ID.</li>
     *   <li>Unknown: Logs an error message for unsupported operations.</li>
     * </ul>
     * </p>
     *
     * @param message The message received from the queue, expected to be in JSON format.
     */
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
