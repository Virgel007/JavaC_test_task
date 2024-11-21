#!/bin/bash

# Папка с файлами на вашем компьютере
#LOCAL_DIR=/path/to/your/local/folder
LOCAL_DIR=C:/work/Test_task/upload_config

# Имя Docker контейнера
CONTAINER_NAME=test_task-test-task-1

# Папка в Docker контейнере, куда будут скопированы файлы конфигурации
CONTAINER_DIR=/app/config/application.properties

# Копируем файлы конфигурации в Docker контейнер
docker cp $LOCAL_DIR/. $CONTAINER_NAME:$CONTAINER_DIR