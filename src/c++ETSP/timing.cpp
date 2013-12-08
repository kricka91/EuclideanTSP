#include "timing.h"
#include <sys/time.h>
#include <cstdlib>
#include <cmath>
#include <cstdio>

int startTime, endTime, partialTime;
bool measureTime = true;

int getTime() {
	timeval ti;
	gettimeofday(&ti, 0);
	return ti.tv_usec;
}

int timeSinceStart() {
	return getTime() - startTime;
}

