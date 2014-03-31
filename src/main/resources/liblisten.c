#include <stdlib.h>
#include <stdio.h>

#include "xhklib.h"
#include "io_loli_sc_core_HotKeyRegister.h"

void endListen(xhkEvent e, void *r1, void *r2, void *r3)
{
}

JNIEXPORT jint JNICALL Java_io_loli_sc_core_HotKeyRegister_bindAndListen
  (JNIEnv * env, jclass clazz, jint modifiers)
{
    xhkConfig *hkconfig;
    hkconfig = xhkInit(NULL);
    xhkBindKey(hkconfig, 0, XK_H, modifiers, xhkKeyPress, &endListen, 0, 0, 0);
    xhkPollKeys(hkconfig, 1);
    xhkClose(hkconfig);
    return 0;
}