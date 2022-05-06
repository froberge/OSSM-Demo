# Introduction à Red Hat OpenShift Service Mesh

Bienvenue dans l'atelier de travail sur [**Red Hat OpenShift Service Mesh**](https://www.redhat.com/en/technologies/cloud-computing/openshift/what-is-openshift-service-mesh#:~:text=Red%20Hat%C2%AE%20OpenShift%C2%AE,microservices%20in%20your%20service%20mesh.)

---
## Table des matières
 * [Installation du ServiceMesh](https://github.com/froberge/howto-ocp-servicemesh-setup/)
 * [Démo](#demo)

---
## Démo

Pour cette démo nous allons utiliser 3 services:
* [Service transaction](transaction-service/README.md)
* [Service Credit](credit-service/README.md)
* [Service Debit](credit-service/README.md)

```
             +---------------------+ 
             | Transaction Service |
             +---------------------+
                       |
 +----------------+    |    +---------------+ 
 | Credit Service | <-- --> | Debit Service |
 +----------------+         +---------------+ 
```


### Contenu
1. [Mise en place du project](docs/workshop_part1.md)
2. [Mise en place de la securité](docs/workshop_part2.md)
3. [Deployer une deuxieme version](docs/workshop_part3.md)
4. [Démontrer le failOver](docs/workshop_part4.md)