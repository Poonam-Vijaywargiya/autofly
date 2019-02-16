import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', loadChildren: './tabs/tabs.module#TabsPageModule' },
  { path: 'passenger', loadChildren: './passenger/passenger.module#PassengerPageModule' },
  { path: 'driver', loadChildren: './driver/driver.module#DriverPageModule' },
  { path: 'register', loadChildren: './register/register.module#RegisterPageModule' },
  { path: 'search-ride', loadChildren: './search-ride/search-ride.module#SearchRidePageModule' },
  { path: 'driver-ride', loadChildren: './driver-ride/driver-ride.module#DriverRidePageModule' }
];
@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {}
