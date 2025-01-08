package dev.angryl1on.domainservice.repositories;

import dev.angryl1on.domainservice.models.entity.TransactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<TransactionEntity, String> {
}
