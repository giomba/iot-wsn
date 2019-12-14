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
    i_max = int(df['I_MAX'].to_string().split()[1])
    key = str(kappa) + ':' + str(i_min) + ':' + str(i_max)

    if (not key in all_route_discovery):
        all_route_discovery[key] = { 'samples': [], 'k': kappa, 'i_min': i_min }

    with open(RESULTSDIR + '/' + filename) as file:
        for line in file:
            if (re.match('ALL-ROUTE-DISCOVERY', line)):
                t = int(line.split()[1]) / (10**6)
                all_route_discovery[key]['samples'].append(t)
                break

# now, for every k, i_min, i_max
# all_route_discovery contains an array with all samples of the measured value

print('k i_min mean ci')
for simulation in all_route_discovery:
    all_route_discovery[simulation]['mean'] = np.mean(all_route_discovery[simulation]['samples'])
    all_route_discovery[simulation]['ci']   = compute_confidence_interval(all_route_discovery[simulation]['samples'])

    print(
        #simulation,
        all_route_discovery[simulation]['k'],
        all_route_discovery[simulation]['i_min'],
        all_route_discovery[simulation]['mean'],
        all_route_discovery[simulation]['ci']
    )

exit(0)

'''
d = []
f = []

# compute fd in our interval of interest
d = np.arange(0, (M * 2**0.5 / 2))
for i in d:
    f.append(fd(i))

# compute integral of fd
integral = []
last = 0
for i in range(0, len(f)):
    part = f[i]
    last = last + part
    integral.append(last)

print("normalization fd: ", np.trapz(f))

# make plots
fig, ax1 = plt.subplots()

color = 'tab:red'
ax1.set_xlabel('d [m]')
ax1.set_ylabel('fD(d)')
ax1.plot(d, f, 'c,', label='fD')

ax2 = ax1.twinx()

color = 'tab:blue'
ax2.set_ylabel('FD(d)')
ax2.plot(d, integral, 'b,', label="FD")

plt.legend()
plt.show()

def fs(s):
    if s >= 0 and s <= (T * M**2 ) / 4:
        return np.pi / ( T * M**2 )
    elif s >= (T * M**2 ) / 4 and s <= (T * M**2) / 2:
        return (np.pi / (T * M**2)) - (4 / (T * M**2)) * np.arccos(M/2 * ((T / s)**0.5))
    else:
        return 0

### Distribution of Service Time
s = T * d**2
f = []
for i in s:
    f.append(fs(i))

print("normalization fs: ", np.trapz(f, s))

# compute integral of fs
integral = [ 0 ]
last = 0
for i in range(1, len(f)):
    part = f[i] * (s[i] - s[i - 1])
    last = last + part
    integral.append(last)

# make plots
fig, ax1 = plt.subplots()
color = 'tab:red'
ax1.set_xlabel('s [s]')
ax1.set_ylabel('fS(s)')
ax1.plot(s, f, 'c,', label='fS')

ax2 = ax1.twinx()

color = 'tab:blue'
ax2.set_ylabel('FD(d)')
ax2.plot(s, integral, 'b,', label='FS')

plt.legend()
plt.show()
'''