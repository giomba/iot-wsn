#!/bin/bash

REPOSITORY=$(pwd)
CONTIKI=/home/giomba/workspace/uni/contiki/
SIMULATION=/home/giomba/workspace/uni/anaws-proj/cooja/simulation-prng.csc
REPEAT=30
INIT=0

# Setup environment
mkdir -p "$REPOSITORY/simulation/results"

# Setup simulation parameters
for KAPPA in 1; do
    for I_MIN in 11; do
        I_MAX=4

        # set trickle parameters in project-conf.h
        for PROJECTCONF in "$REPOSITORY/oracle/project-conf.h" "$CONTIKI/examples/ipv6/rpl-border-router/project-conf.h"; do
            sed -i "s/^\#define RPL_CONF_DIO_REDUNDANCY *[0-9]*$/\#define RPL_CONF_DIO_REDUNDANCY $KAPPA/g" "$PROJECTCONF"
            sed -i "s/^\#define RPL_CONF_INTERVAL_MIN *[0-9]*$/\#define RPL_CONF_INTERVAL_MIN $I_MIN/g" "$PROJECTCONF"
            sed -i "s/^\#define RPL_CONF_DIO_INTERVAL_DOUBLINGS *[0-9]*$/\#define RPL_CONF_DIO_INTERVAL_DOUBLINGS $I_MAX/g" "$PROJECTCONF"
        done

        # compile firmware using all available cores
        cd "$REPOSITORY/oracle"
        make -j$(nproc) TARGET=z1

        cd "$CONTIKI/examples/ipv6/rpl-border-router"
        make -j$(nproc) TARGET=z1

        # Run simulation
        for (( i=$INIT; i<$INIT+$REPEAT; i++ )); do
            cd "$REPOSITORY/cooja"
            sed -i "s_<randomseed>.*<\\/randomseed>_<randomseed>$i<\\/randomseed>_g" "$SIMULATION"

            cd "$CONTIKI/tools/cooja"
            ant run_nogui -Dargs="$SIMULATION" &

            cd "$CONTIKI/examples/ipv6/rpl-border-router"
            RESULT=""
            while [ -z $RESULT ]; do    # wait for cooja border router server
                sleep 1
                RESULT=$(ss -tlnp | awk '{print $4}' | grep 60001)
            done
            make connect-router-cooja PREFIX="2001:470:c844:32::1/64"   # this will terminate as soon as simulation ends

            UNIXTIMESTAMP=$(date +%s)
            echo -e "REPEAT\t$i\nKAPPA\t$KAPPA\nI_MIN\t$I_MIN\nI_MAX\t$I_MAX\n" | cat - "$CONTIKI/tools/cooja/build/COOJA.testlog" > "$REPOSITORY/simulation/results/$UNIXTIMESTAMP"
        done
    done
done


