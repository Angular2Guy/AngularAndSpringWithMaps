#!/bin/sh
helm delete angularandspringwithmaps
helm install angularandspringwithmaps ./  --set serviceType=NodePort