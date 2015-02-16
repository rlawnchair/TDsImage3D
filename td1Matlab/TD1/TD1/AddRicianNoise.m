function [ outputImg] = AddRicianNoise( input, noise)

Ir = AddGaussianNoise(input,noise);

pxMax = max(input(:));
sigma = noise / 100 * pxMax;
Ii = sigma * randn(size(input));

outputImg = sqrt(Ir.^2 + Ii.^2);
end

