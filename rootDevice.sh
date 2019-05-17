#!/usr/bin/env bash
# 以读写模式打开模拟器(root权限)，模拟器的名称是test，cd ~到用户目录(cd /users/wp)
cd ~/Library/Android/sdk/platform-tools
emulator -avd test -writable-system