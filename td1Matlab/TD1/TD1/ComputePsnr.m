function [ PSNR ] = ComputePsnr( input, noise )
pxMax = max(input(:));
RMSE = sqrt(mean((input(:) - noise(:)).^2));
PSNR = 20*log10(pxMax/RMSE);
end

