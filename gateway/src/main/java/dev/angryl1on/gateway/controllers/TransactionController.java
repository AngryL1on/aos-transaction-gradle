package dev.angryl1on.gateway.controllers;

import dev.angryl1on.gateway.TransactionDTO;
import dev.angryl1on.grpc.DomainServiceGrpc;
import dev.angryl1on.grpc.TransactionsProto;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/transactions")
public class TransactionController {

    private final DomainServiceGrpc.DomainServiceBlockingStub stub;

    public TransactionController() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("domain-service", 8080)
                .usePlaintext()
                .build();
        this.stub = DomainServiceGrpc.newBlockingStub(channel);
    }

    // CREATE
    @PostMapping
    @CacheEvict(value = {"transactionsList", "transactions"}, allEntries = true)
    public String createTransaction(@RequestBody TransactionDTO transaction) {
        // ИСПРАВЛЕННО: CreateTransactionRequest, а не TransactionRequest
        TransactionsProto.CreateTransactionRequest request =
                TransactionsProto.CreateTransactionRequest.newBuilder()
                        .setAmount(transaction.getAmount())
                        .setDate(transaction.getDate())
                        .setType(transaction.getType())
                        .build();

        TransactionsProto.TransactionResponse response = stub.createTransaction(request);
        return response.getMessage();
    }

    // READ by ID
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

    // READ ALL
    @GetMapping
    @Cacheable(value = "transactionsList", unless = "#result == null || #result.isEmpty()")
    public List<TransactionDTO> getAllTransactions() {
        TransactionsProto.TransactionListRequest request =
                TransactionsProto.TransactionListRequest.newBuilder()
                        .build();

        TransactionsProto.TransactionListResponse response = stub.getAllTransactions(request);

        List<TransactionDTO> transactions = new ArrayList<>();
        // ИСПРАВЛЕНО: getTransactionsList() вместо getStudentsList()
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

    // UPDATE
    @PutMapping("/{id}")
    @CacheEvict(value = "transactions", key = "#id")
    public String updateTransaction(@PathVariable String id, @RequestBody TransactionDTO transactionRequest) {
        // ИСПРАВЛЕНО: UpdateTransactionRequest, а не Transaction
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

    // DELETE
    @DeleteMapping("/{id}")
    @CacheEvict(value = {"transactionsList", "transactions"}, key = "#id")
    public String deleteTransaction(@PathVariable String id) {
        // ИСПРАВЛЕНО: DeleteTransactionRequest, а не TransactionRequest/StudentRequest
        TransactionsProto.DeleteTransactionRequest request =
                TransactionsProto.DeleteTransactionRequest.newBuilder()
                        .setId(id)
                        .build();

        TransactionsProto.TransactionResponse response = stub.deleteTransaction(request);
        return response.getMessage();
    }
}
