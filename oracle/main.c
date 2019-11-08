#include <stdio.h>

#include "contiki.h"
#include "contiki-net.h"
#include "rest-engine.h"
#include "net/ip/uip-debug.h"

int precious_resource = 0;
char sensor_name[32];    /* for RFC8428 */

/* handlers for coap */
void res_get_handler(void* request, void* response, uint8_t* buffer, uint16_t preferred_size, int32_t* offset) {
    static char payload[128];
    sprintf(payload, "[{\"n\":\"urn:it.unipi.ing.ce.netpp:%s\",\"v\":%d}]", sensor_name, precious_resource);

    REST.set_header_content_type(response, REST.type.TEXT_PLAIN);
    REST.set_response_payload(response, payload, strlen(payload));

    printf("[D] GET handled\n");
}

void res_periodic_handler();

/* declare resource and set handlers */
PERIODIC_RESOURCE(myresource, "title=\"An observable resource\";rt=\"some descriptive text\";obs", res_get_handler, NULL, NULL, NULL, 10 * CLOCK_SECOND, res_periodic_handler);

void res_periodic_handler() {
    /* sample some sensor */
    /* byte mysample = *(some_bar + some_offset); */
    ++precious_resource;
    printf("sampled the precious resource: %d\n", precious_resource);

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
    /* set sensor_name as mac address */
    for (i = 0; i < sizeof(uip_lladdr.addr); ++i) {
        sprintf(sensor_name + i, "%02x", (unsigned char)uip_lladdr.addr[i]);
    }


    /* enable CoAP engine */
    rest_init_engine();
    rest_activate_resource(&myresource, "PreciousResource");

    while (1) {
            PROCESS_WAIT_EVENT();
    }

    PROCESS_END();

}

