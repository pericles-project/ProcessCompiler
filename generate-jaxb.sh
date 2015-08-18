#!/bin/bash

test -d src/generated-sources/java || mkdir -p src/generated-sources/java
rm -rf src/generated-sources/java/*
xjc -enableIntrospection -no-header -extension -b jaxb-binding.xjb -d src/generated-sources/java/ http://www.omg.org/spec/BPMN/20100501/BPMN20.xsd

