function [ outputImg ] = GaussianDenoising(noiseImg, kernSize, spatialKern)
kern = GaussianKernel(kernSize);
outputImg = convn(noiseImg, kern,'same');
end

