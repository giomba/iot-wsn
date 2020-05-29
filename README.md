# NetPP exhibition control wireless sensor network
NetPP is a simulation of a wireless sensor network which makes use of the IEEE stack recommended for IoT, from bottom to top: wireless IEEE 802.15.4, 6LowPAN, IPv6 with RPL routing, UDP and CoAP.
This project:
* was developed for the Advanced Networks and Wireless Systems exam (Networking++ for friends)
* exploits Contiki and Californium
* runs on Z1 motes

## Scenario
NetPP is a company which organizes public events and exhibitions, which installs a sensor (_oracle_) for each stand in order to keep track of people's flow.
* Formal requirements:
  * network must be fully operational in less than 70 minutes, which is an estimation of the time needed to mount all the stands for small daily exhibition
  * nodes run on battery and must use as less energy as possible, in order to make it possible to reuse them for multiple events
* Other requirements:
  * RPL must use objective function 0, ie find the nearest grounded root
  * Sensor messages must be RFC8428 (SenML) compliant
  * a CoAP proxy must be developed in Java using the Eclipse Californium CoAP library (_proxy_)

## Trickle parameters
In order to satisfy the formal requirements, studies from [this paper](#tricklef) have been taken in account, thus smallest possible value for _k_ and highest possible value for _I<sub>min</sub>_ have been chosen, and in particular these have been exploited:
* k = 1
* I<sub>min</sub> = 2<sup>11</sup> (ms)

30 independent [simulations](#simulation) have been run, and all of them let the first network DODAG form in less than 20 minutes.

## Repository organization
* ```client```: source code for a CoAP client
* ```cooja```: sample simulations for Cooja
* ```oracle```: source code for the sensor node
* ```proxy```: source code for the CoAP proxy
* ```simulation```: scripts for running simulations and plot performance graphs

## Run
1. choose a Cooja simulation file from ```cooja``` directory
  - start the serial socket server on node 1 (border router)
  - start the simulation
2. attach a _rpl-border-router_ to Cooja
  - you can use the one from ```examples/ipv6/rpl-border-router``` in your Contiki repository
    - if you want to reproduce the simulation results, use the modified version (which outputs the number of discovered routes)
  - connect
    ```$ make connect-router-cooja PREFIX="2001:db8:cafe:babe::1/64"```

### Proxy
1. ```$ mvn package```
2. ```$ java -jar target/proxy-2019.11.0-jar-with-dependencies.jar <prefix> <n>```

where
  - ```<prefix>``` is your network prefix (specify **all** 64 bits)
  - ```<n>``` is the number of nodes in the network

Example:
  ```$ java -jar proxy/target/proxy-2019.11.0-jar-with-dependencies.jar 2001:db8:cafe:babe 30```

Resources on the server are named after the node IPv6 address, eg: ```urn:it.unipi.ing.ce.netpp:c00000001```.

You can use a CoAP browser to list all resources on the server, for example:
* https://github.com/federicorossifr/node-coap-browser
* http://coap.me

### Client
1. ```$ mvn package```
2. ```java -jar target/client-2019.11.0-jar-with-dependencies.jar <url>```

where
  ```<url>``` is a CoAP URL

Example:
  ```$ java -jar target/client-2019.11.0-jar-with-dependencies.jar coap://[2001:db8:cafe:babe::1]/urn:it.unipi.ing.ce.netpp:c00000001```


### Simulation
The ```simulation/main.sh``` can be used to run automatically several independent simulations with Cooja.
#### Parameters
* ```REPOSITORY``` path to this repository root directory
* ```CONTIKI``` path to Contiki repository
* ```SIMULATION``` path to Cooja simulation (.csc file)
* ```REPEAT``` number of repetitions
* ```INIT``` initial repetition number (```INIT + REPEAT``` is used as random seed)

Moreover, inner ```for```s can be tuned in order to explore several combinations of _k_ and _I<sub>min</sub>_.

#### Output
Output results can be found in ```simulation/results```.

## References
<a name="tricklef">[1]</a> Vallati, Carlo & Mingozzi, Enzo. (2013). Trickle-F: Fair broadcast suppression to improve energy-efficient route formation with the RPL routing protocol
