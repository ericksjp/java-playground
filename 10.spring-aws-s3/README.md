# Spring AWS S3 Demo

This project demonstrates a simple Spring Boot application that interacts with
AWS S3 to upload and download JSON files. It provides two REST endpoints for
managing product lists stored in an S3 bucket.

## Prerequisites

- Java 17 or higher
- AWS account with an S3 bucket
- AWS credentials (Access Key and Secret Key)

## Endpoints 

- **PUT /products/{store_name}** : Upsert a product list as a JSON file to the S3 bucket.
- **GET /products/{store_name}** : Retrieve a product list stored in the S3 bucket.

## Required Environment Variables

- `PORT`: The port on which the application will run (default: `8080`).
- `AWS_ACESS_KEY`: Your AWS Access Key.
- `AWS_SECRET_KEY`: Your AWS Secret Key.
- `AWS_REGION`: The AWS region where your S3 bucket is located.
- `AWS_BUCKET`: The name of your S3 bucket.

## Running the application

```bash
mvnw spring-boot:run
```

