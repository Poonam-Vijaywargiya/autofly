import { Component } from '@angular/core';
import { ToastController } from '@ionic/angular';
import { Router } from '@angular/router';
@Component({
  selector: 'app-driver-ride',
  templateUrl: './driver-ride.page.html',
  styleUrls: ['./driver-ride.page.scss'],
})
export class DriverRidePage {

  constructor(private router: Router, private toastCtrl: ToastController) { }

  isToggled: Boolean = false;

  async getDriverLocation() {
    if (this.isToggled) {
// call the api to send the current location
    const result = await this.getNearestHotSpot();
    }
    this.presentToast(this.isToggled);
  }

  async presentToast(onDuty) {
    let message;
    if (onDuty) {
      message = 'Thanks for your availability, please go to the nearest hotspot marked in the map.';
    } else {
      message = 'Thanks for your service, you have earned ' + this.driverWalletBal + 'today.';
    }
    const toast = await this.toastCtrl.create({
      message: message,
      duration: 2000
    });
    toast.present();
  }

  getNearestHotSpot() {
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
      request.send(JSON.stringify(userDetails));
  });

}
