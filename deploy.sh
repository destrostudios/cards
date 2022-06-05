#!/bin/bash

VERSION=$1
SERVER=$2
CLIENT=$3

# Checkout
git clone https://github.com/destrostudios/cards.git
if [ -n "$VERSION" ]; then
  git checkout "$VERSION"
fi

# Build
mvn clean install

# Deploy (Client)
rm -rf "${CLIENT}"*
mv assets "${CLIENT}"
mv frontend/frontend-application/target/libs "${CLIENT}"
mv frontend/frontend-application/target/frontend-application-0.0.1.jar "${CLIENT}cards.jar"
echo -n "./assets/" > "${CLIENT}assets.ini"
curl https://destrostudios.com:8080/apps/11/updateFiles

# Deploy (Server)
mv backend/backend-application/target/backend-application-0.0.1-jar-with-dependencies.jar "${SERVER}cards.jar"
sh "${SERVER}control.sh" restart