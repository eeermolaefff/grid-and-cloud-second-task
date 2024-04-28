# Grid and cloud project (Task 2)

## Description

The project is a toy bank simulator.

Through the web client, you can register a new user, delete an account, or log in to existing one. After you log in to the user's account, you can transfer money between users, which will be reflected on their balances.

## Implementation

The implementation consists of three modules:
- `Database module`
- `API module` for interaction with the `Database module`
- `Frontend module` for interaction with `API module`, displaying the result to the end-user

Each of the modules runs in its own container.

## Extra achievement

The application is deployed on my local cluster and available from the Internet on: https://grid-and-cloud-frontend.project-stambul.ru

By scanning the network of a running website, you can see how the `Frontend module` and the `API module` (available on: https://grid-and-cloud-api.project-stambul.ru)
 interact with each other.

## Deployment

Docker images are built and published via `GitHub CI/CD`. The corresponding config is located in the `./.github/workflows` directory.

Configuration files located in the `./deployment` directory were used to deploy the program on the cluster by running the command:
        
    k create -f .



