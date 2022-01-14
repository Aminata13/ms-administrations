#!/bin/bash

# bash build-app.sh branche "comment"

echo 'Génération du fichier jar'

mvn clean package

cp /var/www/html/projets/entreprises/asma/safe-logistics/logics/gestion-entreprises-users/target/gestion-entreprises-users-0.0.1-SNAPSHOT.jar /var/www/html/projets/entreprises/asma/safe-logistics/deployments/backend-deploy/dev/artifacts/administrations-0.0.1-SNAPSHOT.jar

cd  /var/www/html/projets/entreprises/asma/safe-logistics/deployments/backend-deploy

git status

git add .

git commit -m "$2"

git push origin $1

echo 'Done !!!'
