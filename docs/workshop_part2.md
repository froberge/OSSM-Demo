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

On doit aller dans le fichier et remplacer les valeur pour les jwtRules.

* editer le fichier  `manifest/mesh/oicd/request-authentication-crd.yml` en remplacer les placeholder [REPLACE_WITH_YOUR_VALUE] par vos valeur de keycloak. Voici un example.

> issuer: https://keycloak-sso.apps.rhcasalab.sandbox1385.opentlc.com/auth/realms/user-applications

> jwksUri: https://keycloak-sso.apps.rhcasalab.sandbox1385.opentlc.com/auth/realms/user-applications/protocol/openid-connect/certs


Executer la commande suivante pour mettre le request authentication en place.
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

On doit aller dans le fichier et remplacer les valeur pour les jwtRules.

* editer le fichier  `k8s/debit-service/request-authentication-crd.yml` en remplacer les placeholder [REPLACE_WITH_YOUR_VALUE] par vos valeur de keycloak. Voici un example.

> issuer: https://keycloak-sso.apps.rhcasalab.sandbox1385.opentlc.com/auth/realms/user-applications

> jwksUri: https://keycloak-sso.apps.rhcasalab.sandbox1385.opentlc.com/auth/realms/user-applications/protocol/openid-connect/certs


Executer la commande suivante pour mettre le request authentication en place.
```
oc apply -f k8s/debit-service/request-authentication-crd.yaml -n uc1-zonea
```


:tada: FÉLICITATION

Il est maintenant possible de faire des tests.

:point_right: Retour: [Démo](../README.md#demo)