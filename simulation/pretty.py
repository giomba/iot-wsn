import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import csv
import os
import re
import statistics

### PARAMETERS ###
RESULTSDIR='results/'

### FUNCTIONS ###
def compute_confidence_interval(samples):   # 95%
    stdev = statistics.stdev(samples)
    ci = 1.96 * stdev / np.sqrt(len(samples))
    return ci

### GLOBAL VARIABLES ###
all_route_discovery = dict()

### MAIN ###
for filename in os.listdir(RESULTSDIR):
    df = pd.read_csv(RESULTSDIR + '/' + filename,
        delimiter='\s|\t',
        index_col=0,
        nrows=4,
        engine='python',
        header=None).T

    kappa = int(df['KAPPA'].to_string().split()[1])
    i_min = int(df['I_MIN'].to_string().split()[1])
    key = str(kappa) + ':' + str(i_min)

    if (not key in all_route_discovery):
        all_route_discovery[key] = { 'samples': [], 'k': kappa, 'i_min': i_min, 'count': 0 }

    all_route_discovery[key]['count'] += 1

    with open(RESULTSDIR + '/' + filename) as file:
        for line in file:
            if (re.match('ALL-ROUTE-DISCOVERY', line)):
                t = int(line.split()[1]) / (10**6)
                all_route_discovery[key]['samples'].append(t)
                break

# now, for every k, i_min, i_max
# all_route_discovery contains an array with all samples of the measured value

print('k i_min count mean ci')
for simulation in all_route_discovery:
    all_route_discovery[simulation]['mean'] = np.mean(all_route_discovery[simulation]['samples'])
    all_route_discovery[simulation]['ci']   = compute_confidence_interval(all_route_discovery[simulation]['samples'])

    print(
        all_route_discovery[simulation]['k'],
        all_route_discovery[simulation]['i_min'],
        all_route_discovery[simulation]['count'],
        all_route_discovery[simulation]['mean'],
        all_route_discovery[simulation]['ci']
    )

exit(0)

