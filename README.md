# ProcessCompiler

The Process Compiler is a core component of the PERICLES architecture that compiles RDF-based preservation processes into executable BPMN workflows. It helps to realise the _preservation by design_ approach by narrowing the gap between the theoretical model-driven preservation domain and the real information systems domain. The capabilities provided by the PC are:
- _Validate the implementation of a process_: it ensures the RDF-description of a process and an implementation file are coherent, i.e. all process elements are present in the BPMN file with the right names and data types.
- _Validate a process aggregation_: it ensures the process aggregation is valid, i.e. subprocesses exist and have valid implementation files, data connections are feasible, that means, the source and target slots exist and their data types are compatible. 
- _Compile an aggregated process_: it validates an aggregated process and creates its implementation file by combining the implementations of its subprocesses in the way specified by the process and data flows.

## Installation

Install Java and Maven
```
apt-get install java maven
```
Clone the project
```
git clone git@github.com:pericles-project/ProcessCompiler.git
```
Compile the project with Maven skipping the tests
```
cd ProcessCompiler; mvn compile package -DskipTests=true
```

## Usage

### Start the server

The Process Compiler can be used in one of two ways: As a standalone command line tool, or as a shared web service. In order to access the Process Compilers functionality through its web service API it is necessary to start a server process first. The following command start the Process Compiler in server mode and listens to web requests until the process is stopped again with `Ctrl+c:
```
processcompiler -s <URL> -r <REPO> server [-p <PORT>]
```
where:
* `<URL>` is the path to the triplestore service
* `<REPO>` is the name of the repository 
* `<PORT>` is the port to access the PC server (optional, defaults to 8080)

### Validate Implementation

Validate an implementation via CLI:
```
processcompiler -s <URL> -r <REPO> validate_implementation [-h] <PROC> <IMPL>
```
where:
* `<URL>` is the path to the store service 
* `<REPO>` is the name of the repository 
* `<PROC>` is the process, which can be given by
  * the ID of a process entity already stored in the triplestore 
  * a json file with the description of the process entity containing the fields related to the process attributes
* `<IMP>` is the implementation, which can be given by 
  * the ID of a process entity already stored in the triplestore 
  * an implementation file, i.e. BPMN file

Validate an implementation via REST API:
```
curl -XPUT --data <DATA> -HContent-Type:application/json http://<URL>:<PORT>/validate_implementation
```
where:
* `<URL>` and `<PORT>` are the domain/IP and port to the process compiler server
* `<DATA>` is the request data in JSON format, it contains:
  * “id”: mandatory, this field identifies the process which can be given by 
    * the ID of a process entity already stored in the triplestore 
    * the name of a json file with the description of the process entity containing the fields related to the process attributes
  * “implementation”: mandatory, this field identifies the implementation which can be given by 
    * the ID of an implementation entity already stored in the triplestore 
    * an implementation file, i.e. BPMN file
  * “ermr”: optional, url of the store service 
  * “store”: optional, repository name

### Validate Aggregation

Validate a process aggregation via CLI:
```
processcompiler -s <URL> -r <REPO> validate_aggregation [-h] <PROC>
```
where:
* `<URL>` is the path to the store service 
* `<REPO>` is the name of the repository 
* `<PROC>` is the aggregated process, which can be given by
  * the ID of an aggregated process entity already stored in the triplestore
  * the name of a json file with the description of the aggregated process entity containing the fields related to the process attributes

Validate an aggregation via REST API:
```
curl -XPUT --data <DATA> -HContent-Type:application/json http://<URL>:<PORT>/validate_implementation
```
where:
* `<URL>` and `<PORT>` are the domain/IP and port to the process compiler server
* `<DATA>` is the request data in JSON format, it contains:
  * “id”: mandatory, this field identifies the process which can be given by
    * the ID of a process entity already stored in the triplestore
    * the name of a json file with the description of the process entity containing the fields related to the process attributes
  * “ermr”: optional, url of the store service 
  * “store”: optional, repository name

### Compile

Compile an aggregated process via CLI:
```
processcompiler -s <URL> -r <REPO> compile [-h] -o <FILE> <PROC>
```
where:
* `<URL>` is the path to the store service 
* `<REPO>` is the name of the repository 
* `<FILE>` is the name of the file to output the bpmn description
* `<PROC>` is the aggregated process, which can be given by
  * the ID of an aggregated process entity already stored in the triplestore
  * the name of a json file with the description of the aggregated process entity containing the fields related to the process attributes 

Compile an aggregated process via REST API:
```
curl -XPUT --data <DATA> -HContent-Type:application/json http://<URL>:<PORT>/compile
```
where:
* `<URL>` and `<PORT>` are the domain/IP and port to the process compiler server
* `<DATA>` is the request data in JSON format, it contains:
  * “id”: mandatory, this field identifies the process which can be given by
    * the ID of a process entity already stored in the triplestore
    * the name of a json file with the description of the process entity containing the fields related to the process attributes
  * “ermr”: optional, url of the store service 
  * “store”: optional, repository name

## Documentation

A full description of the Process Compiler and its API can be found in the <a href="http://pericles-project.eu/deliverables/54">PERICLES Deliverable 6.4</a> and <a href="http://pericles-project.eu/deliverables/80">PERICLES Deliverable 6.6</a>.

The theoretical approach behind the Process Compiler is described in the paper "Campos-López N. and Wannenwetsch O. (2016). <b>The PERICLES Process Compiler: Linking BPMN Processes into Complex Workflows for Model-Driven Preservation in Evolving Ecosystems.</b>In _Proceedings of the 12th International Conference on Web Information Systems and Technologies - Volume 1: WEBIST_, ISBN 978-989-758-186-1, pages 76-83. DOI: 10.5220/0005759800760083" (available at <a href="http://www.scitepress.org/DigitalLibrary/PublicationsDetail.aspx?ID=E24vyfHKFm8=&t=1">SCITEPRESS Digital Library</a>) presented on the <a href="http://www.webist.org/?y=2016">12th International Conference on Web Information Systems and Technologies (WEBIST 2016)</a>.

## License

The Process Compiler is licensed under the Apache License, Version 2.0.

## Credits

 _This project has received funding from the European Union’s Seventh Framework Programme for research, technological development and demonstration under grant agreement no FP7- 601138 PERICLES._

 <a href="http://ec.europa.eu/research/fp7"><img src="https://github.com/pericles-project/ProcessCompiler/blob/images/images/EU_Logo.png" width="110"/></a>
 <a href="http://www.pericles-project.eu/"> <img src="https://github.com/pericles-project/ProcessCompiler/blob/images/images/PERICLES_Logo.jpg" width="200" align="right"/> </a>

<a href="http://www.gwdg.de/"><img src="https://github.com/pericles-project/ProcessCompiler/blob/images/images/GWDG_Logo.jpg" width="230"/></a>

