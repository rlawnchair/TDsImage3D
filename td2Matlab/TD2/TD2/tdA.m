load 'MRIT1w.mat';
clc;
close all;

[noiseImg,sigma] = AddGaussianNoise(MRIT1w,7);
PSNR(1) = ComputePsnr(MRIT1w,noiseImg);
% imagesc(noiseImg(:,:,90));
% colormap gray;
% axis image;
tic;
[denoised] = GaussianDenoising(noiseImg, 7, 7);
PSNR(2) = ComputePsnr(MRIT1w,denoised);
% imagesc(denoised(:,:,90));
% colormap gray;
% axis image;
toc;