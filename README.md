# Stakefish interview api

## How it works ?

- This project uses Quarkus, the Supersonic Subatomic Java Framework. If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .
- Two docker images will be build : **latest** and **github commit hash tag**



## How to start

### Start Locally ###
1. start database locally using `docker-compose up` , make sure run this command in root folder
2. `./mvnw quarkus:dev -Dquarkus.profile=dev -Dquarkus.container-image.build=false -Dquarkus.container-image.push=false`


### Deploy to K8s 
CI/CD is implemented using GithubAction and pipeline script is here: [ci.yml](.github%2Fworkflows%2Fci.yml)
So you need to do is submit your code and merge it to `main` branch , pipeline will trigger automatically [pipeline](https://github.com/kobe73er/stakefish_interview/actions)

### Visit swagger UI
http://localhost:3000/swagger-ui/

## Note ##

### Start dev model with production configuration

```
./mvnw quarkus:dev -Dquarkus.profile=prod
```

### Build production image and push to dockerhub

```
./mvnw package -Dquarkus.profile=prod
```
### Database credential stored in K8s 
```yaml
kubectl create secret generic stakefish-db-credentials \
  --from-literal=username='stakefishadmin' \
  --from-literal=password='Deng_pf1234' \
  -n stakefish

```
