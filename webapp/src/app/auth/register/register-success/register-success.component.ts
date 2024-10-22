import { Component } from '@angular/core';
import { TranslateService } from "@ngx-translate/core";
import { Paths } from "../../../shared/_model/paths";

@Component({
  selector: 'app-register-success',
  templateUrl: './register-success.component.html',
  styleUrl: './register-success.component.scss'
})
export class RegisterSuccessComponent {

	constructor(
		protected readonly translate: TranslateService,
	) {
	}

	protected readonly Paths = Paths;
}
