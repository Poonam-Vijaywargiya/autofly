import { Component, OnInit } from '@angular/core';
import { ToastController } from '@ionic/angular';
import { Router } from '@angular/router';
@Component({
  selector: 'app-driver',
  templateUrl: './driver.page.html',
  styleUrls: ['./driver.page.scss'],
})
export class DriverPage {

  constructor(private router: Router, private toastCtrl: ToastController) { }

  driverDetails = {
    emailId: '',
    password: ''
  };

   onClickCancel() {
    this.router.navigate(['/tabs/tab1']);
  }

  async driverLogin() {
    this.router.navigate(['/driver-ride', {driverId: this.driverDetails.emailId, driverWalletBal: this.driverDetails.password}]);
    const result = await this.checkAuthentication();
  // code below here will only execute when await makeRequest() finished loading
    // if (result.success) {
  //     this.router.navigate(['/driver-ride', {driverId: this.driverDetails.emailId, driverWalletBal: this.driverDetails.password}]);
    // } else {
    //   this.presentToast();
    // }
  }
  async presentToast() {
    const toast = await this.toastCtrl.create({
      message: 'Invalid Credentials!!',
      duration: 2000
    });
    toast.present();
  }

  checkAuthentication() {
    console.log(this.driverDetails);
    const driverDetails = this.driverDetails;
     return new Promise(function (resolve, reject) {
      const request = new XMLHttpRequest();
      const method = 'POST';
      const url = 'http://localhost:8181/autofly/login';
      const async = true;

      request.open(method, url, async);
      request.setRequestHeader('Content-Type', 'application/json');
      request.onreadystatechange = function () {
          if (request.readyState === 4) {
              if (request.status === 200) {
                  const response = JSON.parse(request.responseText);
                  resolve(response);
              } else {
                  reject(request.status);
              }
          }
      };
      request.send(JSON.stringify(driverDetails));
  });
  }
}

