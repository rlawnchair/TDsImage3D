clc;
load 'MRIT1w.mat';
h = ones(3,3)/9;
img1 = convn(MRIT1w,h,'same');

figure;
imagesc(MRIT1w(:,:,90));
colormap gray;
axis image;
figure;
imagesc(img1(:,:,90));
colormap gray;
axis image;