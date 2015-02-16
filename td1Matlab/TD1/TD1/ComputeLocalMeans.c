#include "mex.h"
#include "matrix.h"
#include <math.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>

void mexFunction( int nlhs, mxArray *plhs[],int nrhs, const mxArray*prhs[] )
{
    /*Declarations*/
    double *input, *output;
    int ndims;
    const int *dims;
    int v;
    
    /*Copy input pointer*/
    input = (double*)mxGetPr(prhs[0]);
    ndims = mxGetNumberOfDimensions(prhs[0]);
    dims = mxGetDimensions(prhs[0]);
    
    /*Copy input parameters*/
    v = (int) (mxGetScalar(prhs[1]));
    
    /*Allocate memory and assign output pointer*/
    plhs[0] = mxCreateNumericArray(ndims, dims, mxDOUBLE_CLASS, mxREAL);
    
    /*Get a pointer to the data space in our newly allocated memory*/
    output = (double*) mxGetPr(plhs[0]);
    
    return;
}