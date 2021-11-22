QUERY=$(oc get virtualservice/transaction -o jsonpath='{.spec.http[0].match[0].uri.exact}')
GATEWAY_URL="http://$(oc get route istio-ingressgateway -n istio-system --template='{{ .spec.host }}')$QUERY"
echo $GATEWAY_URL
