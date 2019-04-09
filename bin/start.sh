#!/usr/bin/env bash

this="${BASH_SOURCE-$0}"
while [ -h "$this" ]; do
    ls=`ls -ld "$this"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '.*/.*' > /dev/null; then
	this="$link"
    else
	this=`dirname "$this"`/"$link"
    fi
done

# convert relative path to absolute path
bin=`dirname "$this"`
script=`basename "$this"`
bin=`cd "$bin">/dev/null; pwd`
this="$bin/$script"

if [ -z "$APP_HOME" ]; then
    export APP_HOME=`dirname "$this"`/..
fi

APP_CONF_DIR="${APP_CONF_DIR:-$APP_HOME/config}"


CLASSPATH="${APP_CONF_DIR}"
for f in $APP_HOME/lib/*.jar; do
    CLASSPATH=${CLASSPATH}:$f;
done
for f in $APP_HOME/*.jar; do
    CLASSPATH=${CLASSPATH}:$f;
done



export CLASSPATH
echo $CLASSPATH
APP_MAIN_CLASS=vn.cicdata.submit.Submit

java $APP_MAIN_CLASS $@



#./start.sh cdr_new_format.properties submit_cdr_new_encrypt.sh


