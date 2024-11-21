@echo off

set "LOCAL_DIR=%CD%\upload_config"
set "CONTAINER_NAME=test_task-test-task-1"
set "CONTAINER_DIR=/app/config/application.properties"

REM Копируем файлы из папки на вашем компьютере в Docker контейнер
docker cp "%LOCAL_DIR%" "%CONTAINER_NAME%:%CONTAINER_DIR%"