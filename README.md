# Stakefish interview api

## How it works ?

### Tech stack

- JDK 21
- [Quarkus](https://quarkus.io/) (Framework)

- Docker / [DokcerHub](https://hub.docker.com/repository/docker/andrewprogramming/skatefish-api/general) / Docker Compose(Dev environment)
- K8s (Production)
- Azure LoadBalancer
- AWS RDS PostgreSQL

### PS

- This project uses Quarkus, the Supersonic Subatomic Java Framework. If you want to learn more
  about Quarkus, please visit its website: https://quarkus.io/ .
- Two docker images will be build : **latest** and **github commit hash tag (eg. 8ff8bed)**

![img.png](img.png)

## How to start

### Start Locally (Dev Env) ###

#### Prerequest

- Docker / Docker Compose

**Two options:**

**Option 1**

Start database locally using `docker-compose up -d --build` , make sure run this command in root
folder

**Option 2**

**Prerequest:**

- JDK 21

```yaml
docker-compose -f docker-compose-dev.yaml up
```

 ```yaml
./mvnw quarkus:dev -Dquarkus.profile=dev -Dquarkus.container-image.build=false -Dquarkus.container-image.push=false
 ````

### Start in Production (Prod Env)
**PS: This project already deployed in AKS (Azure Kubernetes Service) and keep available during the interview period**
![img_4.png](img_4.png)
![img_5.png](img_5.png)
![img_8.png](img_8.png)
- CI/CD is implemented using GithubAction and pipeline script is
  here: [ci.yml](.github%2Fworkflows%2Fci.yml)
- All you need to do is submit your code and merge it to `main` branch , pipeline will trigger
  automatically [Pipeline Link](https://github.com/kobe73er/stakefish_interview/actions)
- Visit services by using Azure LoadBalancer IP (4.147.249.188), for
  example : http://4.147.249.188/v1/history


### Visit swagger UI

- http://localhost:3000/swagger-ui/ (Dev)
- http://4.147.249.188/swagger-ui/ (Production)![img_2.png](img_2.png)

### CI/CD

- By leverage the power of Quarkus framework , CI/CD can be easily implemented with GithubAction ;
- Most of the cases HELM with FluxCD or ArgoCD maybe a better option for GitOps implementation then
  Kubernetes manifests but for this case due to the limitation of time and the great advantage of
  Quarkus , Kubernetes manifests is choosed
  [![img_1.png](img_1.png)](https://github.com/kobe73er/stakefish_interview/actions)https://github.com/kobe73er/stakefish_interview/actions


## Next Step

- Implement GitOps best practise with ArgoCD / FluxCD

## Note ##

### Start dev model with production configuration

```
./mvnw quarkus:dev -Dquarkus.profile=prod
```

### Build production image and push to dockerhub locally

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

### Create namespace in K8s

```yaml
kubectl create namespace stakefish
```
