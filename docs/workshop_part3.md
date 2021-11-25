# Parti 3 - Deployment d'une deuxieme version du service Credit

Ajoutons maintenant une deuxieme version du service dans le project.


## Étapes:

### Deploiment de la version 2.


```
oc apply -f k8s/credit-service/deploymentConfig_v2.yaml
```

### Deploiment de la Destination Rule
```
oc apply -f k8s/credit-service/destination-rule-v1-v2.yaml
```
or

oc replace -f k8s/credit-service/destination-rule-v1-v2.yaml

### Deploiment du service Virtuel
```
oc create -f k8s/credit-service/virtual-service-v1-v2.yaml
```
or
```
oc replace -f k8s/credit-service/virtual-service-v1-v2.yaml
```
