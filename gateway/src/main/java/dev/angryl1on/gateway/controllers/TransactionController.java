package dev.angryl1on.gateway.controllers;

import dev.angryl1on.gateway.models.dtos.TransactionDTO;
import dev.angryl1on.grpc.DomainServiceGrpc;
import dev.angryl1on.grpc.TransactionsProto;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for managing transactions via gRPC.
 *
 * <p>This controller provides endpoints to perform CRUD operations on transactions,
 * leveraging a gRPC Domain Service for backend processing. The endpoints include:
 * <ul>
 *   <li>Create a new transaction</li>
 *   <li>Retrieve a transaction by ID</li>
 *   <li>Retrieve all transactions</li>
 *   <li>Update an existing transaction</li>
 *   <li>Delete a transaction</li>
 * </ul>
 * </p>
 *
 * <p>Caching is used to optimize read operations, and cache eviction is applied
 * on write operations to maintain data consistency.</p>
 *
 * <p>Endpoints are exposed under the base URL {@code /api/transactions}.</p>
 *
 * @author AngryL1on
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("api/transactions")
public class TransactionController {

    /**
     * gRPC blocking stub for communicating with the Domain Service.
     */
    private final DomainServiceGrpc.DomainServiceBlockingStub stub;

    /**
     * Constructs the {@code TransactionController} and initializes the gRPC channel.
     */
    public TransactionController() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("domain-service", 8080)
                .usePlaintext()
                .build();

        this.stub = DomainServiceGrpc.newBlockingStub(channel);
    }

    /**
     * Creates a new transaction.
     *
     * <p>Sends a gRPC {@code CreateTransactionRequest} to the Domain Service.
     * Cache is cleared upon successful creation.</p>
     *
     * @param transactionRequest The transaction details provided in the request body.
     * @return A success message from the gRPC response.
     */
    @PostMapping
    @CacheEvict(value = {"transactionsList", "transactions"}, allEntries = true)
    public String createTransaction(@RequestBody TransactionDTO transactionRequest) {
        TransactionsProto.CreateTransactionRequest request =
                TransactionsProto.CreateTransactionRequest.newBuilder()
                        .setAmount(transactionRequest.getAmount())
                        .setDate(transactionRequest.getDate())
                        .setType(transactionRequest.getType())
                        .build();

        TransactionsProto.TransactionResponse response = stub.createTransaction(request);
        return response.getMessage();
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * <p>Sends a gRPC {@code GetTransactionById} request to the Domain Service.
     * The result is cached to optimize repeated reads.</p>
     *
     * @param id The ID of the transaction to retrieve.
     * @return The transaction details as a {@code TransactionDTO}.
     */
    @GetMapping("/{id}")
    @Cacheable(value = "transactions", key = "#id", unless = "#result == null")
    public TransactionDTO getTransactionById(@PathVariable String id) {
        TransactionsProto.TransactionRequest request =
                TransactionsProto.TransactionRequest.newBuilder()
                        .setId(id)
                        .build();

        TransactionsProto.TransactionResponse response = stub.getTransactionById(request);

        return new TransactionDTO(
                response.getId(),
                response.getAmount(),
                response.getDate(),
                response.getType()
        );
    }

    /**
     * Retrieves all transactions.
     *
     * <p>Sends a gRPC {@code GetAllTransactions} request to the Domain Service.
     * The result is cached to optimize repeated reads.</p>
     *
     * @return A list of all transactions as {@code TransactionDTO} objects.
     */
    @GetMapping
    @Cacheable(value = "transactionsList", unless = "#result == null || #result.isEmpty()")
    public List<TransactionDTO> getAllTransactions() {
        TransactionsProto.TransactionListRequest request =
                TransactionsProto.TransactionListRequest.newBuilder()
                        .build();

        TransactionsProto.TransactionListResponse response = stub.getAllTransactions(request);

        List<TransactionDTO> transactions = new ArrayList<>();
        for (TransactionsProto.TransactionResponse tr : response.getTransactionsList()) {
            transactions.add(new TransactionDTO(
                    tr.getId(),
                    tr.getAmount(),
                    tr.getDate(),
                    tr.getType()
            ));
        }
        return transactions;
    }

    /**
     * Updates an existing transaction.
     *
     * <p>Sends a gRPC {@code UpdateTransactionRequest} to the Domain Service.
     * Cache entries are evicted upon successful update.</p>
     *
     * @param id                The ID of the transaction to update.
     * @param transactionRequest The updated transaction details.
     * @return A success message from the gRPC response.
     */
    @PutMapping("/{id}")
    @CacheEvict(value = {"transactions", "transactionsList"}, key = "#id", allEntries = true)
    public String updateTransaction(@PathVariable String id, @RequestBody TransactionDTO transactionRequest) {
        TransactionsProto.UpdateTransactionRequest request =
                TransactionsProto.UpdateTransactionRequest.newBuilder()
                        .setId(id)
                        .setAmount(transactionRequest.getAmount())
                        .setDate(transactionRequest.getDate())
                        .setType(transactionRequest.getType())
                        .build();

        TransactionsProto.TransactionResponse response = stub.updateTransaction(request);
        return response.getMessage();
    }

    /**
     * Deletes a transaction by its ID.
     *
     * <p>Sends a gRPC {@code DeleteTransactionRequest} to the Domain Service.
     * Cache entries are evicted upon successful deletion.</p>
     *
     * @param id The ID of the transaction to delete.
     * @return A success message from the gRPC response.
     */
    @DeleteMapping("/{id}")
    @CacheEvict(value = {"transactionsList", "transactions"}, key = "#id", allEntries = true)
    public String deleteTransaction(@PathVariable String id) {
        TransactionsProto.DeleteTransactionRequest request =
                TransactionsProto.DeleteTransactionRequest.newBuilder()
                        .setId(id)
                        .build();

        TransactionsProto.TransactionResponse response = stub.deleteTransaction(request);
        return response.getMessage();
    }
}
