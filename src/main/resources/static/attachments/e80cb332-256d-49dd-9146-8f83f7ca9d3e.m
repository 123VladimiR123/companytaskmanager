% This is a 1D FDTD simulation of psi. 

clear all, close all, clc

NN = 250;             % Number of points in the problem space.
hbar = 1.054e-34;     % Plank's constant
m0 = 9.1e-31;         % Free space mass of an electron
meff = 1.0;           % Effective mass: Si is 1.08, Ge is 0.067, GaAs is 0.55
melec = meff*m0;      % Mass of an electron
eV2J = 1.6e-19;       % Energy conversion factors
J2eV = 1/eV2J;

dx = 0.2e-9;        % The cell size
dt = 1e-16;           % Time steps 
ra = (0.5*hbar/melec)*(dt/dx^2)  % ra must be < .15
DX = dx*1e9;       % Cell size in nm.
XX = linspace(0, NN * dx * 1e9, NN);   % Length in nm for plotting

% --- Specify the potential ---------------------

%Elvl = @(n, a) pi ^ 2 * hbar ^ 2 * n ^ 2 / 2 / m0 / a ^ 2 * J2eV;
V=zeros(1,NN);
%V(200:end) = 0.1 * eV2J;
V([99 100 101 149 150 151]) = 0.2 / J2eV;


% ---------------------------------------------------
% Initialize a sine wave in a gaussian envelope

lambda = 100;         % Pulse wavelength
sigma = 20;          % Pulse width
nc = 100;            % Starting position
prl = zeros(1,NN);   % The real part of the state variable
pim = zeros(1,NN);   % The imaginary part of the state variable
ptot = 0.;
%Заменить на синусоиду/косинусоиду в энерг. уровне
for n=1:101
    prl(n) = sin(pi * (n - 1) * dx * 2 / 20e-9) ;
    %pim(n) =  cos(pi * (n - 1) * dx * 2 / 20e-9) ;
    ptot = ptot +  prl(n)^2 + pim(n)^2;
end
pnorm = sqrt(ptot);    % Normalization constant

% Normalize and check
ptot = 0.;
for n=1:NN
    prl(n) = prl(n)/pnorm;
    pim(n) = pim(n)/pnorm;
    ptot = ptot +  prl(n)^2 + pim(n)^2;
end
ptot;                   % This should have the value 1

T = 0;
n_step = 1;
while n_step > 0
    n_step = input('How many time steps  -->');

    % -----------This is the core FDTD program -------------
    for m=1:n_step
        T = T + 1;

        for n=2:NN-1
            prl(n) = prl(n) - ra*(pim(n-1) -2*pim(n) + pim(n+1)) ...
               + (dt/hbar)*V(n)*pim(n);
        end 

        for n=2:NN-1
            pim(n) = pim(n) + ra*(prl(n-1) -2*prl(n) + prl(n+1)) ...
               - (dt/hbar)*V(n)*prl(n);
        end

    end
    % ------------------------------------------------------

    % Calculate the expected values

    PE = 0.;
    for n=1:NN
        psi(n) = prl(n) + i*pim(n);    % Write as a complex function
        PE = PE + psi(n)*psi(n)'*V(n);
    end
    psi*psi';               % This checks normalization
    PE = PE*J2eV;           % Potential energy

    ke = 0. + j*0.;
    for n=2:NN-1
        lap_p = psi(n+1) - 2*psi(n) + psi(n-1);
        ke = ke + lap_p*psi(n)';
    end
    KE = -J2eV*((hbar/dx)^2/(2*melec))*real(ke); % Kinetic energy

    %subplot(2,1,1)
    plot(XX,prl,'k') ;
    hold on;
    plot(XX,pim,'-.k'); hold on;
    plot(XX,J2eV*V,'--k');
    hold off;
    axis( [ 0 DX*NN -.2 .3 ])
    TT = text(5,.15,sprintf('%7.0f fs',T*dt*1e15));
    set(TT,'fontsize',12)
    TT = text(5,-.15,sprintf('KE = %5.3f eV',KE));
    set(TT,'fontsize',12)
    TT = text(25,-.15,sprintf('PE = %5.3f eV',PE));
    set(TT,'fontsize',12)
    TT = text(25,.13,sprintf('E_t_o_t = %5.3f eV',KE+PE));
    set(TT,'fontsize',12)
    xlabel('nm')
    set(gca,'fontsize',12)
    T;
    
    %fprintf('Вероятность отражения %.2d\n', sum(abs(psi(1:199)).^2));
    %fprintf('Вероятность прохождения %.2d\n', sum(abs(psi(200:end)).^2))
end
    fprintf('Вероятность отражения %.2d\n', sum(abs(psi(1:199)).^2));
    fprintf('Вероятность прохождения %.2d\n', sum(abs(psi(200:end)).^2))
