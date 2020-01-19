# Helper for building thrift files

rm -rf ../gen-java/*

for FILE in $(ls |grep thrift)
do 
  thrift --gen java:java7 -o ../ $FILE
done