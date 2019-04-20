import os
from shutil import copyfile


def move(dir1, dir2):
    for name in os.listdir(dir1):
        fpath = os.path.join(dir1, name)
        if os.path.isfile(fpath) and name != 'swap.py':
            copyfile(fpath, os.path.join(dir2, name))
            os.remove(fpath)


def swap(dir1, dir2):
    if not os.path.isdir('swap'):
        os.mkdir('swap')
    move(dir1, 'swap')
    move(dir2, dir1)
    move('swap', dir2)
    os.rmdir('swap')


swap(os.getcwd(), 'excluded')
