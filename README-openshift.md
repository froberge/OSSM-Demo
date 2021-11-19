create database:
```oc apply -k manifests/creditdb```

deploy credit-service:
```cd credit-service
   mvn clean fabric8:deploy -P openshift```

deploy transaction-service:
```cd transaction-service
   mvn clean fabric8:deploy -P openshift```
