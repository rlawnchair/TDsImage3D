function [ padMRIT1w ] = zeropad( img )
[m,n,o] = size(img);

if mod(m,2) ~= 0 || mod(n,2) ~= 0 || mod(o,2) ~= 0
h = m+1;
w = n+1;
z = o+1;
padMRIT1w = padarray(img, [floor((w - n)/2) floor((h - m)/2) floor((z - m)/2)], 0, 'pre');
padMRIT1w = padarray(padMRIT1w, [ceil((w - n)/2) ceil((h - m)/2) ceil((z - m)/2)], 0, 'post');
else
    padMRIT1w = img;
end
end

