# Architecture Diagram

![Image](https://github.com/mmstf00/airbnb-clone-backend/assets/65444856/48e9b13e-909f-4700-91bc-12253e9f31d5)

### Keycloak setup

1. Open `localhost:8181`
    - username: `admin`
    - password: `admin`

2. Generate secret
    - Open `Clients` tab from left side
    - Open `spring-cloud-client`
    - Go to `Credentials` tab and click Regenerate Secret

### Execute HTTP requests with Postman

1. Add the OAuth config
    - Open `Authorization`
    - Select Type `OAuth 2.0`
    - Configure New Token
        - Token Name: `token`
        - Grant Type: `Client Credentials`
        - Access Token URL: The `token_endpoint` value from OpenId Configuration,
          replace `localhost` with `keycloak`
        - Client ID: `spring-cloud-client`
        - Client Secret: Get from 3rd of Keycloak setup.
        - Click `Get New Access Token` and `Use Token`
        - Send the Request

## Available endpoints

|     Name      |        Address        |
|:-------------:|:---------------------:|
| API Endpoint  | http://localhost:8181 |
| Eureka Server | http://localhost:8761 |
|   Keycloak    | http://localhost:8080 |
|    Zipkin     | http://localhost:9411 |

## Notes

- It takes around 30 seconds for eureka server to register the services after starting
- After restarting a service, new Oauth2 token should be generated