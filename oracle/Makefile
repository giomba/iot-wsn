CONTIKI_PROJECT = main

SMALL=1

CFLAGS += -DUIP_CONF_IPV6=1 -DRPL_CONF_STATS=1 -DUIP_CONF_IPV6_RPL=1 -DUIP_CONF_TCP=0 -DPROJECT_CONF_H=\"project-conf.h\"
CONTIKI = /home/giomba/workspace/uni/contiki
WITH_UIP6=1
UIP_CONF_IPV6=1
UIP_CONF_IPV6_RPL=1

APPS += er-coap rest-engine

all: $(CONTIKI_PROJECT)

include $(CONTIKI)/Makefile.include

