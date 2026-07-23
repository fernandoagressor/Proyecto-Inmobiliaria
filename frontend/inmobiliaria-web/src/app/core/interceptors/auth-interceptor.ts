import { inject } from '@angular/core';
import {
  HttpInterceptorFn
} from '@angular/common/http';

import { AuthService } from '../../features/auth/services/auth.service';

export const authInterceptor: HttpInterceptorFn = (
  request,
  next
) => {

  const authService = inject(AuthService);

  const token = authService.obtenerToken();

  if (!token) {
    return next(request);
  }

  const requestConToken = request.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  });

  return next(requestConToken);
};
