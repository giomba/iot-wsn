#include <stdio.h>

#include "contiki.h"
#include "contiki-net.h"
#include "rest-engine.h"
#include "net/ip/uip-debug.h"

int precious_resource = 0;

/* handlers for coap */
void res_get_handler(void* request, void* response, uint8_t* buffer, uint16_t preferred_size, int32_t* offset) {
    static char payload[16];
    sprintf(payload, "pr = %d", precious_resource);

    REST.set_header_content_type(response, REST.type.TEXT_PLAIN);
    REST.set_response_payload(response, payload, strlen(payload));
}

void res_periodic_handler();

/* declare resource and set handlers */
PERIODIC_RESOURCE(myresource, "title=\"An observable resource\";rt=\"some descriptive text\";obs", res_get_handler, NULL, NULL, NULL, 10 * CLOCK_SECOND, res_periodic_handler);

void res_periodic_handler() {
    /* sample some sensor */
    /* byte mysample = *(some_bar + some_offset); */
    ++precious_resource;

    /* Notify subscribers of the new sample */
    REST.notify_subscribers(&myresource);
}

/* contiki protothread, as usual... */
PROCESS(main_process_name, "Simple-CoAP-Server");

AUTOSTART_PROCESSES(&main_process_name);

PROCESS_THREAD(main_process_name, ev, data) {
	PROCESS_BEGIN();

	printf("Started....\n");
    /* print enabled addresses */
    printf("My addresses:\n");
    int i;
    for (i = 0; i < UIP_DS6_ADDR_NB; ++i) {
        if (uip_ds6_if.addr_list[i].isused) {
            uip_debug_ipaddr_print(&uip_ds6_if.addr_list[i].ipaddr);
            printf("\n");
        }
    }

    /* enable CoAP engine */
    rest_init_engine();
    rest_activate_resource(&myresource, "test/myresource");

    while (1) {
            PROCESS_WAIT_EVENT();
    }

    PROCESS_END();

}

