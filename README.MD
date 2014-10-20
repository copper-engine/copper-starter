cooper-starter
==============

A quickstarter example project for cooper-engine.

Features of copper-engine:
---------------------------
* a workflow engine that provides unlimited "threads" 
* threads will be serialized transient or persistent
* Workflow-Files with wait()-continuations coded in Java
* Workflow-Files with version attributes to support old long running jobs
* Workflow with typesafe RequestData and Response object.
* Workflows can be updated at runtime (by changing the source files)


What you need to have:
----------------------
* Java JDK 1.6+
* gradle 2.0+
* Java Ide eclipse or intellij IDEA

What you need to know:
----------------------

* Java Source Code in src/workflow/java is parsed, compiled and instrumented at runtime

Injecting Services into workflow
--------------------------------

* Spring
* Pojo

Todo
-----
* asm 3.3 dependency duplicate
* gradle run task
