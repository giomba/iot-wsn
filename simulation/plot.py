import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import numpy as numpy
import pandas as pd

def getcolor(n):
    return ['#aa0000', '#00aa00', '#0000aa', '#aaaa00', '#aa00aa'][int((i - 12) / 2)]

df = pd.read_csv('pretty.csv', delimiter='\s', engine='python')

x = df['k'].unique()

for i in df['i_min'].unique():
    rows = df.loc[df['i_min'] == i]
    plt.errorbar(
        x,
        rows['mean'],
        yerr=rows['ci'],
        marker='.',
        capsize=4,
        ms=3,
        mec='r',
        mfc='r',
        linestyle='-',
        linewidth=1,
        color=getcolor(i),
        ecolor='red'
    )

handles = []
for i in df['i_min'].unique():
    handles.append(mpatches.Patch(color=getcolor(i), label='Imin = ' + str(i)))

plt.legend(handles=handles)

plt.show()

