#!/bin/bash
cd /home/kavia/workspace/code-generation/task-organizer-330024-330040/android_todo_app_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

