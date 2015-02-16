clc;
load 'MRIT1w.mat';


% [noiseMRIT1w,sigma] = AddGaussianNoise(MRIT1w, 5);
% fprintf('Valeur réelle : %f \n',sigma)
% std = DonohoWaveletMad(noiseMRIT1w);
% fprintf('Valeur estimée : %f \n',std)
max = 21;
error = 1:1:max;
for i=1 : max
    tic;
    [noiseMRIT1w,sigma] = AddGaussianNoise(MRIT1w, i);
    fprintf('Valeur réelle pour le niveau de gris %d : %f \n',i,sigma)
    std = DonohoWaveletMad(noiseMRIT1w);
    fprintf('Valeur estimée pour le niveau de gris %d : %f \n',i,std)
    error(i) = abs(1 - (sigma/std));
    fprintf('Taux d erreur %f \n', error(i))
    toc;
end

x = 1:1:max;
plot(x,error);