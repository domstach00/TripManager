import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { LoginFormComponent } from "./auth/login-form/login-form.component";
import { AuthGuard } from "./guard/auth-guard";
import { RegisterComponent } from "./auth/register/register.component";
import { RegisterSuccessComponent } from "./auth/register/register-success/register-success.component";
import { PageNotFoundComponent } from "./page-not-found/page-not-found.component";
import { ActivateAccountComponent } from "./auth/activate-account/activate-account.component";
import { ForgotPasswordComponent } from "./auth/forgot-password/forgot-password.component";


const routes: Routes = [
	{ path: '', redirectTo: '/home', pathMatch: 'full' },
	{ path: 'not-found', component: PageNotFoundComponent },
	{ path: 'login', component: LoginFormComponent },
	{ path: 'register', component: RegisterComponent },
	{ path: 'register-success', component: RegisterSuccessComponent },
	{ path: 'activate-account', component: ActivateAccountComponent },
	{ path: 'forgot-password', component: ForgotPasswordComponent },
	{ path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
	{ path: 'settings', component: HomeComponent, canActivate: [AuthGuard] },
	{ path: 'trips', loadChildren: () => import('./trips/trips.module').then(m => m.TripsModule), canActivate: [AuthGuard] },
	{ path: 'budgets', loadChildren: () => import('./budget/budget.module').then(m => m.BudgetModule), canActivate: [AuthGuard] },
	{ path: '**', redirectTo: '/not-found' },
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule {

}
