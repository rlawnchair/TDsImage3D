clc;
load 'MRIT1w.mat';

% noiseImg = AddGaussianNoise(MRIT1w,5);
% imagesc(noiseImg(:,:,90));
% colormap gray;
% axis image;
% 
% PSNR = ComputePsnr(MRIT1w,noiseImg);
tic;
for i=1 : 21
    [noiseImg,sigma] = AddGaussianNoise(MRIT1w,i);
    PSNR1(i) = ComputePsnr(MRIT1w,noiseImg);
end
x = 1:1:21;
plot(x,PSNR1);

hold on;

for i=1 : 21
    noiseImg = AddRicianNoise(MRIT1w,i);
    PSNR2(i) = ComputePsnr(MRIT1w,noiseImg);
end

x = 1:1:21;
plot(x,PSNR2,'g');
toc;