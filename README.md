# VaultWise

VaultWise is a secure banking application built with Spring Boot that simulates real-world banking operations. It includes functionalities such as customer account management, deposits, withdrawals, and transaction tracking.

## Features

- **User Management**: Register, update, and delete users.
- **Account Management**: Create and manage accounts.
- **Deposit & Withdrawal**: Perform deposits and withdrawals directly on accounts.
- **Transaction**: Creating transactions for deposits and withdrawals
- **Card Management**: Retrieve associated cards for an account.
- **Payment Tracking**: View payment history for an account.
## Technologies Used

- **Backend**: Java 22, Spring Boot
- **Database**: MySQL (or your preferred relational database)
- **Build Tool**: Gradle
- **Testing**: JUnit, Mockito
- **API Documentation**: Swagger (if enabled)

## Setup Instructions

### Prerequisites
- Java 22 or later
- MySQL or another supported database
- Gradle installed

### Installation & Running

1. Clone the repository:
   ```sh
   git clone https://github.com/gzimju/vaultwise.git
   cd vaultwise
   ```

2. Configure the database:
    - Update `application.properties` or `application.yml` with your database credentials.

3. Build and run the application:
   ```sh
   ./gradlew bootRun
   ```

4. API is available at:
   ```
   http://localhost:8080/api/
   ```

### Running Tests
To execute unit tests, run:
```sh
./gradlew test
```


## Contributing
Pull requests are welcome. Please ensure all tests pass before submitting.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

