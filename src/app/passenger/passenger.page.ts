import { Component, OnInit } from '@angular/core';
import { GooglePlus } from '@ionic-native/google-plus/ngx';
import { NativeStorage } from '@ionic-native/native-storage/ngx';
import { LoadingController, AlertController, Platform, ToastController } from '@ionic/angular';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-passenger',
  templateUrl: './passenger.page.html',
  styleUrls: ['./passenger.page.scss'],
})
export class PassengerPage {

  constructor(
    private googlePlus: GooglePlus,
    private nativeStorage: NativeStorage,
    public loadingController: LoadingController,
    private router: Router,
    private platform: Platform,
    public alertController: AlertController,
    public toastCtrl: ToastController
  ) { }

  userDetails = {
    emailId: '',
    password: ''
  };

  public onClickCancel() {
    this.router.navigate(['/tabs/tab1']);
  }


  async logForm() {
    // this.router.navigate(['/search-ride', {userId: this.userDetails.emailId, walletBal: this.userDetails.password}]);
    const result = await this.checkAuthentication();
    if (result['success']) {
        const passanger =  result['passenger'];
        this.presentToast('Login SuccessFul!');
        this.router.navigate(['/search-ride', {userId: passanger.userId, walletBal: passanger.walletBalance}]);
    } else {
      this.presentToast('Invalid Credentials!!');
    }
  }
  async presentToast(msg) {
    const toast = await this.toastCtrl.create({
      message: msg,
      duration: 2000
    });
    toast.present();
  }

  checkAuthentication() {
    console.log(this.userDetails);
    const userDetails = this.userDetails;
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

  registerUser() {
    this.router.navigate(['/register']);
  }
}
