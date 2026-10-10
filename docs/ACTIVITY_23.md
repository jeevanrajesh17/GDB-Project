# Activity 23: JDBC Account Repository

## Aim
To implement a JDBC-based account repository supporting CRUD operations and atomic money transfers using SQLite.

## Objectives
- Implement account creation, retrieval, updating, and deletion.
- Retrieve all accounts and check account existence.
- Generate the next account number.
- Perform atomic transfers between accounts.
- Handle database errors using `DataAccessException`.
- Ensure failed transfers are rolled back.

## Files Created or Modified
- `src/com/gdb/exceptions/DataAccessException.java`
- `src/com/gdb/repository/AccountRepository.java`
- `src/com/gdb/repository/InMemoryAccountRepository.java`
- `src/com/gdb/repository/JdbcAccountRepository.java`
- `src/com/gdb/repository/RepositoryFactory.java`
- `src/com/gdb/domain/AbstractAccount.java`
- `src/main/resources/schema.sql`
- `src/com/gdb/tests/TestJdbcAccountRepository.java`

## Implementation
`JdbcAccountRepository` uses JDBC and SQLite to perform account operations through SQL statements. Prepared statements are used for database queries and updates.

The existing database schema is migrated when necessary to add the account PIN column.

Money transfers use database transactions. The source account is debited and the destination account is credited within the same transaction. If an operation fails, the transaction is rolled back.

`RepositoryFactory` selects `JdbcAccountRepository` when `persistence.mode=jdbc` and retains `InMemoryAccountRepository` for memory mode.

## Testing
The test program verifies:
1. Account creation.
2. Account retrieval.
3. Account updating.
4. Listing accounts.
5. Successful money transfer.
6. Rejection of an invalid transfer.
7. Rollback after a failed transfer.
8. Rejection of transfers exceeding the available balance.
9. Account deletion.
10. Test account cleanup.

## Result
The Activity 23 test completed successfully with **10 tests passed and 0 tests failed**.