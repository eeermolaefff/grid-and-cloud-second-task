#  Toy Project for Grid and Сloud Computing

## Description

The project is a toy bank simulator.

Through the web client, you can register a new user, delete an account, or log in to existing one. After you log in to the user's account, you can transfer money between users, which will be reflected on their balances.

## Implementation

The implementation consists of three modules:
- `Database module`
- `API module` for interaction with the `Database module`
- `Frontend module` for interaction with `API module`, displaying the result to the end-user

Each of the modules runs in its own container.

## Deployment

Docker images are built and published via `GitHub CI/CD`. The corresponding config is located in the `./.github/workflows` directory.

Configuration files located in the `./deployment` directory were used to deploy the program on the cluster by running the command:
        
    k create -f .



