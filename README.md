# README
## Spring + PostgreSQL on Kubernetes demo

You can reproduce this demo starting from scratch, just follow these instructions and the commit history.

1. Generated from [start.spring.io](https://start.spring.io/#!type=maven-project&language=java&platformVersion=2.6.6&packaging=jar&jvmVersion=17&groupId=com.example&artifactId=spring-postgres-k8s&name=spring-postgres-k8s&description=Demo%20project%20for%20Spring%20Boot&packageName=com.example.demo&dependencies=actuator,web,data-jpa,postgresql) with web, actuator, data jpa & postgres driver.
2. Create basic web jpa app.
3. Configure db connection & schema generation.
4. Generate postgres k8s service boilerplate:

```shell
mkdir k8s
kubectl.exe create deployment postgres --image=postgres --dry-run=client -o yaml > k8s/pg-deployment.yaml
kubectl.exe create service clusterip postgres --tcp=5432:5432 -o yaml --dry-run=client > k8s/pg-service.yaml
```

5. Add postgres password & db name env vars to the deployment file.
6. Generate app k8s service boilerplate:

```shell
kubectl.exe create deployment spring-pg-k8s --image=andregs/spring-pg-k8s --dry-run=client -o yaml > k8s/app-deployment.yaml
kubectl.exe create service clusterip spring-pg-k8s --tcp=80:8080 -o yaml --dry-run=client > k8s/app-service.yaml
```

7. Build spring app and publish it to docker hub (you may want to skip tests):

```shell
.\mvnw spring-boot:build-image 
docker push andregs/spring-pg-k8s
```

8. Deploy

```shell
minikube start
kubectl.exe apply -f ./k8s
kubectl.exe rollout status deployment spring-pg-k8s
```

9. At this point the app should be running, but it's accessible inside the cluster network only.
10. Expose it and test the /todo endpoint:

```shell
minikube service --url spring-pg-k8s
```

11. Add skaffold support via:

```shell
skaffold init -k ./k8s/*.yaml
```

12. Add the whole build section to the generated yaml file, specifying how skaffold should build your app image.
13. Build & deploy to k8s with skaffold (I had to downgrade to Java 11):

```shell
skaffold.exe dev --port-forward
```

14. Pay attention to the url and port skaffold will print for you, then test your /todo endpoints.

* Notice that you can delete the repo you published to Docker Hub on step 7, because with skaffold you use only our local docker registry.