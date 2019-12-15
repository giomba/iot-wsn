#ifndef PROJECT_CONF_H_
#define PROJECT_CONF_H_

/* Set the max response payload before fragmentation */
#undef REST_MAX_CHUNK_SIZE
#define REST_MAX_CHUNK_SIZE 64
<<<<<<< HEAD
#undef COAP_MAX_OPEN_TRANSACTIONS // Set the max number of concurrent transactions
#define COAP_MAX_OPEN_TRANSACTIONS 4
#undef NBR_TABLE_CONF_MAX_NEIGHBORS // Set the max number of entries in neighbors table
#define NBR_TABLE_CONF_MAX_NEIGHBORS 15
#undef UIP_CONF_MAX_ROUTES // Set the max number of routes handled by the node
#define UIP_CONF_MAX_ROUTES 15
#undef UIP_CONF_BUFFER_SIZE // Set the amount of memory reserved to the uIP packet buffer
#define UIP_CONF_BUFFER_SIZE 280

#undef RPL_CONF_DIO_REDUNDANCY
#define RPL_CONF_DIO_REDUNDANCY 15

#undef RPL_CONF_DIO_INTERVAL_MIN
#define RPL_CONF_DIO_INTERVAL_MIN 10

#undef RPL_CONF_DIO_INTERVAL_DOUBLINGS 
#define RPL_CONF_DIO_INTERVAL_DOUBLINGS 25
=======

/* Set the max number of concurrent transactions */
#undef COAP_MAX_OPEN_TRANSACTIONS
#define COAP_MAX_OPEN_TRANSACTIONS 2

/* Set the max number of entries in neighbors table */
#undef NBR_TABLE_CONF_MAX_NEIGHBORS
#define NBR_TABLE_CONF_MAX_NEIGHBORS 10

/*Set the max number of routes handled by the node */
#undef UIP_CONF_MAX_ROUTES
#define UIP_CONF_MAX_ROUTES 32

/* Set the amount of memory reserved to the uIP packet buffer */
#undef UIP_CONF_BUFFER_SIZE
#define UIP_CONF_BUFFER_SIZE 192

/* RPL specific tuning parameters */
#undef RPL_CONF_DIO_REDUNDANCY
#define RPL_CONF_DIO_REDUNDANCY 10

#undef RPL_CONF_INTERVAL_MIN
#define RPL_CONF_INTERVAL_MIN 3

#undef RPL_CONF_DIO_INTERVAL_DOUBLINGS
#define RPL_CONF_DIO_INTERVAL_DOUBLINGS 8

>>>>>>> rplfix
#endif

