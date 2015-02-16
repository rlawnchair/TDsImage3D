clc;
load 'MRIT1w.mat';
tic;

padMRIT1w = zeropad(MRIT1w);

[af, sf] = farras;
J = 1;
dwtpadMRIT1w = dwt3D(padMRIT1w,J,af);
figure(1);
imagesc(dwtpadMRIT1w{2}(:,:,45))
colormap gray;
axis image;

figure(2);
imagesc(dwtpadMRIT1w{J}{7}(:,:,45))
colormap gray;
axis image;
toc;