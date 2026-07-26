import { inject } from '@angular/core';
import {
  CanActivateFn,
  Router
} from '@angular/router';

import { AuthService } from '../../features/auth/services/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {

  const authService = inject(AuthService);
  const router = inject(Router);

  const rolUsuario = authService.obtenerRol();

  const rolesPermitidos =
    route.data['roles'] as string[] | undefined;

  if (!authService.estaAutenticado()) {
    return router.createUrlTree(['/login']);
  }

  if (!rolUsuario) {
    authService.eliminarToken();
    return router.createUrlTree(['/login']);
  }

  if (
    !rolesPermitidos ||
    rolesPermitidos.length === 0
  ) {
    return true;
  }

  if (rolesPermitidos.includes(rolUsuario)) {
    return true;
  }

  return router.createUrlTree(['/dashboard']);
};
