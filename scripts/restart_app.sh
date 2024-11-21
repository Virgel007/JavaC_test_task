#!/bin/bash

# Display list of files in YAML directory
echo "List of files in YAML directory:"
ls app/config/application.properties/upload_config/

# Select YAML file name for restarting application
echo "Enter YAML file name for restarting application:"
read -p "Enter YAML file name: " yaml_file_name

# Restart application with selected YAML file
java -jar /app/test_task.jar --spring.config.location=app/config/application.properties/upload_config/$yaml_file_name.yml