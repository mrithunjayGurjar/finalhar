#!/bin/bash

echo "Starting CRM Ticketing System..."

if [ ! -d "/tmp/mysql-data" ]; then
    echo "Initializing MySQL database..."
    mysqld --initialize-insecure --datadir=/tmp/mysql-data
fi

if ! pgrep -x mysqld > /dev/null; then
    echo "Starting MySQL server..."
    mysqld --datadir=/tmp/mysql-data \
           --port=3306 \
           --socket=/tmp/mysql.sock \
           --mysqlx=0 \
           --pid-file=/tmp/mysql.pid &
    MYSQL_PID=$!
    
    echo "Waiting for MySQL to be ready..."
    for i in {1..30}; do
        if mysql -u root -h 127.0.0.1 -e "SELECT 1" > /dev/null 2>&1; then
            echo "MySQL is ready!"
            break
        fi
        echo "Waiting for MySQL... ($i/30)"
        sleep 2
    done
else
    echo "MySQL is already running"
fi

echo "Starting Spring Boot application..."
java -jar target/ticketing-system-1.0.0.jar