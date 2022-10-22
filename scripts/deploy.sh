#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
  target/web-app-0.0.1-SNAPSHOT.jar \
  liza@192.168.0.103:/home/liza/

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa liza@192.168.0.103 << EOF

pgrep java | xargs kill -9
nohup java -jar web-app-0.0.1-SNAPSHOT.jar > log-learn-words.txt &

EOF

echo 'Bye'