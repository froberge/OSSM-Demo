# Sécurisé le project

Maintenant que nous avons une Mesh qui fonctionne nous voulons donc la sécuriser.

Pour ce faire nous allons utiliser un REALM dans keyCloak. 
:warning: La configuration de KeyCloak et d'un REALM ne fait pas parti de se tutorial.

## Étapes pour sécuriser l'application:

### Création de polique authorization
```
oc apply -f manifests/mesh/oidc/authorization-policy-crd.yml
```

### Création des requête authentication
```
oc apply -f manifests/mesh/oidc/request-authentication-crd.yml
```

## Étapes pour sécuriser les accès au debit service:

En sécurisant le accès au service de cette manière, les accès sont injecter au niveau du sideCar donc indépendent du code.

### Création de polique authorization
```
oc apply -f k8s/debit-service/authorization-policy-crd.yaml -n uc1-zonea
```

### Création des requête authentication
```
oc apply -f k8s/debit-service/request-authentication-crd.yaml -n uc1-zonea
```


:tada: FÉLICITATION

Il est maintenant possible de faire des tests.

:point_right: Retour: [Démo](../README.md#demo)