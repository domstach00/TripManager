import { Component, Input } from '@angular/core';
import { Paths } from "../../_model/paths";
import { RouterService } from "../../_service/router.service";

export interface NavSideBarItem {
	name: string;
	icon?: string;
	path?: Paths;
	pathParams?: string[];
}

@Component({
	selector: 'app-side-bar',
	templateUrl: './side-bar.component.html',
	styleUrls: ['./side-bar.component.scss'],
})
export class SideBarComponent {
	@Input() isExpandedMenu: boolean = false;
	@Input() navbarHasHeader: boolean = false;
	@Input() navSideBarItems: NavSideBarItem[];

	constructor(
		readonly routerService: RouterService,
	) {
	}

	navigate(path?: Paths, pathParams?: string[]) {
		if (!path) {
			return;
		}
		this.routerService.navTo(path, pathParams);
	}

	toggleMenuExpansion() {
		this.isExpandedMenu = !this.isExpandedMenu
	}
}
