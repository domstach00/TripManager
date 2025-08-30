import { CanMatchFn, Router } from "@angular/router";
import { inject } from "@angular/core";
import { InitService } from "../initialization/_service/init.service";
import { catchError, map, of } from "rxjs";
import { Paths } from "../shared/_model/paths";

export const initializationEnabledCanMach: CanMatchFn = () => {
	const initService = inject(InitService);
	const router = inject(Router);

	return initService.checkEnabled().pipe(
		map(enabled => enabled ? true : router.parseUrl(Paths.login)),
		catchError(() => of(router.parseUrl(Paths.login)))
	);
}
