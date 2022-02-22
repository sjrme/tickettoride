#!/bin/bash

node ./webservice/server.js &
npm --prefix ./webapp run build
npm --prefix ./webapp run start
