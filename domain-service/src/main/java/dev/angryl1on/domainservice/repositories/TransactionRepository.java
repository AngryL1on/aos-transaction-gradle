package dev.angryl1on.domainservice.repositories;

import dev.angryl1on.domainservice.models.entity.TransactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on the {@link TransactionEntity}.
 *
 * <p>This interface extends {@link org.springframework.data.mongodb.repository.MongoRepository},
 * providing standard methods for interacting with a MongoDB collection. The repository
 * is responsible for accessing and managing transaction data stored in the "transactions"
 * collection.</p>
 *
 * <p>By extending {@code MongoRepository}, this interface inherits methods such as:
 * <ul>
 *   <li>{@code save} - Save or update an entity.</li>
 *   <li>{@code findById} - Retrieve an entity by its ID.</li>
 *   <li>{@code findAll} - Retrieve all entities.</li>
 *   <li>{@code deleteById} - Delete an entity by its ID.</li>
 * </ul>
 * Additional custom query methods can be added as needed.</p>
 *
 * <p>Usage of this repository assumes a properly configured MongoDB connection
 * in the application.</p>
 *
 * @author AngryL1on
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface TransactionRepository extends MongoRepository<TransactionEntity, String> {
}
