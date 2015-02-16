function [ std ] = DonohoWaveletMad( inputImg )

padMRIT1w = zeropad(inputImg);
[af, sf] = farras;
J = 1;
dwtpadMRIT1w = dwt3D(padMRIT1w,J,af);
std = median(abs(dwtpadMRIT1w{J}{7}(:)))/0.6745;
end

