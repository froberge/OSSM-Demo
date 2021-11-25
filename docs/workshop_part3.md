# Démontrer les Destribution Rules

Ajoutons maintenant une deuxième version du service dans le project pour démontrer les destination rules.
Pour ce faire nous allons utilisé la version 2 du  [Credit Service](https://quay.io/repository/froberge/creditservice) qui existe sur Quay.io


## Étapes:

### Deploiment de la version 2.

```
oc apply -f k8s/credit-service/deploymentConfig_v2.yaml
```

### Deploiment de la Destination Rule
```
oc create -f k8s/credit-service/destination-rule-v1-v2.yaml
```
ou s'il y a déjà un destinationRule

oc replace -f k8s/credit-service/destination-rule-v1-v2.yaml

### Deploiment du service Virtuel
```
oc create -f k8s/credit-service/virtual-service-v1-v2.yaml
```
ou s'il y a déjà un virtual service
```
oc replace -f k8s/credit-service/virtual-service-v1-v2.yaml
```
