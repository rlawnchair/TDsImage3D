function [ h ] = GaussianKernel( siz)
%h = fspecial3('gaussian',siz);
error(nargchk(1,2,nargin))

if nargin==1
        siz = 5;
end

if numel(siz)==1
    siz = round(repmat(siz,1,3));
elseif numel(siz)~=3
    error('Number of elements in SIZ must be 1 or 3')
else
    siz = round(siz(:)');
end
sig = siz/(4*sqrt(2*log(2)));
siz   = (siz-1)/2;
[x,y,z] = ndgrid(-siz(1):siz(1),-siz(2):siz(2),-siz(3):siz(3));
h = exp(-(x.*x/2/sig(1)^2 + y.*y/2/sig(2)^2 + z.*z/2/sig(3)^2));
h = h/sum(h(:));
end

