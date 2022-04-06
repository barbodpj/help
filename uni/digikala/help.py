import subprocess
import os

char = ''
min_inst = 10000000000000
file = open("./inscount.out", "r")
r = 100
while (r != 0)
    for i in range(33, 127):
        command = chr(i)
        rc = subprocess.run(
            "./pin -t ./source/tools/ManualExamples/obj-intel64/inscount1.so -o inscount.out -- ../crackme_d079a0af0b01789c01d5755c885da4f6",
            shell=True, text=True, input=command)
        r = int(file.read()[6:-1])
        if r < min_inst:
            min_inst = r
            char = i
        file.seek(0)
print(char)
