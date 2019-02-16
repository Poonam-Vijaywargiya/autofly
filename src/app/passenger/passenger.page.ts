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
    this.router.navigate(['/search-ride', {userId: this.userDetails.emailId, walletBal: this.userDetails.password}]);
    // const result = await this.checkAuthentication();
  // code below here will only execute when await makeRequest() finished loading
    // if (result.success) {
    //     const passanger =  result.passenger;
      //   {
      //     "success": true,
      //     "message": "Login Successful",
      //     "userType": "P",
      //     "driver": null,
      //     "passenger": {
      //         "userId": 1,
      //         "name": "Raj",
      //         "mobNo": "98450212937",
      //         "walletBalance": 500,
      //         "rating": 5
      //     }
      // }
  //     this.router.navigate(['/search-ride', {userId: passanger.userId, walletBal: passanger.walletBalance}]);
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


  async doGoogleLogin() {
    const loading = await this.loadingController.create({
      message: 'Please wait...'
    });
    this.presentLoading(loading);
    this.googlePlus.login({
      'scopes': '', // optional - space-separated list of scopes, If not included or empty, defaults to `profile` and `email`.
      // tslint:disable-next-line:max-line-length
      'offline': true, // Optional, but requires the webClientId - if set to true the plugin will also return a serverAuthCode, which can be used to grant offline access to a non-Google server
      })
      .then(user => {
        // save user data on the native storage
        this.nativeStorage.setItem('google_user', {
          name: user.displayName,
          email: user.email,
          picture: user.imageUrl
        })
        .then(() => {
           this.router.navigate(['/search-ride']);
        }, (error) => {
          console.log(error);
        });
        loading.dismiss();
      }, err => {
        console.log(err);
        if (!this.platform.is('cordova')) {
          this.presentAlert();
        }
        loading.dismiss();
      });
  }

  async presentAlert() {
    const alert = await this.alertController.create({
       message: 'Cordova is not available on desktop. Please try this in a real device or in an emulator.',
       buttons: ['OK']
     });

    await alert.present();
  }


  async presentLoading(loading) {
    return await loading.present();
  }

}
