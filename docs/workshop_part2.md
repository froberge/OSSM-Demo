# Sécurisé le project

Maintenant que nous avons une Mesh qui fonctionne nous voulons donc la sécuriser.

Pour ce faire nous allons utiliser un REALM dans keyCloak. 
:warning: La configuration de KeyCloak et d'un REALM ne fait pas parti de se tutorial.

## Étapes:

### Création de polique authorization
```
oc apply -f manifests/mesh/oidc/authorization-policy-crd.yml
```

### Création des requête authentication
```
oc apply -f manifests/mesh/oidc/request-authentication-crd.yml
```

:tada: FÉLICITATION

Il est maintenant possible de faire des tests.