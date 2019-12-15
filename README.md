# NetPP exhibition control wireless sensor network
NetPP is a simulation of a wireless sensor network which makes use of the IEEE stack recommended for IoT, from bottom to top: wireless IEEE 802.15.4, 6LowPAN, IPv6 with RPL routing, UDP and CoAP. It runs on Z1 motes.
This project was developed for the Advanced Networks and Wireless Systems exam (Networking++ for friends)

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
Use a simulation with a pseudo random number generator enabled, eg. ```simulation-prng.csc```.
