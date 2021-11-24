# Parti 3 - Deployment d'une deuxieme version du service Credit

Maintenant que nous avons une Mesh qui fonctionne nous voulons donc la sécuriser.

Pour ce faire nous allons utiliser un REALM dans keyCloak qui a déja été créé pour nous
Nous pourrons utiliser un utilisateur demo.


## Étapes:

```
oc apply -f k8s/credit-service/deploymentConfig_v2.yaml
```

```
oc apply -f k8s/credit-service/destination-rule-v1-v2.yaml
```

```
oc apply -f k8s/credit-service/virtual-service-v1-v2_50_50.yaml
```
